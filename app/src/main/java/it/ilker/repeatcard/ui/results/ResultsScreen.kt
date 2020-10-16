package it.ilker.repeatcard.ui.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.ilker.repeatcard.R
import it.ilker.repeatcard.models.question.Question
import it.ilker.repeatcard.models.quizresult.QuizResult
import it.ilker.repeatcard.ui.question.QuestionDetailScreen
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class ResultsScreen : AppCompatActivity() {

    private val resultsViewModel: ResultsViewModel by inject()
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var resultListener: ResultListener

    companion object {
        private const val BUNDLE_QUESTIONS_LIST = "BUNDLE_QUESTIONS_LIST"

        fun getIntent(context: Context, result: QuizResult): Intent =
            Intent(context, ResultsScreen::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(BUNDLE_QUESTIONS_LIST, Json.encodeToString(QuizResult.serializer(), result))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_results)
        observe()
        val resultsAsJson = intent.extras!!.getString(BUNDLE_QUESTIONS_LIST)
        resultsViewModel.send(ResultEvent.SendResults(resultsAsJson!!))
        setViews()
    }

    private fun setViews() {
        recyclerView = findViewById(R.id.recyclerView_results)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        resultListener = object : ResultListener {
            override fun showResultDetails(question: Question) {
                QuestionDetailScreen.openScreen(applicationContext, question)
            }
        }

        resultsAdapter = ResultsAdapter(resultListener)
        recyclerView.adapter = resultsAdapter
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            resultsViewModel.state.collect { state ->
                when (state) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> showSuccess(state.results)
                }.exhaustive
            }
        }
    }

    private fun showSuccess(result: QuizResult) {
        resultsAdapter.submitList(result.questions)
        resultsAdapter.notifyDataSetChanged()
    }
}
