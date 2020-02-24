package com.example.flashcards.ui.flashcard_review

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flashcards.R
import com.example.flashcards.ui.home.FlashcardState
import com.example.flashcards.ui.home.HomeViewModel

class FlashcardReviewScreen : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var reviewAdapter: FlashcardReviewAdapter
    private lateinit var closeButton: Button
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_review_layout)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        observe()

        setupViews()
    }

    private fun setupViews() {
        reviewAdapter = FlashcardReviewAdapter()

        viewPager = findViewById(R.id.reviewPager)
        viewPager.adapter = reviewAdapter

        closeButton = findViewById(R.id.closeReviewButton)
        closeButton.setOnClickListener { finish() }
    }

    private fun observe() {
        viewModel.state.observe(this, Observer { state ->

            when (state) {
                is FlashcardState.Error -> Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                is FlashcardState.Success -> {
                    reviewAdapter.submitList(state.flashcards)
                    reviewAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}
