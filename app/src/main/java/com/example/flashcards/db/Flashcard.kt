package com.example.flashcards.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_table")
class Flashcard(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "flashcard_title") val flashcardTitle: String
)