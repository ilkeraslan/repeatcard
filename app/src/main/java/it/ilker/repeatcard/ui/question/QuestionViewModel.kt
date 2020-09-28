package it.ilker.repeatcard.ui.question

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.ilker.repeatcard.models.question.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed class QuestionEvent {
    data class GetDetails(val questionString: String) : QuestionEvent()
}

sealed class QuestionState {
    data class Success(val question: Question) : QuestionState()
}

class QuestionViewModel : ViewModel() {

    private lateinit var question: Question
    var state: MutableLiveData<QuestionState> = MutableLiveData()

    fun send(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.GetDetails -> {
                question = Json.decodeFromString(event.questionString)
                state.postValue(QuestionState.Success(question))
            }
        }
    }
}
