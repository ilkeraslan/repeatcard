package com.example.flashcards.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.flashcard.FlashcardRepository
import com.example.flashcards.db.FlashcardDatabase
import kotlinx.coroutines.launch


// Events that HomeFragment can send
sealed class FlashcardEvent {
    object Load : FlashcardEvent()
    data class AddFlashcard(val flashcard: Flashcard) : FlashcardEvent()
    object DeleteAll: FlashcardEvent()
    // TODO: data class DeleteFlashcard(val id: Int) : FlashcardEvent()
}

// States that a Flashcard can have
sealed class FlashcardState {
    //TODO: object InProgress : FlashcardState()
    data class Error(val error: Throwable) : FlashcardState()

    data class Success(val flashcards: List<Flashcard>) : FlashcardState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun newInstance(application: Application) = HomeViewModel(application)
    }

    private val repository: FlashcardRepository
    val allFlashcards = MutableLiveData<List<Flashcard>>()
    var state: MutableLiveData<FlashcardState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
        updateFlashcards()
    }

    fun send(event: FlashcardEvent) {
        when (event) {
            is FlashcardEvent.Load -> loadContent()
            is FlashcardEvent.AddFlashcard -> {
                insert(flashcard = event.flashcard)
                loadContent()
            }
            is FlashcardEvent.DeleteAll -> deleteAll()
        }
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        updateFlashcards()
    }

    private fun loadContent() {
        // TODO: handle other states
        state.value = FlashcardState.Success(allFlashcards.value!!.toList())
    }

    private fun insert(flashcard: Flashcard) = viewModelScope.launch {
        repository.insert(flashcard)
        updateFlashcards()
    }

    private fun updateFlashcards() = viewModelScope.launch {
        allFlashcards.postValue(repository.getFlashcards())
        state.value = FlashcardState.Success(repository.getFlashcards())
    }
}
