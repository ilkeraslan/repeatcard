package me.ilker.business.flashcard

data class Flashcard(
    val id: Int,
    val title: String,
    val description: String? = null,
    val creationDate: String? = null,
    val lastModified: String? = null,
    val directoryId: Int? = null,
    val imageUri: String? = null
)
