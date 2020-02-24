package com.example.flashcards

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.flashcards.ui.util.GalleryPicker

class AddFlashcardActivity : AppCompatActivity() {

    companion object {
        fun openAddFlashcardActivity(startingActivity: Activity) {
            val intent = Intent(startingActivity, AddFlashcardActivity::class.java)
            startingActivity.startActivityForResult(intent, 1000)
        }
    }

    private lateinit var flashcardTitle: TextView
    private lateinit var flashcardDescription: TextView
    private lateinit var flashcardTitleEdit: EditText
    private lateinit var flashcardDescriptionEdit: EditText
    private lateinit var flashcardSaveButton: Button
    private lateinit var flashcardImage: ImageView
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
    }

    private fun setClickListeners() {
        flashcardSaveButton.setOnClickListener { turnToMain() }
        flashcardImage.setOnClickListener { selectImage() }
    }

    private fun turnToMain() {
        val intentToMain = Intent()

        if (flashcardTitleEdit.text.isNotEmpty()) {
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
            intentToMain.putExtra(
                "ADD_FLASHCARD_IMAGE_RESULT",
                if (imageUri.isNullOrEmpty()) {
                    imageUri = "No image"
                    "No image"
                } else {
                    imageUri
                }
            )

            setResult(Activity.RESULT_OK, intentToMain)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }

    private fun selectImage() {
        startActivityForResult(GalleryPicker.getIntent(this), 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                Toast.makeText(this, "Image ok.", Toast.LENGTH_SHORT).show()

                imageUri = data.data.toString()
                Log.d("IMAGE URI", imageUri.toString())

                // Load the image
                Glide.with(this).load(imageUri).into(flashcardImage)
            }
        }
    }

}
