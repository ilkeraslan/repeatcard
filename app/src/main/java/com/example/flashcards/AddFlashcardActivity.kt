package com.example.flashcards

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

const val SHARED_PREFS_ADD_FLASHCARD_NAME = "AddFlashcardActivity_prefs"
const val SHARED_PREFS_ADD_FLASHCARD_TEXT_TAG = "add_flashcard_text_data"
const val SHARED_PREFS_ADD_FLASHCARD_DESC_TAG = "add_flashcard_desc_data"

class AddFlashcardActivity : AppCompatActivity() {

    companion object {
        fun openAddFlashcardActivity(startingActivity: Activity) {
            val intent = Intent(startingActivity, AddFlashcardActivity::class.java)
            startingActivity.startActivity(intent)
        }
    }

    private lateinit var flashcard_title: TextView
    private lateinit var flashcard_description: TextView
    private lateinit var flashcard_title_edit: EditText
    private lateinit var flashcard_description_edit: EditText
    private lateinit var flashcard_save_button: Button

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)

        // Get shared preferences
        sharedPrefs = getSharedPreferences(SHARED_PREFS_ADD_FLASHCARD_NAME, Context.MODE_PRIVATE)

        setUpViews()
    }

    fun setUpViews() {

        // Get reference of views
        flashcard_title =findViewById(R.id.flashcard_title)
        flashcard_description = findViewById(R.id.flashcard_description)
        flashcard_title_edit = findViewById(R.id.flashcard_title_editText)
        flashcard_description_edit = findViewById(R.id.flashcard_description_editText)
        flashcard_save_button = findViewById(R.id.flashcard_save_button)

        // Click listener on save button
        flashcard_save_button.setOnClickListener {
            sharedPrefs.edit().putString(SHARED_PREFS_ADD_FLASHCARD_TEXT_TAG, flashcard_title_edit.text.toString()).apply()
            sharedPrefs.edit().putString(SHARED_PREFS_ADD_FLASHCARD_DESC_TAG, flashcard_description_edit.text.toString()).apply()

            refreshDataActions(sharedPrefs, flashcard_title, flashcard_description, flashcard_save_button)

            turnToMain()
        }
    }

    private fun refreshDataActions(prefs: SharedPreferences, title_view: TextView, description_view: TextView, saveDataAction: Button) {
        if(prefs.contains(SHARED_PREFS_ADD_FLASHCARD_TEXT_TAG)) {
            title_view.text = prefs.getString(SHARED_PREFS_ADD_FLASHCARD_TEXT_TAG, "No title.")
        }
        if(prefs.contains(SHARED_PREFS_ADD_FLASHCARD_DESC_TAG)) {
            description_view.text = prefs.getString(SHARED_PREFS_ADD_FLASHCARD_DESC_TAG, "No description.")
        }
    }

    fun turnToMain() {
        finish()
    }
}
