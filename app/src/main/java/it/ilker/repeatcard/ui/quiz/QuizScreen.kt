package it.ilker.repeatcard.ui.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.results.ResultsScreen
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class QuizScreen : AppCompatActivity() {

    private val viewModel: QuizViewModel by inject()

    private lateinit var adapter: QuizAdapter
    private lateinit var closeButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var viewPager: ViewPager2
    private var lastSelectedOption: TextView? = null
    private var hasSelectedAnOption = false

    companion object {
        fun openScreen(context: Context) {
            val intent = Intent(context, QuizScreen::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        observe()
        setupViews()
        setupClickListeners()
        setupOnPageChangeListener()
    }

    private fun setupViews() {
        adapter = QuizAdapter(object : QuizListener {
            override fun itemSelected(selectedView: TextView, otherViews: List<TextView>) {
                selectedView.setBackgroundColor(resources.getColor(R.color.colorYellow))
                otherViews.forEach { unselectedView -> unselectedView.setBackgroundColor(0) }
                lastSelectedOption = selectedView
                hasSelectedAnOption = true
            }
        })
        closeButton = findViewById(R.id.closeQuizButton)
        nextButton = findViewById(R.id.nextButtonQuiz)
        previousButton = findViewById(R.id.previousButtonQuiz)
        viewPager = findViewById(R.id.quizPager)
        viewPager.adapter = adapter
    }

    private fun setupClickListeners() {
        closeButton.setOnClickListener { finish() }
        previousButton.setOnClickListener {
            if (viewPager.currentItem < 0) finish() else viewPager.currentItem--
        }
        nextButton.setOnClickListener {
            val question = adapter.currentList[viewPager.currentItem]

            if (hasSelectedAnOption) {
                viewModel.send(QuizEvent.SelectOption(question, lastSelectedOption?.text.toString()))
                hasSelectedAnOption = false
            } else {
                viewModel.send(QuizEvent.SelectOption(question, null))
            }

            if (viewPager.currentItem == adapter.itemCount - 1) {
                viewModel.send(QuizEvent.GetResults)
                observe()
            } else {
                viewPager.currentItem++
            }
        }
    }

    private fun setupOnPageChangeListener() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (viewPager.currentItem == 0) {
                    previousButton.visibility = INVISIBLE
                } else {
                    previousButton.visibility = VISIBLE
                }
            }
        })
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                when (state) {
                    is QuizState.Loading -> {}
                    is QuizState.Error -> {
                        Toast.makeText(this@QuizScreen, "No question available.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is QuizState.Success -> {
                        adapter.submitList(state.questions)
                        adapter.notifyDataSetChanged()
                    }
                    is QuizState.Results -> {
                        startActivity(ResultsScreen.getIntent(applicationContext, state.result))
                        finish()
                    }
                }.exhaustive
            }
        }
    }
}
