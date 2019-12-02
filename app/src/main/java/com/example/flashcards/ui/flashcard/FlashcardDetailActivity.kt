package com.example.flashcards.ui.flashcard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.flashcards.R

private const val BUNDLE_TAG_FLASHCARD_ID: String = "BUNDLE_TAG_FLASHCARD_ID"

class FlashcardDetailActivity : AppCompatActivity() {

    companion object {
        fun openFlashcardDetailActivity(startingActivity: Activity, flashcardId: String) {
            val intent = Intent(startingActivity, FlashcardDetailActivity::class.java)
                .putExtra(BUNDLE_TAG_FLASHCARD_ID, flashcardId)

            startingActivity.startActivity(intent)
        }
    }

    private lateinit var viewModel: FlashcardDetailViewModel
    private lateinit var closeButton: Button
    private lateinit var flashcard_id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_detail_layout)

        setUpViews()
    }

    private fun setUpViews() {
        viewModel = ViewModelProviders.of(this).get(FlashcardDetailViewModel::class.java)
        closeButton = findViewById(R.id.button_close_detail)
        flashcard_id = intent.extras?.get(BUNDLE_TAG_FLASHCARD_ID).toString()

        viewModel.send(
            FlashcardDetailEvent.Load, flashcard_id
        )

        closeButton.setOnClickListener {
            finish()
        }
    }
}