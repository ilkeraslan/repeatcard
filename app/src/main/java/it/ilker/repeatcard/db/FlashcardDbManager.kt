package it.ilker.repeatcard.db

import android.app.Application
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.db.flashcard.FlashcardDao

class FlashcardDbManager(application: Application) {

    val application: Application = application

    private fun getDatabase(): FlashcardDao {
        return FlashcardDatabase.getDatabase(application).flashcardDao()
    }

    suspend fun getFlashcardList(): List<Flashcard> {
        return getDatabase().getFlashcards()
    }
}
