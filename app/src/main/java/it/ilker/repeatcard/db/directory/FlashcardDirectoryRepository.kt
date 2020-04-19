package it.ilker.repeatcard.db.directory

import it.ilker.repeatcard.db.directory.Directory
import it.ilker.repeatcard.db.directory.FlashcardDirectoryDao

class FlashcardDirectoryRepository(private val flashcardDirectoryDao: FlashcardDirectoryDao) {

    suspend fun addDirectory(directory: Directory) {
        flashcardDirectoryDao.insert(directory)
    }

/*    suspend fun addFlashcardToDirectory(id: Int) = flashcardDirectoryDao.addFlashcardToDirectory(id)*/

    suspend fun getDirectories() = flashcardDirectoryDao.getDirectories()

    suspend fun getDirectoryContent(id: Int) = flashcardDirectoryDao.getDirectoryContent(id)

    suspend fun deleteDirectory(id: Int) =
        flashcardDirectoryDao.deleteDirectory(id)
}
