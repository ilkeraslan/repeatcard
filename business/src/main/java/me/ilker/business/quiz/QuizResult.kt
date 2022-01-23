package me.ilker.business.quiz

import me.ilker.business.question.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuizResult(
    val id: String,
    val questions: List<Question>,
    val correctAnswers: List<Question>,
    val wrongAnswers: List<Question>
)
