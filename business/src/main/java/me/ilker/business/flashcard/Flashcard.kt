package me.ilker.business.flashcard

data class Flashcard(
    val id: Int,
    val title: String,
    val description: String?,
    val creationDate: String?,
    val lastModified: String?,
    val directoryId: Int?,
    val imageUri: String?
)
