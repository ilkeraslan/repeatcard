package com.example.flashcards.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.R
import com.example.flashcards.ui.util.exhaustive

class QuizFragment : Fragment() {

    private lateinit var startQuiz: Button
    private lateinit var viewModel: QuizViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.quiz_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        setUpViews()
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
                    startQuiz.setOnClickListener {
                        val intent = Intent(activity, QuizScreen::class.java)
                        startActivity(intent)
                    }
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
