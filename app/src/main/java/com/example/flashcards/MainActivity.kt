package com.example.flashcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // Called when user taps the Add Flashcard button
    fun addFlashcard(view: View) {
        val intent = Intent(this, AddFlashcardActivity::class.java)
        startActivity(intent)
    }
}
