package com.example.flashcards.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.flashcard.FlashcardRepository
import kotlinx.coroutines.launch

sealed class QuizEvent {
    object Load : QuizEvent()
}

sealed class QuizState {
    data class Error(val error: Throwable) : QuizState()
    data class Success(val questions: List<Flashcard>) : QuizState()
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    var state: MutableLiveData<QuizState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
        loadContent()
    }

    fun send(event: QuizEvent) {
        when (event) {
            is QuizEvent.Load -> loadContent()
        }
    }

    private fun loadContent() = viewModelScope.launch {
        val allFlashcards = repository.getFlashcards()
        // TODO -> Create question set here
        state.postValue(QuizState.Success(allFlashcards))
    }
}
