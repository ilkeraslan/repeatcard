package it.ilker.repeatcard.ui.directory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.directory.FlashcardDirectoryRepository
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.db.notification.NotificationRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.ilker.business.flashcard.Flashcard

sealed class DirectoryEvent {
    data class GetDirectoryContent(val directoryId: Int) : DirectoryEvent()
    data class CardDeleted(val directoryId: Int, val flashcard: Flashcard) : DirectoryEvent()
    data class CardAdded(val directoryId: Int, val flashcard: Flashcard) : DirectoryEvent()
}

sealed class DirectoryState {
    object Loading : DirectoryState()
    data class HasContent(val flashcards: List<Flashcard>) : DirectoryState()
    object NoContent : DirectoryState()
}

@ExperimentalCoroutinesApi
class DirectoryViewModel(context: Context) : ViewModel() {

    private val directoryRepository: FlashcardDirectoryRepository
    private val flashcardRepository: FlashcardRepository
    private val logsRepository: NotificationRepository
    var state = MutableStateFlow<DirectoryState>(DirectoryState.Loading)

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
            state.value = DirectoryState.NoContent
        } else {
            state.value = DirectoryState.HasContent(directoryContent)
        }
    }

    private fun addCard(directoryId: Int, flashcard: Flashcard) = viewModelScope.launch {
        flashcardRepository.insert(flashcard)
        getDirectoryContent(directoryId)
    }

    private fun deleteCard(directoryId: Int, flashcard: Flashcard) = viewModelScope.launch {
        flashcardRepository.deleteFlashcard(flashcard.id)
        getDirectoryContent(directoryId)
    }
}
