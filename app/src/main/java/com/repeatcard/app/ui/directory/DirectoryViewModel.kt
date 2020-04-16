package com.repeatcard.app.ui.directory

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.repeatcard.app.db.FlashcardDatabase
import com.repeatcard.app.db.directory.FlashcardDirectoryRepository
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.db.flashcard.FlashcardRepository
import com.repeatcard.app.db.notification.Notification
import com.repeatcard.app.db.notification.NotificationRepository
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

sealed class DirectoryEvent {
    data class GetDirectoryContent(val directoryId: Int) : DirectoryEvent()
    data class CardDeleted(val directoryId: Int) : DirectoryEvent()
    data class CardAdded(val directoryId: Int, val flashcard: Flashcard) : DirectoryEvent()
}

sealed class DirectoryState {
    data class HasContent(val flashcards: List<Flashcard>) : DirectoryState()
    object NoContent : DirectoryState()
}

class DirectoryViewModel(context: Context) : ViewModel() {

    private val directoryRepository: FlashcardDirectoryRepository
    private val flashcardRepository: FlashcardRepository
    private val logsRepository: NotificationRepository
    var state: MutableLiveData<DirectoryState> = MutableLiveData()

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(context).directoryDao()
        val flashcardDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        val notificationRepository = FlashcardDatabase.getDatabase(context).notificationsDao()
        directoryRepository = FlashcardDirectoryRepository(directoriesDao)
        flashcardRepository = FlashcardRepository(flashcardDao)
        logsRepository = NotificationRepository(notificationRepository)
    }

    fun send(event: DirectoryEvent) {
        when (event) {
            is DirectoryEvent.GetDirectoryContent -> getDirectoryContent(event.directoryId)
            is DirectoryEvent.CardAdded -> addCard(event.directoryId, event.flashcard)
            is DirectoryEvent.CardDeleted -> deleteCard(event.directoryId)
        }.exhaustive
    }

    private fun getDirectoryContent(directoryId: Int) = viewModelScope.launch {
        val directoryContent = flashcardRepository.getFlashcardsForDirectory(directoryId)

        if (directoryContent.isEmpty()) {
            state.postValue(DirectoryState.NoContent)
        } else {
            state.postValue(DirectoryState.HasContent(directoryContent))
        }
    }


    private fun addCard(directoryId: Int, flashcard: Flashcard) = viewModelScope.launch {
        logsRepository.insertNotification(createNotification("Added new flashcard", "flashcard"))
        getDirectoryContent(directoryId)
    }

    private fun deleteCard(directoryId: Int) = viewModelScope.launch {
        logsRepository.insertNotification((createNotification("Deleted flashcard", "flashcard")))
        getDirectoryContent(directoryId)
    }

    private fun createNotification(title: String, type: String): Notification {
        return Notification(
            notificationId = 0,
            notificationTitle = title,
            notificationType = type,
            creationDate = OffsetDateTime.now().format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withZone(ZoneId.systemDefault())
            )
        )
    }
}
