package com.repeatcard.app.db.directory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.repeatcard.app.db.flashcard.Flashcard

@Dao
interface FlashcardDirectoryDao {

    @Query("SELECT * from flashcard_directories_table ORDER BY title ASC")
    suspend fun getDirectories(): List<Directory>

    @Query("SELECT * from flashcard_table WHERE directory_id = :id")
    suspend fun getDirectoryContent(id: Int): List<Flashcard>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(directory: Directory)

    @Query("DELETE FROM flashcard_directories_table")
    suspend fun deleteAll()

    @Query("DELETE FROM flashcard_directories_table WHERE id = :id")
    suspend fun deleteDirectory(id: Int)
}
