package it.ilker.repeatcard.ui.flashcardedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

sealed class FlashcardEditEvent {
    data class GetCurrentValues(val id: Int) : FlashcardEditEvent()
    data class Edit(val id: Int, val title: String, val description: String?, val imageUri: String?) : FlashcardEditEvent()
}

sealed class FlashcardEditState {
    object Loading : FlashcardEditState()
    data class Success(val flashcard: Flashcard) : FlashcardEditState()
    object UpdateSuccess : FlashcardEditState()
}

@ExperimentalCoroutinesApi
class EditFlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val flashcardRepository: FlashcardRepository
    var state = MutableStateFlow<FlashcardEditState>(FlashcardEditState.Loading)

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        flashcardRepository = FlashcardRepository(flashcardsDao)
    }

    fun send(event: FlashcardEditEvent) {
        when (event) {
            is FlashcardEditEvent.GetCurrentValues -> getCurrentValues(event.id)
            is FlashcardEditEvent.Edit -> updateValues(event.id, event.title, event.description, event.imageUri)
        }.exhaustive
    }

    private fun getCurrentValues(id: Int) = viewModelScope.launch {
        val flashcardToEdit = flashcardRepository.getFlashcard(id)
        state.value = FlashcardEditState.Success(flashcardToEdit)
    }

    private fun updateValues(id: Int, title: String, description: String?, imageUri: String?) = viewModelScope.launch {
        val flashcardToUpdate = flashcardRepository.getFlashcard(id)
        val newFlashcard = Flashcard(
            id = id,
            title = title,
            description = description,
            imageUri = imageUri,
            directoryId = flashcardToUpdate.directoryId,
            creationDate = flashcardToUpdate.creationDate,
            lastModified = OffsetDateTime.now().format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withZone(ZoneId.systemDefault())
            )
        )

        flashcardRepository.updateFlashcard(newFlashcard)
        state.value = FlashcardEditState.UpdateSuccess
    }
}
