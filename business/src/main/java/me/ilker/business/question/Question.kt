package me.ilker.business.question

import kotlinx.serialization.Serializable
import me.ilker.business.answer.Answer

@Serializable
data class Question(
    val id: Int,
    val imageUri: String,
    val answer: Answer,
    val description: String? = null,
    var selectedAnswer: Answer? = null,
    val options: MutableList<Answer> = mutableListOf(answer)
)
