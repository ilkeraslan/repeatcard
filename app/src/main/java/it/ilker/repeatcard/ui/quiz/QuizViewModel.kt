package it.ilker.repeatcard.ui.quiz

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import me.ilker.business.answer.Answer
import me.ilker.business.question.Question
import me.ilker.business.quiz.QuestionGenerator
import me.ilker.business.quiz.QuizResult
import java.util.UUID

const val MIN_CARD_NUMBER_FOR_QUIZ = 4

sealed class QuizEvent {
    object Load : QuizEvent()
    data class SelectOption(
        val question: Question,
        val answer: Answer?
    ) : QuizEvent()
}

sealed class QuizState {
    object Loading : QuizState()
    data class Error(val error: Throwable) : QuizState()
    data class Success(
        val question: Question,
        val progress: Float
    ) : QuizState()
    data class Results(val result: QuizResult) : QuizState()
}

@ExperimentalCoroutinesApi
class QuizViewModel(context: Context) : ViewModel() {
    private val repository: FlashcardRepository
    private val generatedQuestions = mutableListOf<Question>()
    private var currentQuestionIndex = -1

    var state = MutableStateFlow<QuizState>(QuizState.Loading)

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
        loadContent()
    }

    fun send(event: QuizEvent) {
        when (event) {
            is QuizEvent.Load -> loadContent()
            is QuizEvent.SelectOption -> {
                selectOption(
                    question = event.question,
                    answer = event.answer
                )

                if (currentQuestionIndex < generatedQuestions.size - 1) {
                    next()
                } else {
                    finish()
                }
            }
        }.exhaustive
    }

    private fun finish() {
        state.value = QuizState.Results(
            QuizResult(
                id = UUID.randomUUID().toString(),
                questions = generatedQuestions,
                correctAnswers = generatedQuestions.filter { question ->
                    question.selectedAnswer == question.answer
                },
                wrongAnswers = generatedQuestions.filterNot { question ->
                    question.selectedAnswer == question.answer
                }
            )
        )
    }

    private fun loadContent() {
        viewModelScope.launch {
            val allFlashcards = repository.getFlashcards()
            val questions = mutableListOf<Question>()

            // Create questions for each flashcard that has an image
            allFlashcards.forEach { flashcard ->
                if (!flashcard.imageUri.isNullOrEmpty()) {
                    val question = Question(
                        id = flashcard.id,
                        imageUri = flashcard.imageUri!!,
                        answer = Answer(flashcard.title),
                        description = flashcard.description
                    )
                    questions.add(question)
                }
            }

            // Generate questions with random options
            generatedQuestions.clear()
            generatedQuestions.addAll(QuestionGenerator.generate(questions))

            if (generatedQuestions.size >= MIN_CARD_NUMBER_FOR_QUIZ) {
                next()
            } else {
                state.value = QuizState.Error(NullPointerException())
            }
        }
    }

    private fun next() {
        currentQuestionIndex++

        state.value = QuizState.Success(
            question = generatedQuestions[currentQuestionIndex],
            progress = currentQuestionIndex.toFloat() / (generatedQuestions.size - 1).toFloat()
        )
    }

    private fun selectOption(
        question: Question,
        answer: Answer?
    ) {
        question.selectedAnswer = answer
    }
}
