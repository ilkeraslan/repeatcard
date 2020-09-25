package it.ilker.repeatcard.ui.quiz

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.models.question.Question
import it.ilker.repeatcard.models.quizresult.QuizResult
import it.ilker.repeatcard.ui.util.exhaustive
import java.util.UUID
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
    data class Results(val result: QuizResult) : QuizState()
}

class QuizViewModel(context: Context) : ViewModel() {

    private val repository: FlashcardRepository
    private val generatedQuestions = mutableListOf<Question>()
    var state: MutableLiveData<QuizState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(context).flashcardDao()
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
        state.postValue(QuizState.Results(
            QuizResult(
                id = UUID.randomUUID().toString(),
                questions = generatedQuestions,
                correctAnswers = generatedQuestions.filter { question -> question.selectedAnswer == question.correctAnswer },
                wrongAnswers = generatedQuestions.filterNot { question -> question.selectedAnswer == question.correctAnswer }
            )))
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
            if (generatedQuestions.size >= MIN_CARD_NUMBER_FOR_QUIZ) {
                QuizState.Success(generatedQuestions)
            } else {
                QuizState.Error(NullPointerException())
            }
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
