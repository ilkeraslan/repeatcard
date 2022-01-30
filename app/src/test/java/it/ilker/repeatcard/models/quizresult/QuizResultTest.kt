package it.ilker.repeatcard.models.quizresult

import me.ilker.business.quiz.QuizResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizResultTest {

    private val emptySubject = QuizResult(
            id = "",
            questions = emptyList(),
            correctAnswers = emptyList(),
            wrongAnswers = emptyList()
    )

    @Test
    fun testGetId_returnsEmptyString_whenGivenEmptyString() {
        assertEquals("", emptySubject.id)
    }

    @Test
    fun testGetQuestions_returnsEmptyList_whenGivenEmptyList() {
        assertTrue(emptySubject.questions.isEmpty())
    }

    @Test
    fun testGetCorrectAnswers_returnsEmptyList_whenGivenEmptyList() {
        assertTrue(emptySubject.correctAnswers.isEmpty())
    }

    @Test
    fun testGetWrongAnswers_returnsEmptyList_whenGivenEmptyList() {
        assertTrue(emptySubject.wrongAnswers.isEmpty())
    }
}
