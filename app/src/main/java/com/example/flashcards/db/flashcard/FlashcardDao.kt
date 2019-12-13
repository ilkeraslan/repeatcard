package com.example.flashcards.db.flashcard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlashcardDao {

    @Query("SELECT * from flashcard_table ORDER BY flashcard_title ASC")
    suspend fun getFlashcards(): List<Flashcard>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(flashcard: Flashcard)

    @Query("DELETE FROM flashcard_table")
    suspend fun deleteAll()
}