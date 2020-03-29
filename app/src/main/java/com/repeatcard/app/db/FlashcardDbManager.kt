package com.repeatcard.app.db

import android.app.Application
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.db.flashcard.FlashcardDao

class FlashcardDbManager (application: Application) {

    val application : Application = application

    fun getDatabase() : FlashcardDao {
        return FlashcardDatabase.getDatabase(application).flashcardDao()
    }

    suspend fun getFlashcardList() : List<Flashcard> {
        return getDatabase().getFlashcards()
    }
}
