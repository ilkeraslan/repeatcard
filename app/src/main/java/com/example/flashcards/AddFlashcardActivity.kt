package com.example.flashcards

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)

        setUpViews()
    }

    private fun setUpViews() {

        flashcardTitle = findViewById(R.id.flashcard_title)
        flashcardDescription = findViewById(R.id.flashcard_description)
        flashcardTitleEdit = findViewById(R.id.flashcard_title_editText)
        flashcardDescriptionEdit = findViewById(R.id.flashcard_description_editText)
        flashcardSaveButton = findViewById(R.id.flashcard_save_button)

        flashcardSaveButton.setOnClickListener { turnToMain() }
    }

    /*
     * Function to return to HomeFragment
     */
    private fun turnToMain() {
        val intentToMain = Intent()

        if (flashcardTitleEdit.text.isNotEmpty()) {
            intentToMain.putExtra(
                "ADD_FLASHCARD_TITLE_RESULT",
                flashcardTitleEdit.text.toString()
            )
            intentToMain.putExtra(
                "ADD_FLASHCARD_DESC_RESULT",
                flashcardDescriptionEdit.text.toString()
            )

            setResult(Activity.RESULT_OK, intentToMain)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }
}
