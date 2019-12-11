package com.example.flashcards.ui.flashcard_detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.R

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

    private var flashcard_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_detail_layout)

        setUpViews()
    }

    private fun setUpViews() {
        viewModel = ViewModelProvider(this).get(FlashcardDetailViewModel::class.java)
        closeButton = findViewById(R.id.button_close_detail)
        flashcard_id = intent.extras!!.getInt("BUNDLE_TAG_FLASHCARD_ID")

        viewModel.send(
            FlashcardDetailEvent.Load, flashcard_id
        )

        closeButton.setOnClickListener {
            finish()
        }
    }
}