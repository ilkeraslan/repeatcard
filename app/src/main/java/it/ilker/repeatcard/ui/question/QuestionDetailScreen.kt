package it.ilker.repeatcard.ui.question

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import it.ilker.repeatcard.R
import it.ilker.repeatcard.models.question.Question

class QuestionDetailScreen : AppCompatActivity() {

    private lateinit var questionViewModel: QuestionViewModel
    private lateinit var questionImage: ImageView

    companion object {
        private const val QUESTION = "QUESTION"

        fun openScreen(startingContext: Context, question: Question, gson: Gson) {
            val intent = Intent(startingContext, QuestionDetailScreen::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(QUESTION, gson.toJson(question))

            startingContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_base)

        questionViewModel = QuestionViewModel(Gson())
        setViews()
        observe()

        val resultsAsJson = intent.extras!!.getString(QUESTION)
        questionViewModel.send(QuestionEvent.GetDetails(resultsAsJson!!))
    }

    private fun setViews() {
        questionImage = findViewById(R.id.quizImageView)
        questionImage.setImageResource(R.drawable.photography)
    }

    private fun observe() {
        questionViewModel.state.observe(this, Observer { state ->
            when (state) {
                is QuestionState.Success -> {
                    Glide.with(this)
                        .load(state.question.imageUri)
                        .into(questionImage)
                }
            }
        })
    }
}
