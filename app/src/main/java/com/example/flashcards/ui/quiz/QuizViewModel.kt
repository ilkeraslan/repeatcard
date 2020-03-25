package com.example.flashcards.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flashcards.db.FlashcardDatabase
import com.example.flashcards.db.flashcard.FlashcardRepository
import com.example.flashcards.models.Question
import com.example.flashcards.ui.util.exhaustive
import kotlinx.coroutines.launch

sealed class QuizEvent {
    object GetResults : QuizEvent()
    object Load : QuizEvent()
    data class SelectOption(val id: Int, val option: String) : QuizEvent()
}

sealed class QuizState {
    data class Error(val error: Throwable) : QuizState()
    data class Success(val questions: List<Question>) : QuizState()
    data class Results(val results: HashMap<Int, String?>) : QuizState()
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    private val questions = mutableListOf<Question>()
    private val selectedOptions = hashMapOf<Int, String?>()
    var state: MutableLiveData<QuizState> = MutableLiveData()

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(application).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
        loadContent()
    }

    fun send(event: QuizEvent) {
        when (event) {
            is QuizEvent.GetResults -> state.postValue(QuizState.Results(selectedOptions))
            is QuizEvent.Load -> loadContent()
            is QuizEvent.SelectOption -> selectOption(event.id, event.option)
        }.exhaustive
    }

    private fun loadContent() = viewModelScope.launch {
        val allFlashcards = repository.getFlashcards()

        // Create questions for each flashcard that has an image
        allFlashcards.forEach { flashcard ->
            if (!flashcard.imageUri.isNullOrEmpty()) {
                val question = Question(
                    id = flashcard.id,
                    imageUri = flashcard.imageUri,
                    correctAnswer = flashcard.description ?: "no description",
                    description = flashcard.description
                )
                question.option1 = flashcard.title
                question.option2 = "Foo"
                question.option3 = "Bar"
                question.option4 = "Baz"
                questions.add(question)
            }
        }

        // Post Success if exist 4 questions else Error
        state.postValue(if (questions.size >= 4) QuizState.Success(questions) else QuizState.Error(NullPointerException()))
    }

    private fun selectOption(id: Int, option: String) {
        selectedOptions[id] = option
    }
}
