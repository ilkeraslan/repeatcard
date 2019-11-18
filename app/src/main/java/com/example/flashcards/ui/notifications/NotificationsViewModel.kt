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
        addNotificationsData(
            Flashcard(4,"Lorem ipsum dolor sit amet this is an example notification-4")
        )
        addNotificationsData(
            Flashcard(5,"Lorem ipsum dolor sit amet this is an example notification-5")
        )
        addNotificationsData(
            Flashcard(6,"Lorem ipsum dolor sit amet this is an example notification-6")
        )
        addNotificationsData(
            Flashcard(7,"Lorem ipsum dolor sit amet this is an example notification-7")
        )
        addNotificationsData(
            Flashcard(8,"Lorem ipsum dolor sit amet this is an example notification-8")
        )
        addNotificationsData(
            Flashcard(9,"Lorem ipsum dolor sit amet this is an example notification-9")
        )
    }

    val notifications_list : LiveData<MutableList<Flashcard>> = notifications_data

    // Public method to add data
    fun addNotificationsData(notification: Flashcard) {
        notifications_data.value?.add(notification)
    }
}
