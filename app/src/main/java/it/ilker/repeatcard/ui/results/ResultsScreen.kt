package it.ilker.repeatcard.ui.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import it.ilker.repeatcard.R
import it.ilker.repeatcard.models.question.Question
import it.ilker.repeatcard.models.quizresult.QuizResult
import it.ilker.repeatcard.ui.question.QuestionDetailScreen
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import org.koin.android.ext.android.inject
import timber.log.Timber

class ResultsScreen : AppCompatActivity() {

    private val resultsViewModel: ResultsViewModel by inject()
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var resultListener: ResultListener

    companion object {
        private const val BUNDLE_QUESTIONS_LIST = "BUNDLE_QUESTIONS_LIST"

        fun getIntent(context: Context, result: QuizResult, gson: Gson): Intent =
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
                QuestionDetailScreen.openScreen(applicationContext, question, Gson())
            }
        }
        
        resultsAdapter = ResultsAdapter(resultListener)
        recyclerView.adapter = resultsAdapter
    }

    private fun observe() {
        resultsViewModel.state.observe(this, Observer { state ->
            when (state) {
                is ResultState.Success -> showSuccess(state.results)
            }
        })
    }

    private fun showSuccess(result: QuizResult) {
        resultsAdapter.submitList(result.questions)
        resultsAdapter.notifyDataSetChanged()
    }
}
