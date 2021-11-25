package it.ilker.repeatcard.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.AppNavigator
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.android.synthetic.main.quiz_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import timber.log.Timber

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is QuizState.Loading -> showLoader()
                    is QuizState.Error -> showError()
                    is QuizState.Success -> {
                        progress_circular.visibility = GONE
                        startQuiz.visibility = VISIBLE
                        feedbackText.visibility = INVISIBLE
                        startQuiz.setOnClickListener { navigator.goToQuiz() }
                    }
                    is QuizState.Results -> Timber.d("Results")
                }.exhaustive
            }
        }
    }

    private fun showLoader() {
        progress_circular.visibility = VISIBLE
        feedbackText.visibility = INVISIBLE
        startQuiz.visibility = INVISIBLE
    }

    private fun showError() {
        progress_circular.visibility = GONE
        feedbackText.visibility = VISIBLE
    }
}
