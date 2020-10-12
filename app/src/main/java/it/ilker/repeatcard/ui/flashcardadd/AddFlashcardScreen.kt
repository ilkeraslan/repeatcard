package it.ilker.repeatcard.ui.flashcardadd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.util.GalleryPicker
import timber.log.Timber

const val SELECT_IMAGE_INTENT = 2000

class AddFlashcardScreen : AppCompatActivity() {

    private lateinit var flashcardTitle: TextView
    private lateinit var flashcardDescription: TextView
    private lateinit var flashcardTitleEdit: EditText
    private lateinit var flashcardDescriptionEdit: EditText
    private lateinit var flashcardSaveButton: Button
    private lateinit var flashcardImage: ImageView
    private lateinit var tapToAdd: TextView
    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)

        setUpViews()
        setClickListeners()
    }

    private fun setUpViews() {
        flashcardTitle = findViewById(R.id.flashcard_title)
        flashcardDescription = findViewById(R.id.flashcard_description)
        flashcardTitleEdit = findViewById(R.id.flashcard_title_editText)
        flashcardDescriptionEdit = findViewById(R.id.flashcard_description_editText)
        flashcardSaveButton = findViewById(R.id.flashcard_save_button)
        flashcardImage = findViewById(R.id.flashcard_select_image)
        tapToAdd = findViewById(R.id.textTapToSelectImage)
    }

    private fun setClickListeners() {
        flashcardSaveButton.setOnClickListener {
            if (isValid())
                turnToMain()
            else Toast.makeText(this, R.string.flashcard_invalid_error, Toast.LENGTH_SHORT).show()
        }
        flashcardImage.setOnClickListener { selectImage() }
    }

    private fun isValid(): Boolean {
        return !(flashcardTitleEdit.text.isNullOrEmpty() || imageUri == null)
    }

    private fun turnToMain() {
        val intentToMain = Intent()
        intentToMain.putExtra(
            "ADD_FLASHCARD_TITLE_RESULT",
            flashcardTitleEdit.text.toString()
        )
        intentToMain.putExtra(
            "ADD_FLASHCARD_DESCRIPTION_RESULT",
            if (flashcardDescriptionEdit.text.isNullOrEmpty()) {
                "No description"
            } else {
                flashcardDescriptionEdit.text.toString()
            }
        )
        intentToMain.putExtra("ADD_FLASHCARD_IMAGE_RESULT", imageUri)
        setResult(Activity.RESULT_OK, intentToMain)
        finish()
    }

    private fun selectImage() {
        startActivityForResult(GalleryPicker.getIntent(this), SELECT_IMAGE_INTENT)
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
