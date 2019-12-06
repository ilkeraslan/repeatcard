package com.example.flashcards.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_table")
class Flashcard(@PrimaryKey @ColumnInfo(name = "flashcard") val flashcardTitle: String)