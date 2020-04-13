package com.repeatcard.app.ui.flashcardedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.repeatcard.app.R
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
        editFlashcardViewModel.send(FlashcardEditEvent.Edit(flashcardId))
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
                    Timber.d("EDIT OK")
                }
            }
        })
    }
}
