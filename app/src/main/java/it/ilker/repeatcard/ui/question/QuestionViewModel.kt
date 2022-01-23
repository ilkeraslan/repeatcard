package it.ilker.repeatcard.ui.question

import androidx.lifecycle.ViewModel
import me.ilker.business.question.Question
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed class QuestionEvent {
    data class GetDetails(val questionString: String) : QuestionEvent()
}

sealed class QuestionState {
    object Loading : QuestionState()
    data class Success(val question: Question) : QuestionState()
}

@ExperimentalCoroutinesApi
class QuestionViewModel : ViewModel() {

    private lateinit var question: Question
    var state = MutableStateFlow<QuestionState>(QuestionState.Loading)

    fun send(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.GetDetails -> {
                question = Json.decodeFromString(event.questionString)
                state.value = QuestionState.Success(question)
            }
        }
    }
}
