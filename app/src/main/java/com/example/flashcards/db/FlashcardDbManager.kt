package com.example.flashcards.db

import android.app.Application
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.flashcard.FlashcardDao

class FlashcardDbManager (application: Application) {

    val application : Application = application

    fun getDatabase() : FlashcardDao {
        return FlashcardDatabase.getDatabase(application).flashcardDao()
    }

    suspend fun getFlashcardList() : List<Flashcard> {
        return getDatabase().getFlashcards()
    }
}