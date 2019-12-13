package com.example.flashcards.db.flashcard_directory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FlashcardDirectoryDao  {

    @Query("SELECT * from flashcard_directories_table ORDER BY title ASC")
    suspend fun getDirectories() : List<FlashcardDirectory>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(flashcardDirectory: FlashcardDirectory)

    @Query("DELETE FROM flashcard_directories_table")
    suspend fun deleteAll()

    @Query("DELETE FROM flashcard_directories_table WHERE id = :id")
    suspend fun deleteDirectory(id: Int)
}