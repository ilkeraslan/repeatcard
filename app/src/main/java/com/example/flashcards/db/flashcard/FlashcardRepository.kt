package com.example.flashcards.db.flashcard

class FlashcardRepository(private val flashcardDao: FlashcardDao) {

    suspend fun deleteAll() = flashcardDao.deleteAll()

    suspend fun deleteFlashcard(id: Int) = flashcardDao.deleteFlashcard(id)

    suspend fun getFlashcard(id: Int) = flashcardDao.getFlashcard(id)

    suspend fun getFlashcards() = flashcardDao.getFlashcards()

    suspend fun insert(flashcard: Flashcard) {
        flashcardDao.insert(flashcard)
    }
}
