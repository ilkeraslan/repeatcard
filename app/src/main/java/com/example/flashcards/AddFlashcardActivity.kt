package com.example.flashcards

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class AddFlashcardActivity : AppCompatActivity() {

    private lateinit var flashcard_title: TextView
    private lateinit var flashcard_description: TextView
    private lateinit var flashcard_title_edit: EditText
    private lateinit var flashcard_description_edit: EditText
    private lateinit var flashcard_save_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)

        // Get reference of views
        flashcard_title =findViewById(R.id.flashcard_title)
        flashcard_description = findViewById(R.id.flashcard_description)
        flashcard_title_edit = findViewById(R.id.flashcard_title_editText)
        flashcard_description_edit = findViewById(R.id.flashcard_description_editText)
        flashcard_save_button = findViewById(R.id.flashcard_save_button)

        setUpViews()

        // Set clicklistener for save button
        flashcard_save_button.setOnClickListener { turnToMain() }
    }

    fun setUpViews() {
        if(flashcard_title_edit.text.isNotEmpty()) {
            flashcard_title.text = flashcard_title_edit.text
        }
        if(flashcard_description_edit.text.isNotEmpty()) {
            flashcard_description.text = flashcard_description_edit.text
        }
    }

    fun turnToMain() {
        finish()
    }
}
