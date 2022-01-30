package me.ilker.business.question

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Int,
    val imageUri: String,
    val answer: String,
    val description: String? = null,
    var selectedAnswer: String? = null,
    val options: MutableList<String> = mutableListOf(answer)
)
