package com.example.flashcards.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashcards.ui.flashcard.Flashcard

class NotificationsViewModel : ViewModel() {

    private val notifications_data = MutableLiveData<MutableList<Flashcard>>()

    init {
        notifications_data.value = ArrayList()

        addNotificationsData(
            Flashcard(1,"Lorem ipsum dolor sit amet this is an example notification-1")
        )
        addNotificationsData(
            Flashcard(2,"Lorem ipsum dolor sit amet this is an example notification-2")
        )
        addNotificationsData(
            Flashcard(3,"Lorem ipsum dolor sit amet this is an example notification-3")
        )
    }

    val notifications_list : LiveData<MutableList<Flashcard>> = notifications_data

    // Public method to add data
    fun addNotificationsData(notification: Flashcard) {
        notifications_data.value?.add(notification)
    }
}
