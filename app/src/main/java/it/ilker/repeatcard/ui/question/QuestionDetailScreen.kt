package it.ilker.repeatcard.ui.question

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import it.ilker.repeatcard.R
import it.ilker.repeatcard.models.question.Question
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject

class QuestionDetailScreen : AppCompatActivity() {

    private val questionViewModel: QuestionViewModel by inject()
    private lateinit var questionImage: ImageView

    companion object {
        private const val QUESTION = "QUESTION"

        fun openScreen(startingContext: Context, question: Question) {
            val intent = Intent(startingContext, QuestionDetailScreen::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(QUESTION, Json.encodeToString(Question.serializer(), question))

            startingContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_base)

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
