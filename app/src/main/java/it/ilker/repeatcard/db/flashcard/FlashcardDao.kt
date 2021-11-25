package it.ilker.repeatcard.db.flashcard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FlashcardDao {

    @Query("DELETE FROM flashcard_table")
    suspend fun deleteAll()

    @Query("DELETE FROM flashcard_table WHERE id=:id")
    suspend fun deleteFlashcard(id: Int)

    @Query("SELECT * from flashcard_table  WHERE id=:id")
    suspend fun getFlashcard(id: Int): Flashcard

    @Query("SELECT * from flashcard_table ORDER BY flashcard_title ASC")
    suspend fun getFlashcards(): List<Flashcard>

    @Query("SELECT * FROM flashcard_table WHERE directory_id=:directoryId")
    suspend fun getFlashcardsForDirectory(directoryId: Int): List<Flashcard>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flashcard: Flashcard)

    @Update(entity = Flashcard::class)
    suspend fun updateFlashcard(flashcard: Flashcard)
}
