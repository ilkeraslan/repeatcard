package com.example.flashcards.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashcards.ui.flashcard.Flashcard


// Events that HomeFragment can send
sealed class FlashcardEvent {
    object Load : FlashcardEvent()
    data class AddFlashcard(val flashcard: Flashcard) : FlashcardEvent()
}

// States that a Flashcard can have
sealed class FlashcardState {
    object InProgress : FlashcardState()
    data class Error(val error: Throwable) : FlashcardState()
    object Success : FlashcardState()
}

class HomeViewModel : ViewModel() {

    private val home_data = MutableLiveData<MutableList<Flashcard>>()
    var state: MutableLiveData<FlashcardState> = MutableLiveData()

    init {
        home_data.value = ArrayList()
    }

    val flashcards_list: LiveData<MutableList<Flashcard>> = home_data

    // Public method to add Flashcard
    fun addFlashcard(flashcard: Flashcard) {
        val newList = home_data
        newList.value?.add(flashcard)
        home_data.value = newList.value
    }

    fun send(event: FlashcardEvent) {
        // TODO
        when(event) {
            is FlashcardEvent.AddFlashcard -> {
                addFlashcard(flashcard = event.flashcard)
                state.value = FlashcardState.Success
            }
        }
    }
}
