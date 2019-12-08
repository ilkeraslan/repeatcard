package com.example.flashcards.db

import android.app.Application
import androidx.room.Dao

class FlashcardDbManager (application: Application) {

    val application : Application = application

    fun getDatabase() : FlashcardDao{
        return FlashcardRoomDatabase.getDatabase(application).flashcardDao()
    }

    suspend fun getFlashcardList() : List<Flashcard> {
        return getDatabase().getFlashcards()
    }
}