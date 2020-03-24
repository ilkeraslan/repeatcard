package com.example.flashcards.ui.quiz

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.flashcards.R

class QuizScreen : AppCompatActivity() {

    private lateinit var adapter: QuizAdapter
    private lateinit var closeButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        setupViews()
    }

    private fun setupViews() {
        adapter = QuizAdapter()
        closeButton = findViewById(R.id.closeQuizButton)
        nextButton = findViewById(R.id.nextButtonQuiz)
        previousButton = findViewById(R.id.previousButtonQuiz)
        viewPager = findViewById(R.id.quizPager)

        viewPager.adapter = adapter

        closeButton.setOnClickListener { finish() }
    }

}
