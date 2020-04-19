package it.ilker.repeatcard.ui.directory

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.directory.FlashcardDirectoryRepository
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.db.notification.Notification
import it.ilker.repeatcard.db.notification.NotificationRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

sealed class DirectoryEvent {
    data class GetDirectoryContent(val directoryId: Int) : DirectoryEvent()
    data class CardDeleted(val directoryId: Int, val flashcard: Flashcard) : DirectoryEvent()
    data class CardAdded(val directoryId: Int, val flashcard: Flashcard) : DirectoryEvent()
}

sealed class DirectoryState {
    data class HasContent(val flashcards: List<Flashcard>) : DirectoryState()
    data class NoContent(val flashcards: List<Flashcard>) : DirectoryState()
}

class DirectoryViewModel(context: Context) : ViewModel() {

    private val directoryRepository: FlashcardDirectoryRepository
    private val flashcardRepository: FlashcardRepository
    private val logsRepository: NotificationRepository
    var state: MutableLiveData<DirectoryState> = MutableLiveData()

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(context).directoryDao()
        val flashcardDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        val logsDao = FlashcardDatabase.getDatabase(context).notificationsDao()
        directoryRepository = FlashcardDirectoryRepository(directoriesDao)
        flashcardRepository = FlashcardRepository(flashcardDao)
        logsRepository = NotificationRepository(logsDao)
    }

    fun send(event: DirectoryEvent) {
        when (event) {
            is DirectoryEvent.GetDirectoryContent -> getDirectoryContent(event.directoryId)
            is DirectoryEvent.CardAdded -> addCard(event.directoryId, event.flashcard)
            is DirectoryEvent.CardDeleted -> deleteCard(event.directoryId, event.flashcard)
        }.exhaustive
    }

    private fun getDirectoryContent(directoryId: Int) = viewModelScope.launch {
        val directoryContent = flashcardRepository.getFlashcardsForDirectory(directoryId)

        if (directoryContent.isEmpty()) {
            state.postValue(DirectoryState.NoContent(listOf()))
        } else {
            state.postValue(DirectoryState.HasContent(directoryContent))
        }
    }

    private fun addCard(directoryId: Int, flashcard: Flashcard) = viewModelScope.launch {
        flashcardRepository.insert(flashcard)
        logsRepository.insertNotification(createNotification("Added new flashcard"))
        getDirectoryContent(directoryId)
    }

    private fun deleteCard(directoryId: Int, flashcard: Flashcard) = viewModelScope.launch {
        flashcardRepository.deleteFlashcard(flashcard.id)
        logsRepository.insertNotification(createNotification("Deleted flashcard"))
        getDirectoryContent(directoryId)
    }

    private fun createNotification(title: String): Notification {
        return Notification(
            notificationId = 0,
            notificationTitle = title,
            notificationType = "flashcard",
            creationDate = OffsetDateTime.now().format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withZone(ZoneId.systemDefault())
            )
        )
    }
}
