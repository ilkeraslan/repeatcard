package com.repeatcard.app.ui.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.repeatcard.app.R

class ResultsAfterQuiz : AppCompatActivity() {

    private lateinit var resultsQuestionNumber: TextView
    private lateinit var resultsViewModel: ResultsViewModel

    companion object {
        private const val BUNDLE_QUESTIONS_LIST = "BUNDLE_QUESTIONS_LIST"

        fun getIntent(context: Context, results: HashMap<Int, String?>, gson: Gson): Intent =
            Intent(context, ResultsAfterQuiz::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(BUNDLE_QUESTIONS_LIST, gson.toJson(results))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_results)

        resultsViewModel = ResultsViewModel(Gson())
        setViews()
        observe()

        val resultsAsJson = intent.extras!!.getString(BUNDLE_QUESTIONS_LIST)
        resultsViewModel.send(ResultEvent.SendResults(resultsAsJson!!))
    }

    private fun setViews() {
        resultsQuestionNumber = findViewById(R.id.results_question_number_text)
    }

    private fun observe() {
        resultsViewModel.state.observe(this, Observer { state ->
            when (state) {
                is ResultState.Success -> {
                    state.results.keys.forEach { key ->
                        Log.d("SELECTED AFTER CONVERSION: ".plus(key.toString()), state.results[key].toString())
                    }
                }
            }
        })
    }
}
