package it.ilker.repeatcard.ui.logs

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.directory.Directory
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.db.notification.Notification
import it.ilker.repeatcard.db.notification.NotificationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

sealed class LogEvent {
    data class AddFlashcard(val flashcard: Flashcard) : LogEvent()
    data class AddDirectory(val directory: Directory) : LogEvent()
    data class AddToDirectory(val flashcardId: Int) : LogEvent()
    data class DeleteFlashcard(val flashcardId: Int) : LogEvent()
    data class DeleteDirectory(val directoryId: Int) : LogEvent()
    data class DeleteLog(val notificationId: Int) : LogEvent()
    object DeleteAll : LogEvent()
    object Load : LogEvent()
}

sealed class LogState {
    data class Error(val error: Throwable, val notifications: List<Notification>) : LogState()
    data class Success(val notifications: List<Notification>) : LogState()
}

@ExperimentalCoroutinesApi
class LogsViewModel(context: Context) : ViewModel() {

    private val repository: NotificationRepository
    val state: MutableLiveData<LogState> = MutableLiveData()

    init {
        val logsDao = FlashcardDatabase.getDatabase(context).notificationsDao()
        repository = NotificationRepository(logsDao)
    }

    @ExperimentalCoroutinesApi
    fun send(event: LogEvent) {
        when (event) {
            is LogEvent.AddDirectory -> insert(createLog("Added new directory", "directory"))
            is LogEvent.AddFlashcard -> insert(createLog("Added new flashcard", "flashcard"))
            is LogEvent.AddToDirectory -> insert(createLog("Added to directory", "flashcard"))
            is LogEvent.DeleteDirectory -> insert(createLog("Deleted directory", "directory"))
            is LogEvent.DeleteFlashcard -> insert(createLog("Deleted flashcard", "flashcard"))
            is LogEvent.DeleteLog -> deleteLog(event.notificationId)
            is LogEvent.DeleteAll -> deleteAll()
            is LogEvent.Load -> loadContent()
        }
    }

    private fun createLog(title: String, type: String): Notification {
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
        val notifications = repository.getNotifications()
        if (notifications.isEmpty()) {
            state.postValue(LogState.Error(NullPointerException(), mutableListOf()))
        } else {
            state.postValue(LogState.Success(notifications))
        }
    }

    private fun deleteLog(id: Int) = viewModelScope.launch {
        repository.deleteNotification(id)
        loadContent()
    }

    private fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        loadContent()
    }
}
