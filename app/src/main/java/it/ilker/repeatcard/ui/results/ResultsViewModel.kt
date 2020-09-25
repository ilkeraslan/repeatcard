package it.ilker.repeatcard.ui.results

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import it.ilker.repeatcard.models.quizresult.QuizResult
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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
/*                val resultsToken = object : TypeToken<List<QuizResult>>() {}.type
                result = gson.fromJson(event.results, resultsToken)*/

                result = Json.decodeFromString(event.results)
                state.postValue(ResultState.Success(result))
            }
        }
    }
}
