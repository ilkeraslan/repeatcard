package it.ilker.repeatcard.db.directory

class FlashcardDirectoryRepository(private val flashcardDirectoryDao: FlashcardDirectoryDao) {

    suspend fun addDirectory(directory: Directory) {
        flashcardDirectoryDao.insert(directory)
    }

    suspend fun getDirectories() = flashcardDirectoryDao.getDirectories()

    suspend fun getDirectoryContent(id: Int) = flashcardDirectoryDao.getDirectoryContent(id)
        .map { flashcard -> flashcard.toDomain() }

    suspend fun deleteDirectory(id: Int) = flashcardDirectoryDao.deleteDirectory(id)
}
