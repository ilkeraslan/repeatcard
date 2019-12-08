package com.example.flashcards.ui.flashcard_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashcards.db.Flashcard


// Events that FlashcardDetailActivity can send
sealed class FlashcardDetailEvent {
    object Load : FlashcardDetailEvent()
}

// States that FlashcardDetailViewModel can have
sealed class FlashcardDetailState {
    data class Error(val error: Throwable) : FlashcardDetailState()
    data class Success(val flashcard: Flashcard) : FlashcardDetailState()
}


class FlashcardDetailViewModel : ViewModel() {

    var state: MutableLiveData<FlashcardDetailState> = MutableLiveData()

    fun send(event: FlashcardDetailEvent, id: String) {
        when (event) {
            is FlashcardDetailEvent.Load -> loadContent(id)
        }
    }

    private fun loadContent(flashcard_id: String) {
        state.value = FlashcardDetailState.Success(Flashcard(flashcard_id, "Test Title"))
    }

}