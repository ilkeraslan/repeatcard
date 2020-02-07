package com.example.flashcards.ui.directories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.directory.Directory
import com.example.flashcards.db.directory.FlashcardDirectoryRepository
import kotlinx.coroutines.launch


sealed class DirectoryEvent {
    object Load : DirectoryEvent()
    data class AddDirectory(val directory: Directory) : DirectoryEvent()
    data class DeleteDirectory(val directory: Directory) : DirectoryEvent()
    data class GetDirectoryContent(val directoryId: Int) : DirectoryEvent()
}

sealed class DirectoryState {
    data class Error(val error: Throwable) : DirectoryState()
    data class Success(val directories: List<Directory>) : DirectoryState()
    data class DirectoryContentSuccess(val flashcards: List<Flashcard>) : DirectoryState()
}

class DirectoriesViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun newInstance(application: Application) = DirectoriesViewModel(application)
    }

    private val repository: FlashcardDirectoryRepository
    val allDirectories = MutableLiveData<List<Directory>>()
    var state: MutableLiveData<DirectoryState> = MutableLiveData()
    var directoryState: MutableLiveData<DirectoryState> = MutableLiveData()

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(application).directoryDao()
        repository = FlashcardDirectoryRepository(directoriesDao)
        updateDirectories()
    }

    fun send(event: DirectoryEvent) {
        when (event) {
            is DirectoryEvent.Load -> loadDirectories()
            is DirectoryEvent.AddDirectory -> {
                insert(event.directory)
                loadDirectories()
            }
            is DirectoryEvent.DeleteDirectory -> deleteDirectory(event.directory)
            is DirectoryEvent.GetDirectoryContent -> getDirectoryContent(event.directoryId)
        }
    }

    private fun deleteDirectory(directory: Directory) = viewModelScope.launch {
        repository.deleteDirectory(directory)
    }

    private fun getDirectoryContent(directoryId: Int) = viewModelScope.launch {
        directoryState.value =
            DirectoryState.DirectoryContentSuccess(repository.getDirectoryContent(directoryId))
    }

    private fun loadDirectories() {
        // TODO: Handle other states
        state.value = DirectoryState.Success(allDirectories.value!!.toList())
    }

    private fun insert(directory: Directory) = viewModelScope.launch {
        repository.addDirectory(directory)
        updateDirectories()
    }

    private fun updateDirectories() = viewModelScope.launch {
        allDirectories.postValue(repository.getDirectories())
        state.value = DirectoryState.Success(repository.getDirectories())
    }
}
