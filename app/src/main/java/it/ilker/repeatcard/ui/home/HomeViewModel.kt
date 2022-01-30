package it.ilker.repeatcard.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.ilker.business.flashcard.Flashcard

sealed class FlashcardState {
    object Error : FlashcardState()
    data class Success(val flashcards: List<Flashcard>) : FlashcardState()
    object Loading : FlashcardState()
}

@ExperimentalCoroutinesApi
class HomeViewModel(context: Context) : ViewModel() {
    private val flashcardsDao = FlashcardDatabase.getDatabase(context).flashcardDao()
    private val repository = FlashcardRepository(flashcardsDao)

    private var _state = MutableStateFlow<FlashcardState>(FlashcardState.Loading)
    val state: StateFlow<FlashcardState>
        get() = _state

    init {
        loadContent()
    }

    fun deleteCard(flashcard: Flashcard) {
        _state.value = FlashcardState.Loading

        viewModelScope.launch {
            repository.deleteFlashcard(flashcard.id)
            loadContent()
        }
    }

    fun loadContent() {
        _state.value = FlashcardState.Loading

        viewModelScope.launch {
            _state.value = FlashcardState.Success(repository.getFlashcards())
        }
    }
}
