package it.ilker.repeatcard.db.flashcard

class FlashcardRepository(private val flashcardDao: FlashcardDao) {

    suspend fun deleteAll() = flashcardDao.deleteAll()

    suspend fun deleteFlashcard(id: Int) = flashcardDao.deleteFlashcard(id)

    suspend fun getFlashcard(id: Int) = flashcardDao.getFlashcard(id).toDomain()

    suspend fun getFlashcards() = flashcardDao.getFlashcards().map { flashcard ->
        flashcard.toDomain()
    }

    suspend fun getFlashcardsForDirectory(directoryId: Int) =
        flashcardDao.getFlashcardsForDirectory(directoryId).map { flashcard ->
            flashcard.toDomain()
        }

    suspend fun insert(flashcard: me.ilker.business.flashcard.Flashcard) {
        flashcardDao.insert(
            Flashcard(
                id = flashcard.id,
                title = flashcard.title,
                description = flashcard.description,
                creationDate = flashcard.creationDate,
                lastModified = flashcard.lastModified,
                directoryId = flashcard.directoryId,
                imageUri = flashcard.imageUri
            )
        )
    }

    suspend fun updateFlashcard(flashcard: me.ilker.business.flashcard.Flashcard) =
        flashcardDao.updateFlashcard(
            Flashcard(
                id = flashcard.id,
                title = flashcard.title,
                description = flashcard.description,
                creationDate = flashcard.creationDate,
                lastModified = flashcard.lastModified,
                directoryId = flashcard.directoryId,
                imageUri = flashcard.imageUri
            )
        )
}
