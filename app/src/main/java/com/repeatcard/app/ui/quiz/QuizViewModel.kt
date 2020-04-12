package com.repeatcard.app.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.repeatcard.app.db.FlashcardDatabase
import com.repeatcard.app.db.flashcard.FlashcardRepository
import com.repeatcard.app.models.question.Question
import com.repeatcard.app.models.question.QuestionGenerator
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.launch

const val MIN_CARD_NUMBER_FOR_QUIZ = 4

sealed class QuizEvent {
    object GetResults : QuizEvent()
    object Load : QuizEvent()
    data class SelectOption(val question: Question, val option: String?) : QuizEvent()
}

sealed class QuizState {
    data class Error(val error: Throwable) : QuizState()
    data class Success(val questions: List<Question>) : QuizState()
    data class Results(val results: List<Question>) : QuizState()
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    private val generatedQuestions = mutableListOf<Question>()
    var state: MutableLiveData<QuizState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
        loadContent()
    }

    fun send(event: QuizEvent) {
        when (event) {
            is QuizEvent.GetResults -> getResults()
            is QuizEvent.Load -> loadContent()
            is QuizEvent.SelectOption -> selectOption(event.question, event.option)
        }.exhaustive
    }

    private fun getResults() {
        state.postValue(QuizState.Results(generatedQuestions))
    }

    private fun loadContent() = viewModelScope.launch {
        val allFlashcards = repository.getFlashcards()
        val questions = mutableListOf<Question>()

        // Create questions for each flashcard that has an image
        allFlashcards.forEach { flashcard ->
            if (!flashcard.imageUri.isNullOrEmpty()) {
                val question = Question(
                    id = flashcard.id,
                    imageUri = flashcard.imageUri,
                    correctAnswer = flashcard.title,
                    description = flashcard.description
                )
                questions.add(question)
            }
        }

        // Generate questions with random options
        generatedQuestions.clear()
        generatedQuestions.addAll(QuestionGenerator.generate(questions))

        // Post Success if exist MIN_CARD_NUMBER_FOR_QUIZ else Error
        state.postValue(
            if (generatedQuestions.size >= MIN_CARD_NUMBER_FOR_QUIZ) QuizState.Success(generatedQuestions)
            else QuizState.Error(NullPointerException())
        )
    }

    private fun selectOption(question: Question, option: String?) {
        if (option.isNullOrEmpty()) {
            question.selectedAnswer = null
        } else {
            question.selectedAnswer = option
        }
    }
}
