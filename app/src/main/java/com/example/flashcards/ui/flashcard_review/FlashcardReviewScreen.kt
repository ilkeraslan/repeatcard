package com.example.flashcards.ui.flashcard_review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.R
import com.example.flashcards.ui.home.HomeViewModel


class FlashcardReviewScreen : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var reviewAdapter: FlashcardReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_review_layout)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        reviewAdapter = FlashcardReviewAdapter(viewModel.allFlashcards)
    }
}
