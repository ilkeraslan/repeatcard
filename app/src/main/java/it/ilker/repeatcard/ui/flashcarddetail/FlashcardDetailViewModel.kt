package it.ilker.repeatcard.ui.flashcarddetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.launch

sealed class FlashcardDetailEvent {
    object Load : FlashcardDetailEvent()
}

sealed class FlashcardDetailState {
    data class Error(val error: Throwable) : FlashcardDetailState()
    data class Success(val flashcard: Flashcard) : FlashcardDetailState()
}

class FlashcardDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    var state: MutableLiveData<FlashcardDetailState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
    }

    fun send(event: FlashcardDetailEvent, id: Int) {
        when (event) {
            is FlashcardDetailEvent.Load -> loadContent(id)
        }.exhaustive
    }

    private fun loadContent(flashcardId: Int) = viewModelScope.launch {
        state.postValue(FlashcardDetailState.Success(repository.getFlashcard(flashcardId)))
    }
}
