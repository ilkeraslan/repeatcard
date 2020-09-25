package it.ilker.repeatcard.models.quizresult

import it.ilker.repeatcard.models.question.Question
import java.util.UUID

data class QuizResult(
    val id: UUID,
    val questions: List<Question>,
    val correctAnswers: List<Question>,
    val wrongAnswers: List<Question>
)