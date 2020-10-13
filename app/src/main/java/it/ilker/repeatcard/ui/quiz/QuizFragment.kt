package it.ilker.repeatcard.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.AppNavigator
import it.ilker.repeatcard.ui.util.exhaustive
import org.koin.android.ext.android.inject
import timber.log.Timber

class QuizFragment : Fragment() {

    private val viewModel: QuizViewModel by inject()
    private val navigator: AppNavigator by inject()

    private lateinit var startQuiz: Button
    private lateinit var feedbackText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.quiz_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        setUpViews(view)
        viewModel.send(QuizEvent.Load)
    }

    override fun onResume() {
        super.onResume()
        observe()
    }

    private fun setUpViews(view: View) {
        startQuiz = view.findViewById(R.id.startQuiz)
        feedbackText = view.findViewById(R.id.quizFeedbackText)
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is QuizState.Error -> showError()
                is QuizState.Success -> {
                    feedbackText.visibility = INVISIBLE
                    startQuiz.setOnClickListener { navigator.goToQuiz() }
                }
                is QuizState.Results -> Timber.d("Results")
            }.exhaustive
        })
    }

    private fun showError() {
        feedbackText.visibility = VISIBLE
    }
}
