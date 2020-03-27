package com.example.flashcards.ui.flashcard_review

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.flashcards.R
import com.example.flashcards.ui.home.FlashcardState
import com.example.flashcards.ui.home.HomeViewModel
import com.example.flashcards.ui.util.exhaustive

class FlashcardReviewScreen : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var reviewAdapter: FlashcardReviewAdapter
    private lateinit var closeButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_review_layout)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        observe()
        setupViews()
        setupClickListeners()
        setupOnPageChangeListener()
    }

    private fun observe() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is FlashcardState.Error -> Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                is FlashcardState.Success -> {
                    reviewAdapter.submitList(state.flashcards)
                    reviewAdapter.notifyDataSetChanged()
                }
            }.exhaustive
        })
    }

    private fun setupViews() {
        reviewAdapter = FlashcardReviewAdapter()

        viewPager = findViewById(R.id.reviewPager)
        viewPager.adapter = reviewAdapter

        closeButton = findViewById(R.id.closeReviewButton)
        nextButton = findViewById(R.id.nextReviewButton)
        previousButton = findViewById(R.id.previousReviewButton)
    }

    private fun setupClickListeners() {
        closeButton.setOnClickListener { finish() }
        previousButton.setOnClickListener {
            if (viewPager.currentItem < 0) finish() else viewPager.currentItem--
        }
        nextButton.setOnClickListener {
            if (viewPager.currentItem == reviewAdapter.itemCount - 1) finish() else viewPager.currentItem++
        }
    }

    private fun setupOnPageChangeListener() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (viewPager.currentItem == 0) {
                    previousButton.visibility = View.INVISIBLE
                    nextButton.visibility = View.VISIBLE
                } else {
                    previousButton.visibility = View.VISIBLE
                    nextButton.visibility = View.VISIBLE
                }
            }
        })
    }

}
