package it.ilker.repeatcard.ui.results

import androidx.lifecycle.ViewModel
import it.ilker.repeatcard.models.quizresult.QuizResult
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

sealed class ResultEvent {
    data class SendResults(val results: String) : ResultEvent()
}

sealed class ResultState {
    object Initial : ResultState()
    data class Success(val results: QuizResult) : ResultState()
}

@ExperimentalCoroutinesApi
class ResultsViewModel : ViewModel() {

    private lateinit var result: QuizResult
    var state = MutableStateFlow<ResultState>(ResultState.Initial)

    fun send(event: ResultEvent) {
        when (event) {
            is ResultEvent.SendResults -> {
                result = Json.decodeFromString(event.results)
                state.value = ResultState.Success(result)
            }
        }.exhaustive
    }
}
