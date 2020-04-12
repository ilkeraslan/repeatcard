package com.repeatcard.app.ui.results

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.repeatcard.app.R
import com.repeatcard.app.models.question.Question

class ResultsAfterQuiz : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var resultsViewModel: ResultsViewModel

    companion object {
        private const val BUNDLE_QUESTIONS_LIST = "BUNDLE_QUESTIONS_LIST"

        fun getIntent(context: Context, results: List<Question>, gson: Gson): Intent =
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
        recyclerView = findViewById(R.id.recyclerView_results)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        resultsAdapter = ResultsAdapter()
        recyclerView.adapter = resultsAdapter
    }

    private fun observe() {
        resultsViewModel.state.observe(this, Observer { state ->
            when (state) {
                is ResultState.Success -> resultsAdapter.submitList(state.results)
            }
        })
    }
}
