package com.repeatcard.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.repeatcard.app.db.FlashcardDatabase
import com.repeatcard.app.db.directory.FlashcardDirectoryRepository
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.db.flashcard.FlashcardRepository
import kotlinx.coroutines.launch

sealed class FlashcardEvent {
    data class AddFlashcard(val flashcard: Flashcard) : FlashcardEvent()
    data class AddToDirectory(val id: Int, val directoryId: Int) : FlashcardEvent()
    object DeleteAll : FlashcardEvent()
    data class DeleteFlashcard(val id: Int) : FlashcardEvent()
    object Load : FlashcardEvent()
}

sealed class FlashcardState {
    data class Error(val error: Throwable) : FlashcardState()
    data class Success(val flashcards: List<Flashcard>) : FlashcardState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    private val directoryRepository: FlashcardDirectoryRepository
    var state: MutableLiveData<FlashcardState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        val directoryDao = FlashcardDatabase.getDatabase(application).directoryDao()
        repository = FlashcardRepository(flashcardsDao)
        directoryRepository = FlashcardDirectoryRepository(directoryDao)
        loadContent()
    }

    fun send(event: FlashcardEvent) {
        when (event) {
            is FlashcardEvent.AddFlashcard -> {
                insert(flashcard = event.flashcard)
                loadContent()
            }
            is FlashcardEvent.AddToDirectory -> addFlashcardToDirectory(event.id, event.directoryId)
            is FlashcardEvent.DeleteAll -> deleteAll()
            is FlashcardEvent.DeleteFlashcard -> deleteFlashcard(event.id)
            is FlashcardEvent.Load -> loadContent()
        }
    }

    private fun addFlashcardToDirectory(flashcardId: Int, directoryId: Int) =
        viewModelScope.launch {
            val flashcardToChange = repository.getFlashcard(flashcardId)
            flashcardToChange.directoryId = directoryId
            repository.updateFlashcard(flashcardToChange)
            loadContent()
        }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        loadContent()
    }

    private fun deleteFlashcard(id: Int) = viewModelScope.launch {
        repository.deleteFlashcard(id)
        loadContent()
    }

    private fun loadContent() = viewModelScope.launch {
        val allFlashcards = repository.getFlashcards()
        state.postValue(FlashcardState.Success(allFlashcards))
    }

    private fun insert(flashcard: Flashcard) = viewModelScope.launch {
        repository.insert(flashcard)
        loadContent()
    }
}
