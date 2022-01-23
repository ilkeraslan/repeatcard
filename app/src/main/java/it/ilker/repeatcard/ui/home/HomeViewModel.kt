package it.ilker.repeatcard.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.ilker.business.flashcard.Flashcard

sealed class FlashcardEvent {
    object Load : FlashcardEvent()
}

sealed class FlashcardState {
    object Error : FlashcardState()
    data class Success(val flashcards: List<Flashcard>) : FlashcardState()
    object Loading : FlashcardState()
}

@ExperimentalCoroutinesApi
class HomeViewModel(context: Context) : ViewModel() {
    private val repository: FlashcardRepository

    private var _state = MutableStateFlow<FlashcardState>(FlashcardState.Loading)
    val state: StateFlow<FlashcardState>
        get() = _state

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
        loadContent()
    }

    fun send(event: FlashcardEvent) {
        when (event) {
            is FlashcardEvent.Load -> loadContent()
        }.exhaustive
    }

    fun deleteCard(flashcard: Flashcard) {
        _state.value = FlashcardState.Loading

        viewModelScope.launch {
            repository.deleteFlashcard(flashcard.id)
            loadContent()
        }
    }

    private fun loadContent() {
        _state.value = FlashcardState.Loading

        viewModelScope.launch {
            val flashcards = repository.getFlashcards()
            _state.value = if (flashcards.isEmpty()) {
                FlashcardState.Success(emptyList())
            } else {
                FlashcardState.Success(flashcards)
            }
        }
    }
}
