package com.example.flashcards.ui.flashcard_detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard

private const val BUNDLE_TAG_FLASHCARD_ID: String = "BUNDLE_TAG_FLASHCARD_ID"

class FlashcardDetailActivity : AppCompatActivity() {

    companion object {
        fun openFlashcardDetailActivity(startingActivity: Activity, flashcardId: Int) {
            val intent = Intent(startingActivity, FlashcardDetailActivity::class.java)
                .putExtra(BUNDLE_TAG_FLASHCARD_ID, flashcardId)

            startingActivity.startActivity(intent)
        }
    }

    private lateinit var viewModel: FlashcardDetailViewModel
    private lateinit var closeButton: Button
    private lateinit var detailTextView: TextView

    private var flashcard_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_detail_layout)

        setUpViews()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is FlashcardDetailState.Error -> showError(state.error)
                is FlashcardDetailState.Success -> showFlashcard(state.flashcard)
            }
        })
    }

    private fun showFlashcard(flashcard: Flashcard) {
        detailTextView.text = flashcard.title
    }

    private fun showError(error: Throwable) {
        Log.i("SHOW_ERROR", "Error: ", error)
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
    }

    private fun setUpViews() {
        viewModel = ViewModelProvider(this).get(FlashcardDetailViewModel::class.java)
        closeButton = findViewById(R.id.button_close_detail)
        detailTextView = findViewById(R.id.flashcard_detail_textView)
        flashcard_id = intent.extras!!.getInt("BUNDLE_TAG_FLASHCARD_ID")

        viewModel.send(
            FlashcardDetailEvent.Load, flashcard_id
        )

        closeButton.setOnClickListener {
            finish()
        }
    }
}