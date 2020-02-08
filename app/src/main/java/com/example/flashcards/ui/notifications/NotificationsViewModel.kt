package com.example.flashcards.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.directory.Directory
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.notification.Notification
import com.example.flashcards.db.notification.NotificationRepository
import kotlinx.coroutines.launch


sealed class NotificationEvent {
    data class AddFlashcard(val flashcard: Flashcard) : NotificationEvent()
    data class AddDirectory(val directory: Directory) : NotificationEvent()
    data class DeleteFlashcard(val flashcard: Flashcard) : NotificationEvent()
    data class DeleteDirectory(val directory: Directory) : NotificationEvent()
    object DeleteAll : NotificationEvent()
    object Load : NotificationEvent()
}

sealed class NotificationState {
    data class Error(val error: Throwable) : NotificationState()
    data class Success(val notifications: List<Notification>) : NotificationState()
}


class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun newInstance(application: Application) = NotificationsViewModel(application)
    }

    private val repository: NotificationRepository
    val state: MutableLiveData<NotificationState> = MutableLiveData()

    init {
        val notificationsDao = FlashcardDatabase.getDatabase(application).notificationsDao()
        repository = NotificationRepository(notificationsDao)
        loadContent()
    }

    fun send(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.AddDirectory -> insert(
                Notification(
                    notificationId = 0,
                    notificationTitle = "Added new directory",
                    notificationType = "directory",
                    creationDate = "now"
                )
            )
            is NotificationEvent.AddFlashcard -> insert(
                Notification(
                    notificationId = 0,
                    notificationTitle = "Added new flashcard",
                    notificationType = "flashcard",
                    creationDate = "now"
                )
            )
            is NotificationEvent.DeleteAll -> deleteAll()
            is NotificationEvent.Load -> loadContent()
        }
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        loadContent()
    }

    private fun loadContent() = viewModelScope.launch {
        val notifications = repository.getNotifications()
        state.postValue(NotificationState.Success(notifications))
    }

    fun insert(notification: Notification) = viewModelScope.launch {
        repository.insertNotification(notification)
        loadContent()
    }
}
