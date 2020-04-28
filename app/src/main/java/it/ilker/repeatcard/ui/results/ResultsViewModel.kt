package it.ilker.repeatcard.ui.results

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.ilker.repeatcard.models.question.Question

sealed class ResultEvent {
    data class SendResults(val results: String) : ResultEvent()
}

sealed class ResultState {
    data class Success(val results: List<Question>) : ResultState()
}

class ResultsViewModel : ViewModel() {

    private val gson = Gson()
    private lateinit var results: List<Question>
    var state: MutableLiveData<ResultState> = MutableLiveData()

    fun send(event: ResultEvent) {
        when (event) {
            is ResultEvent.SendResults -> {
                val resultsToken = object : TypeToken<List<Question>>() {}.type
                results = gson.fromJson(event.results, resultsToken)

                state.postValue(ResultState.Success(results))
            }
        }
    }
}
