package com.example.flashcards.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.Flashcard
import com.example.flashcards.db.FlashcardDbManager
import com.example.flashcards.db.FlashcardRepository
import com.example.flashcards.db.FlashcardRoomDatabase
import kotlinx.coroutines.launch


// Events that HomeFragment can send
sealed class FlashcardEvent {
    object Load : FlashcardEvent()
    data class AddFlashcard(val flashcard: Flashcard) : FlashcardEvent()
}

// States that a Flashcard can have
sealed class FlashcardState {
    // object InProgress : FlashcardState()
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
        val flashcardsDao = FlashcardRoomDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
        updateFlashcards()
    }

    fun send(event: FlashcardEvent) {
        when (event) {
            is FlashcardEvent.Load -> loadContent()
            is FlashcardEvent.AddFlashcard -> {
                insert(flashcard = event.flashcard)
                state.value = FlashcardState.Success(allFlashcards.value!!.toList())
            }
        }
    }

    private fun loadContent() {
        state.value = FlashcardState.Success(allFlashcards.value!!.toList())
    }

    private fun updateFlashcards() = viewModelScope.launch {
        allFlashcards.postValue(repository.getFlashcards())
    }

    fun insert(flashcard: Flashcard) = viewModelScope.launch {
        repository.insert(flashcard)
        updateFlashcards()
    }
}
