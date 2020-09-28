package it.ilker.repeatcard.models.question

import kotlinx.serialization.Serializable

@Serializable
data class Question(val id: Int, val imageUri: String, val correctAnswer: String, val description: String?) {

    val options = mutableListOf<String>()
    var selectedAnswer: String? = null

    init {
        options.add(correctAnswer)
    }
}
