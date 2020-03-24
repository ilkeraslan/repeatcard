package com.example.flashcards.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.flashcard.FlashcardRepository
import com.example.flashcards.models.Question
import kotlinx.coroutines.launch

sealed class QuizEvent {
    object Load : QuizEvent()
}

sealed class QuizState {
    data class Error(val error: Throwable) : QuizState()
    data class Success(val questions: List<Question>) : QuizState()
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    private val questions = mutableListOf<Question>()
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

        // Create questions for each flashcard that has an image
        allFlashcards.forEach { flashcard ->
            if (!flashcard.imageUri.isNullOrEmpty()) {
                val question = Question(
                    id = flashcard.id,
                    imageUri = flashcard.imageUri,
                    questionText = flashcard.title,
                    correctAnswer = flashcard.description ?: "no description"
                )
                question.option1 = flashcard.description ?: "no description"
                question.option2 = "Foo"
                question.option3 = "Bar"
                question.option4 = "Baz"
                questions.add(question)
            }
        }

        state.postValue(if (questions.isNotEmpty()) QuizState.Success(questions) else QuizState.Error(NullPointerException()))
    }
}
