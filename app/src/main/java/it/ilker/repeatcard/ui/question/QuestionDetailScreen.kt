package it.ilker.repeatcard.ui.question

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import it.ilker.repeatcard.R
import it.ilker.repeatcard.models.question.Question
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.android.synthetic.main.activity_quiz_base.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
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
        lifecycleScope.launchWhenStarted {
            questionViewModel.state.collect { state ->
                when (state) {
                    is QuestionState.Loading -> showLoader()
                    is QuestionState.Success -> {
                        progress_circular.visibility = View.GONE
                        content_group.visibility = View.VISIBLE
                        Glide.with(this@QuestionDetailScreen)
                            .load(state.question.imageUri)
                            .into(questionImage)
                    }
                }.exhaustive
            }
        }
    }

    private fun showLoader() {
        progress_circular.visibility = View.VISIBLE
        content_group.visibility = View.INVISIBLE
    }
}
