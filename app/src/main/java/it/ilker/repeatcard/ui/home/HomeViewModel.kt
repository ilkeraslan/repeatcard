package it.ilker.repeatcard.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class FlashcardEvent {
    object Load : FlashcardEvent()
}

sealed class FlashcardState {
    object Error : FlashcardState()
    data class Success(val flashcards: List<Flashcard>) : FlashcardState()
    object Initial : FlashcardState()
}

@ExperimentalCoroutinesApi
class HomeViewModel(context: Context) : ViewModel() {

    private val repository: FlashcardRepository
    var state = MutableStateFlow<FlashcardState>(FlashcardState.Initial)

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
    }

    fun send(event: FlashcardEvent) {
        when (event) {
            is FlashcardEvent.Load -> loadContent()
        }.exhaustive
    }

    private fun loadContent() = viewModelScope.launch {
        val allFlashcards = repository.getFlashcards()
        if (allFlashcards.isEmpty()) {
            state.value = FlashcardState.Error
        } else {
            state.value = FlashcardState.Success(allFlashcards)
        }
    }
}
