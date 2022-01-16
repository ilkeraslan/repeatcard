package it.ilker.repeatcard.ui.addcard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.ilker.business.flashcard.Flashcard

sealed class AddCardEvent {
    data class Add(val title: String) : AddCardEvent()
}

sealed class AddCardState {
    object Error : AddCardState()
    data class Success(val flashcard: Flashcard) : AddCardState()
    object Loading : AddCardState()
    object Initial : AddCardState()
}

@ExperimentalCoroutinesApi
class AddCardViewModel(context: Context) : ViewModel() {

    private val repository: FlashcardRepository

    private var _state = MutableStateFlow<AddCardState>(AddCardState.Initial)
    val state: StateFlow<AddCardState>
        get() = _state

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
    }

    fun send(event: AddCardEvent) {
        when (event) {
            is AddCardEvent.Add -> addCard(event.title)
        }.exhaustive
    }

    private fun addCard(title: String) {
        viewModelScope.launch {
            val card = Flashcard(
                id = Random.nextInt(),
                title = title
            )

            val addedCardId = repository.insert(card)
            val addedCard = repository.getFlashcard(addedCardId.toInt())

            _state.value = AddCardState.Success(addedCard)
        }
    }
}
