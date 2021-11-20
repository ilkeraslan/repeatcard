package it.ilker.repeatcard.db.flashcard

class FlashcardRepository(private val flashcardDao: FlashcardDao) {

    suspend fun deleteAll() = flashcardDao.deleteAll()

    suspend fun deleteFlashcard(id: Int) = flashcardDao.deleteFlashcard(id)

    suspend fun getFlashcard(id: Int) = flashcardDao.getFlashcard(id)

    suspend fun getFlashcards() = flashcardDao.getFlashcards()

    suspend fun getFlashcardsForDirectory(directoryId: Int) =
        flashcardDao.getFlashcardsForDirectory(directoryId)

    suspend fun insert(flashcard: Flashcard) {
        flashcardDao.insert(flashcard)
    }

    suspend fun updateFlashcard(flashcard: Flashcard) = flashcardDao.updateFlashcard(flashcard)
}
