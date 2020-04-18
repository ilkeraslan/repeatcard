package com.repeatcard.app.ui.flashcarddetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.repeatcard.app.R
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.ui.util.exhaustive
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val BUNDLE_TAG_FLASHCARD_ID: String = "BUNDLE_TAG_FLASHCARD_ID"

class FlashcardDetailActivity : AppCompatActivity() {

    private val viewModel: FlashcardDetailViewModel by inject()
    private lateinit var closeButton: Button
    private lateinit var detailTitle: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailImage: ImageView

    private var flashcardId = 0

    companion object {
        fun openFlashcardDetailActivity(startingActivity: Activity, flashcardId: Int) {
            val intent = Intent(startingActivity, FlashcardDetailActivity::class.java).putExtra(BUNDLE_TAG_FLASHCARD_ID, flashcardId)
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_detail_layout)
        observeViewModel()
        flashcardId = intent.extras!!.getInt("BUNDLE_TAG_FLASHCARD_ID")
        viewModel.send(FlashcardDetailEvent.Load, flashcardId)
        setUpViews()
    }

    private fun observeViewModel() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is FlashcardDetailState.Error -> showError()
                is FlashcardDetailState.Success -> showFlashcard(state.flashcard)
            }.exhaustive
        })
    }

    private fun setUpViews() {
        closeButton = findViewById(R.id.button_close_detail)
        detailTitle = findViewById(R.id.flashcard_detail_title)
        detailDescription = findViewById(R.id.flashcard_detail_description)
        detailImage = findViewById(R.id.flashcard_detail_imageView)
        closeButton.setOnClickListener { finish() }
    }

    private fun showFlashcard(flashcard: Flashcard) {
        detailTitle.text = flashcard.title
        detailDescription.text = flashcard.description

        Glide.with(this)
            .load(
                if (flashcard.imageUri.isNullOrEmpty()) resources.getDrawable(R.drawable.photography)
                else flashcard.imageUri
            )
            .into(detailImage)
    }

    private fun showError() {
        Timber.e(Error())
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
    }
}
