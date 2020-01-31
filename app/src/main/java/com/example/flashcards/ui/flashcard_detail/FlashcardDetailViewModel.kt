package com.example.flashcards.ui.flashcard_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.flashcard.FlashcardRepository
import kotlinx.coroutines.launch


// Events that FlashcardDetailActivity can send
sealed class FlashcardDetailEvent {
    object Load : FlashcardDetailEvent()
}

// States that FlashcardDetailViewModel can have
sealed class FlashcardDetailState {
    data class Error(val error: Throwable) : FlashcardDetailState()
    data class Success(val flashcard: Flashcard) : FlashcardDetailState()
}


class FlashcardDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    val flashcard = MutableLiveData<Flashcard>()
    var state: MutableLiveData<FlashcardDetailState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
    }

    private fun updateFlashcard(id: Int) = viewModelScope.launch {
        flashcard.postValue(repository.getFlashcard(id))
        state.value = FlashcardDetailState.Success(repository.getFlashcard(id))
    }

    fun send(event: FlashcardDetailEvent, id: Int) {
        when (event) {
            is FlashcardDetailEvent.Load -> loadContent(id)
        }
    }

    private fun loadContent(flashcard_id: Int) = viewModelScope.launch {
        state.value = FlashcardDetailState.Success(
            repository.getFlashcard(flashcard_id)
            /*Flashcard(
                flashcard_id,
                "Test Title",
                null,
                null,
                null
            )*/
        )
    }
}