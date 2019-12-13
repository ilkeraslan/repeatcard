package com.example.flashcards.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashcards.db.flashcard.Flashcard

class NotificationsViewModel : ViewModel() {

    private val notifications_data = MutableLiveData<MutableList<Flashcard>>()

    init {
        notifications_data.value = ArrayList()
    }

    val notifications_list : LiveData<MutableList<Flashcard>> = notifications_data

    // Public method to add data
    fun addNotificationsData(notification: Flashcard) {
        notifications_data.value?.add(notification)
    }
}
