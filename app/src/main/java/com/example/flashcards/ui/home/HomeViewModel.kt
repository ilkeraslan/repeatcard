package com.example.flashcards.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashcards.ui.flashcard.Flashcard
import kotlin.random.Random

class HomeViewModel : ViewModel() {

    private val home_data = MutableLiveData<MutableList<Flashcard>>()

    init {
        home_data.value = ArrayList()

        for (i in 1..10) {
            addFlashcard(Flashcard(Random.nextInt(), "example flashcard"))
        }
    }

    val flashcards_list: LiveData<MutableList<Flashcard>> = home_data

    // Public method to add Flashcard
    fun addFlashcard(flashcard: Flashcard) {
        home_data.value?.add(flashcard)
    }
}
