package com.repeatcard.app.ui.directories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.repeatcard.app.db.FlashcardDatabase
import com.repeatcard.app.db.directory.Directory
import com.repeatcard.app.db.directory.FlashcardDirectoryRepository
import com.repeatcard.app.db.flashcard.FlashcardRepository
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.launch

const val DEFAULT_DIRECTORY_NAME = "Miscellaneous"

sealed class DirectoriesEvent {
    object Load : DirectoriesEvent()
    data class AddDirectories(val directory: Directory) : DirectoriesEvent()
    data class DeleteDirectories(val id: Int) : DirectoriesEvent()
}

sealed class DirectoriesState {
    data class Error(val error: Throwable) : DirectoriesState()
    data class Success(val directories: List<Directory>) : DirectoriesState()
}

class DirectoriesViewModel(context: Context) : ViewModel() {

    private val repository: FlashcardDirectoryRepository
    private val flashcardRepository: FlashcardRepository
    val allDirectories = MutableLiveData<List<Directory>>()
    var directoriesState: MutableLiveData<DirectoriesState> = MutableLiveData()

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(context).directoryDao()
        val flashcardDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        repository = FlashcardDirectoryRepository(directoriesDao)
        flashcardRepository = FlashcardRepository(flashcardDao)
        loadContent()
    }

    fun send(event: DirectoriesEvent) {
        when (event) {
            is DirectoriesEvent.AddDirectories -> insert(event.directory)
            is DirectoriesEvent.DeleteDirectories -> deleteDirectory(event.id)
            is DirectoriesEvent.Load -> loadContent()
        }.exhaustive
    }

    private fun deleteDirectory(id: Int) = viewModelScope.launch {
        // Disassociate any Flashcard before deleting
        val flashcards = repository.getDirectoryContent(id)
        if (flashcards.isNotEmpty()) {
            flashcards.forEach { flashcard ->
                flashcard.directoryId = null
                flashcardRepository.updateFlashcard(flashcard)
            }
        }
        repository.deleteDirectory(id)
        loadContent()
    }

    private fun insert(directory: Directory) = viewModelScope.launch {
        val directories = repository.getDirectories()
        var canAddDirectory = true

        directories.forEach { existingDirectory ->
            if (existingDirectory.title == directory.title) canAddDirectory = false
        }

        if (canAddDirectory) repository.addDirectory(directory)
        loadContent()
    }

    private fun loadContent() = viewModelScope.launch {
        val directories = repository.getDirectories()
        if (directories.isEmpty()) {
            val defaultDirectory = Directory(1, DEFAULT_DIRECTORY_NAME, null)
            repository.addDirectory(defaultDirectory)
            allDirectories.postValue(listOf(defaultDirectory))
        } else {
            allDirectories.postValue(repository.getDirectories())
        }
        directoriesState.postValue(DirectoriesState.Success(repository.getDirectories()))
    }
}
