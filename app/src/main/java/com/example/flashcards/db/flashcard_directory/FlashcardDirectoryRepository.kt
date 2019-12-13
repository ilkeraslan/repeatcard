package com.example.flashcards.db.flashcard_directory

class FlashcardDirectoryRepository(private val flashcardDirectoryDao: FlashcardDirectoryDao) {

    suspend fun insert(flashcardDirectory: FlashcardDirectory) {
        flashcardDirectoryDao.insert(flashcardDirectory)
    }

    suspend fun getDirectories() = flashcardDirectoryDao.getDirectories()

    suspend fun deleteDirectory(directory: FlashcardDirectory) =
        flashcardDirectoryDao.deleteDirectory(directory.id)
}