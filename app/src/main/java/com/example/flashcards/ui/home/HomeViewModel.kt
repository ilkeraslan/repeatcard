package com.example.flashcards.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.flashcard.FlashcardRepository
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.directory.FlashcardDirectoryRepository
import kotlinx.coroutines.launch


// Events that HomeFragment can send
sealed class FlashcardEvent {
    data class AddFlashcard(val flashcard: Flashcard) : FlashcardEvent()
    object DeleteAll : FlashcardEvent()
    data class DeleteFlashcard(val id: Int) : FlashcardEvent()
    object Load : FlashcardEvent()
}

// States that a Flashcard can have
sealed class FlashcardState {
    data class Error(val error: Throwable) : FlashcardState()
    data class Success(val flashcards: List<Flashcard>) : FlashcardState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun newInstance(application: Application) = HomeViewModel(application)
    }

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
            is FlashcardEvent.DeleteAll -> deleteAll()
            is FlashcardEvent.DeleteFlashcard -> deleteFlashcard(event.id)
            is FlashcardEvent.Load -> loadContent()
        }
    }

    private fun addFlashcardToDirectory(id: Int) {
        directoryRepository.
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
