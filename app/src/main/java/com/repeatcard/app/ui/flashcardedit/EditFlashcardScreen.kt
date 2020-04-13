package com.repeatcard.app.ui.flashcardedit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.repeatcard.app.R
import com.repeatcard.app.ui.util.GalleryPicker
import com.repeatcard.app.ui.util.exhaustive
import timber.log.Timber

class EditFlashcardScreen : AppCompatActivity() {

    private lateinit var editFlashcardViewModel: EditFlashcardViewModel
    private lateinit var flashcardTitle: TextView
    private lateinit var flashcardDescription: TextView
    private lateinit var flashcardTitleEdit: EditText
    private lateinit var flashcardDescriptionEdit: EditText
    private lateinit var flashcardSaveButton: Button
    private lateinit var flashcardImage: ImageView
    private lateinit var tapToAdd: TextView
    private var imageUri: String? = null

    companion object {
        private const val FLASHCARD_ID = "FLASHCARD_ID"
        private const val SELECT_IMAGE_INTENT = 2000

        fun openScreen(startingContext: Context, flashcardId: Int) {
            val intent = Intent(startingContext, EditFlashcardScreen::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(FLASHCARD_ID, flashcardId)

            startingContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)

        editFlashcardViewModel = EditFlashcardViewModel(this.application)
        setViews()
        observe()

        val flashcardId = intent.extras!!.getInt(FLASHCARD_ID)
        editFlashcardViewModel.send(FlashcardEditEvent.GetCurrentValues(flashcardId))
    }

    private fun setViews() {
        flashcardTitle = findViewById(R.id.flashcard_title)
        flashcardDescription = findViewById(R.id.flashcard_description)
        flashcardTitleEdit = findViewById(R.id.flashcard_title_editText)
        flashcardDescriptionEdit = findViewById(R.id.flashcard_description_editText)
        flashcardSaveButton = findViewById(R.id.flashcard_save_button)
        flashcardImage = findViewById(R.id.flashcard_select_image)
        tapToAdd = findViewById(R.id.textTapToSelectImage)
    }

    private fun observe() {
        editFlashcardViewModel.state.observe(this, Observer { state ->
            when (state) {
                is FlashcardEditState.Success -> {
                    setCurrentValues(state)
                    flashcardImage.setOnClickListener {
                        startActivityForResult(GalleryPicker.getIntent(this), SELECT_IMAGE_INTENT)
                    }
                    flashcardSaveButton.setOnClickListener {
                        editFlashcardViewModel.send(
                            FlashcardEditEvent.Edit(
                                state.flashcard.id,
                                flashcardTitleEdit.text.toString(),
                                flashcardDescriptionEdit.text.toString(),
                                imageUri
                            )
                        )
                    }
                }
                FlashcardEditState.UpdateSuccess -> finish()
            }.exhaustive
        })
    }

    private fun setCurrentValues(state: FlashcardEditState.Success) {
        flashcardTitleEdit.setText(state.flashcard.title)
        flashcardTitleEdit.selectAll()
        flashcardTitleEdit.requestFocus()

        flashcardDescriptionEdit.setText(
            if (state.flashcard.description == "No description") null
            else state.flashcard.description
        )

        if (!state.flashcard.imageUri.isNullOrEmpty()) {
            Glide.with(this).load(state.flashcard.imageUri).into(flashcardImage)
            tapToAdd.visibility = INVISIBLE
            flashcardImage.background = null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGE_INTENT && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data.toString()
            Timber.d(imageUri.toString())

            // Load the image
            Glide.with(this).load(imageUri).into(flashcardImage)

            tapToAdd.visibility = INVISIBLE
            flashcardImage.background = null
        }
    }
}
