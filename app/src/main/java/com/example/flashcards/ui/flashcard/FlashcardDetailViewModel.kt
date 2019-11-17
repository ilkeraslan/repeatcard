package com.example.flashcards.ui.flashcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlashcardDetailViewModel : ViewModel() {

    private val flashcard = MutableLiveData<Flashcard>()
    private val navigate_to_home = MutableLiveData<Boolean?>()

    init {
        flashcard.value = Flashcard(1, "flashcard-1")
        navigate_to_home.value = true
    }

    val flashcard_detail = flashcard
    val navigate_to_home_state = navigate_to_home
}