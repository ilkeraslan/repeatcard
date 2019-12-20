package com.example.flashcards.db.flashcard

class FlashcardRepository(private val flashcardDao: FlashcardDao) {

    suspend fun insert(flashcard: Flashcard) {
        flashcardDao.insert(flashcard)
    }

    suspend fun getFlashcard(id: Int) = flashcardDao.getFlashcard(id)

    suspend fun getFlashcards() = flashcardDao.getFlashcards()

    suspend fun deleteAll() = flashcardDao.deleteAll()
}