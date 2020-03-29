package com.repeatcard.app.db.flashcard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.repeatcard.app.db.directory.Directory

@Entity(
    foreignKeys = [ForeignKey(
        entity = Directory::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("directory_id"),
        onDelete = CASCADE
    )],
    tableName = "flashcard_table"
)
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "flashcard_title") val title: String,
    @ColumnInfo(name = "flashcard_description") val description: String?,
    @ColumnInfo(name = "creation_date") val creationDate: String?,
    @ColumnInfo(name = "modification_date") val lastModified: String?,
    @ColumnInfo(name = "directory_id", index = true) var directoryId: Int?,
    @ColumnInfo(name = "image_uri") val imageUri: String?
)
