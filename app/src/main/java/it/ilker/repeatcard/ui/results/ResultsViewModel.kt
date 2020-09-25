package it.ilker.repeatcard.ui.results

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.ilker.repeatcard.models.question.Question
import it.ilker.repeatcard.models.quizresult.QuizResult

sealed class ResultEvent {
    data class SendResults(val results: String) : ResultEvent()
}

sealed class ResultState {
    data class Success(val results: QuizResult) : ResultState()
}

class ResultsViewModel : ViewModel() {

    private val gson = Gson()
    private lateinit var result: QuizResult
    var state: MutableLiveData<ResultState> = MutableLiveData()

    fun send(event: ResultEvent) {
        when (event) {
            is ResultEvent.SendResults -> {
                val resultsToken = object : TypeToken<List<QuizResult>>() {}.type
                result = gson.fromJson(event.results, resultsToken)

                state.postValue(ResultState.Success(result))
            }
        }
    }
}
