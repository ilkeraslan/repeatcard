package com.example.flashcards.ui.directories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.flashcard_directory.FlashcardDirectory
import com.example.flashcards.db.flashcard_directory.FlashcardDirectoryRepository
import kotlinx.coroutines.launch


sealed class DirectoryEvent {
    object Load : DirectoryEvent()
    data class AddDirectory(val directory: FlashcardDirectory) : DirectoryEvent()
    data class DeleteDirectory(val directory: FlashcardDirectory) : DirectoryEvent()
}

sealed class DirectoryState {
    // TODO: object InProgress : DirectoryState()
    data class Error(val error: Throwable) : DirectoryState()

    data class Success(val directories: List<FlashcardDirectory>) : DirectoryState()
}

class DirectoriesViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun newInstance(application: Application) = DirectoriesViewModel(application)
    }

    private val repository: FlashcardDirectoryRepository
    val allDirectories = MutableLiveData<List<FlashcardDirectory>>()
    var state: MutableLiveData<DirectoryState> = MutableLiveData()

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(application).directoryDao()
        repository = FlashcardDirectoryRepository(directoriesDao)
        updateDirectories()
    }

    fun send(event: DirectoryEvent) {
        when (event) {
            is DirectoryEvent.Load -> loadContent()
            is DirectoryEvent.AddDirectory -> {
                insert(event.directory)
                loadContent()
            }
            is DirectoryEvent.DeleteDirectory -> deleteDirectory(event.directory)
        }
    }

    private fun loadContent() {
        // TODO: Handle other states
        state.value = DirectoryState.Success(allDirectories.value!!.toList())
    }

    private fun insert(directory: FlashcardDirectory) = viewModelScope.launch {
        repository.insert(directory)
    }

    private fun updateDirectories() = viewModelScope.launch {
        allDirectories.postValue(repository.getDirectories())
        state.value = DirectoryState.Success(repository.getDirectories())
    }

    private fun deleteDirectory(directory: FlashcardDirectory) = viewModelScope.launch {
        repository.deleteDirectory(directory)
    }
}
