package com.example.flashcards.ui.quiz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.flashcards.R

class QuizFragment : Fragment() {

    private lateinit var startQuiz: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.quiz_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
    }

    private fun setUpViews() {
        startQuiz = requireActivity().findViewById(R.id.startQuiz)
        startQuiz.setOnClickListener {
            val intent = Intent(activity, QuizScreen::class.java)
            startActivity(intent)
        }
    }
}
