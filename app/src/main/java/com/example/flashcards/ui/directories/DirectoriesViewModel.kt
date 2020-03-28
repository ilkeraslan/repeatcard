package com.example.flashcards.ui.directories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.directory.Directory
import com.example.flashcards.db.directory.FlashcardDirectoryRepository
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.flashcard.FlashcardRepository
import com.example.flashcards.ui.util.exhaustive
import kotlinx.coroutines.launch

const val DEFAULT_DIRECTORY_NAME = "Miscellaneous"

sealed class DirectoryEvent {
    object Load : DirectoryEvent()
    data class AddDirectory(val directory: Directory) : DirectoryEvent()
    data class DeleteDirectory(val id: Int) : DirectoryEvent()
    data class GetDirectoryContent(val directoryId: Int) : DirectoryEvent()
}

sealed class DirectoryState {
    data class Error(val error: Throwable) : DirectoryState()
    data class Success(val directories: List<Directory>) : DirectoryState()
    data class DirectoryContentSuccess(val flashcards: List<Flashcard>) : DirectoryState()
}

class DirectoriesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardDirectoryRepository
    private val flashcardRepository: FlashcardRepository
    val allDirectories = MutableLiveData<List<Directory>>()
    var directoriesState: MutableLiveData<DirectoryState> = MutableLiveData()
    var directoryState: MutableLiveData<DirectoryState> = MutableLiveData()

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(application).directoryDao()
        val flashcardDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardDirectoryRepository(directoriesDao)
        flashcardRepository = FlashcardRepository(flashcardDao)
        loadContent()
    }

    fun send(event: DirectoryEvent) {
        when (event) {
            is DirectoryEvent.AddDirectory -> {
                insert(event.directory)
                loadContent()
            }
            is DirectoryEvent.DeleteDirectory -> deleteDirectory(event.id)
            is DirectoryEvent.GetDirectoryContent -> getDirectoryContent(event.directoryId)
            is DirectoryEvent.Load -> loadContent()
        }.exhaustive
    }

    private fun deleteDirectory(id: Int) = viewModelScope.launch {
        // Disassociate any Flashcard before deleting
        val flashcards = repository.getDirectoryContent(id)
        if (flashcards.isNotEmpty()) {
            flashcards.forEach { flashcard ->
                flashcard.directory_id = null
                flashcardRepository.updateFlashcard(flashcard)
            }
        }
        repository.deleteDirectory(id)
        loadContent()
    }

    private fun getDirectoryContent(directoryId: Int) = viewModelScope.launch {
        val directoryContent = flashcardRepository.getFlashcardsForDirectory(directoryId)

        if (directoryContent.isEmpty()) {
            directoryState.postValue(DirectoryState.Error(NullPointerException()))
        } else {
            directoryState.postValue(
                DirectoryState.DirectoryContentSuccess(flashcardRepository.getFlashcardsForDirectory(directoryId))
            )
        }
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
            allDirectories.postValue((listOf(defaultDirectory)))
        } else {
            allDirectories.postValue((repository.getDirectories()))
        }
        directoriesState.postValue(DirectoryState.Success(repository.getDirectories()))
    }
}
