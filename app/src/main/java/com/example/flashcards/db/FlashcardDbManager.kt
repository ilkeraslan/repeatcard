package com.example.flashcards.db

import android.app.Application

class FlashcardDbManager (application: Application) {

    val application : Application = application

    fun getDatabase() : FlashcardDao{
        return FlashcardDatabase.getDatabase(application).flashcardDao()
    }

    suspend fun getFlashcardList() : List<Flashcard> {
        return getDatabase().getFlashcards()
    }
}