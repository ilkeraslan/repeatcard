package com.repeatcard.app.ui.flashcardedit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.repeatcard.app.db.FlashcardDatabase
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.db.flashcard.FlashcardRepository
import kotlinx.coroutines.launch

sealed class FlashcardEditEvent {
    data class Edit(val id: Int) : FlashcardEditEvent()
}

sealed class FlashcardEditState {
    data class Success(val flashcard: Flashcard) : FlashcardEditState()
}

class EditFlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val flashcardRepository: FlashcardRepository
    var state: MutableLiveData<FlashcardEditState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        flashcardRepository = FlashcardRepository(flashcardsDao)
    }

    fun send(event: FlashcardEditEvent) {
        when (event) {
            is FlashcardEditEvent.Edit -> edit(event.id)
        }
    }

    private fun edit(id: Int) = viewModelScope.launch {
        val flashcardToEdit = flashcardRepository.getFlashcard(id)

        // Do editing


        state.postValue(FlashcardEditState.Success(flashcardToEdit))
    }
}
