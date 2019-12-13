package com.example.flashcards.db.flashcard_directory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_directories_table")
class FlashcardDirectory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val creationDate: String?
)