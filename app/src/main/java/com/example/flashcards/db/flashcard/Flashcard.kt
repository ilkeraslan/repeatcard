package com.example.flashcards.db.flashcard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_table")
class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "flashcard_title") val title: String,
    @ColumnInfo(name = "flashcard_description") val description: String?,
    @ColumnInfo(name = "creation_date") val creation_date: String?,
    @ColumnInfo(name = "modification_date") val last_modified: String?,
    @ColumnInfo(name = "directory_id") val directory_id: Int?
)
