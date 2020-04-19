package it.ilker.repeatcard.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.repeatcard.app.ui.AppNavigator
import com.repeatcard.app.ui.quiz.QuizEvent
import com.repeatcard.app.ui.quiz.QuizState
import com.repeatcard.app.ui.quiz.QuizViewModel
import com.repeatcard.app.ui.util.exhaustive
import it.ilker.repeatcard.R
import org.koin.android.ext.android.inject

class QuizFragment : Fragment() {

    private val viewModel: QuizViewModel by inject()
    private val navigator: AppNavigator by inject()

    private lateinit var startQuiz: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.quiz_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.send(QuizEvent.Load)
        setUpViews()
        observe()
    }

    override fun onResume() {
        super.onResume()
        observe()
    }

    private fun setUpViews() {
        startQuiz = requireActivity().findViewById(R.id.startQuiz)
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is QuizState.Error -> {
                    startQuiz.setOnClickListener {
                        Toast.makeText(this.context, "You should have 4 Flashcards with a title-description-image.", Toast.LENGTH_SHORT).show()
                    }
                }
                is QuizState.Success -> {
                    startQuiz.setOnClickListener { navigator.goToQuiz() }
                }
                is QuizState.Results -> {
                    startQuiz.setOnClickListener {
                        Toast.makeText(this.context, "Results", Toast.LENGTH_SHORT).show()
                    }
                }
            }.exhaustive
        })
    }
}
