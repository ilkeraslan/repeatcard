package com.repeatcard.app.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.repeatcard.app.db.FlashcardDatabase
import com.repeatcard.app.db.directory.Directory
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.db.notification.Notification
import com.repeatcard.app.db.notification.NotificationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

sealed class NotificationEvent {
    data class AddFlashcard(val flashcard: Flashcard) : NotificationEvent()
    data class AddDirectory(val directory: Directory) : NotificationEvent()
    data class AddToDirectory(val flashcardId: Int) : NotificationEvent()
    data class DeleteFlashcard(val flashcardId: Int) : NotificationEvent()
    data class DeleteDirectory(val directoryId: Int) : NotificationEvent()
    data class DeleteNotification(val notificationId: Int) : NotificationEvent()
    object DeleteAll : NotificationEvent()
    object Load : NotificationEvent()
}

sealed class NotificationState {
    data class Error(val error: Throwable, val notifications: List<Notification>) : NotificationState()
    data class Success(val notifications: List<Notification>) : NotificationState()
}

@ExperimentalCoroutinesApi
class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotificationRepository
    val state: MutableLiveData<NotificationState> = MutableLiveData()

    init {
        val notificationsDao = FlashcardDatabase.getDatabase(application).notificationsDao()
        repository = NotificationRepository(notificationsDao)
        loadContent()
    }

    @ExperimentalCoroutinesApi
    fun send(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.AddDirectory -> insert(createNotification("Added new directory", "directory"))
            is NotificationEvent.AddFlashcard -> insert(createNotification("Added new flashcard", "flashcard"))
            is NotificationEvent.AddToDirectory -> insert(createNotification("Added to directory", "flashcard"))
            is NotificationEvent.DeleteDirectory -> insert(createNotification("Deleted directory", "directory"))
            is NotificationEvent.DeleteFlashcard -> insert(createNotification("Deleted flashcard", "flashcard"))
            is NotificationEvent.DeleteNotification -> deleteNotification(event.notificationId)
            is NotificationEvent.DeleteAll -> deleteAll()
            is NotificationEvent.Load -> loadContent()
        }
    }

    private fun createNotification(title: String, type: String): Notification {
        return Notification(
            notificationId = 0,
            notificationTitle = title,
            notificationType = type,
            creationDate = OffsetDateTime.now().format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)
                    .withZone(ZoneId.systemDefault())
            )
        )
    }

    @ExperimentalCoroutinesApi
    fun insert(notification: Notification) = viewModelScope.launch {
        repository.insertNotification(notification)
        loadContent()
    }

    @ExperimentalCoroutinesApi
    private fun loadContent() = viewModelScope.launch {
        val notifications =
            withContext(viewModelScope.coroutineContext) { repository.getNotifications() }
        if (notifications.isEmpty()) {
            state.postValue(NotificationState.Error(NullPointerException(), mutableListOf()))
        } else {
            state.postValue(NotificationState.Success(notifications))
        }
    }

    private fun deleteNotification(id: Int) = viewModelScope.launch {
        repository.deleteNotification(id)
        loadContent()
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        loadContent()
    }
}
