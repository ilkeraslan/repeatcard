package it.ilker.repeatcard.ui.question

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.ilker.repeatcard.models.question.Question

sealed class QuestionEvent {
    data class GetDetails(val questionString: String) : QuestionEvent()
}

sealed class QuestionState {
    data class Success(val question: Question) : QuestionState()
}

class QuestionViewModel(private val gson: Gson) : ViewModel() {

    private lateinit var question: Question
    var state: MutableLiveData<QuestionState> = MutableLiveData()

    fun send(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.GetDetails -> {
                val resultsToken = object : TypeToken<Question>() {}.type
                question = gson.fromJson(event.questionString, resultsToken)

                state.postValue(QuestionState.Success(question))
            }
        }
    }
}
