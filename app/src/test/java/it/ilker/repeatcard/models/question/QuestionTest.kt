package it.ilker.repeatcard.models.question

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuestionTest {

    private val emptyCorrectAnswer = ""
    private val populatedCorrectAnswer = "Never Gonna Give You Up"
    private val selectedAnswer = "Some Answer"

    private val minimalSubject = Question(
            id = 0,
            imageUri = "",
            correctAnswer = emptyCorrectAnswer,
            description = null
    )

    private val fullSubject = Question(
            id = 1337,
            imageUri = "https://en.wikipedia.org/wiki/Rick_Astley#/media/File:Rick_Astely.jpg",
            correctAnswer = populatedCorrectAnswer,
            description = "This is a description of Rick Astley"
    )

    @Test
    fun getOptions_returnsCorrectAnswer_whenEmpty() {
        assertEquals(emptyCorrectAnswer, minimalSubject.options[0])
    }

    @Test
    fun getOptions_returnsCorrectAnswer_whenNotEmpty() {
        assertEquals(populatedCorrectAnswer, fullSubject.options[0])
    }

    @Test
    fun setSelectedAnswer_setsAnAnswer_whenGivenAnAnswer() {
        fullSubject.selectedAnswer = selectedAnswer
        assertEquals(selectedAnswer, fullSubject.selectedAnswer)
    }

    @Test
    fun setSelectedAnswer_acceptsNull_whenGivenNull() {
        minimalSubject.selectedAnswer = null
        assertNull(minimalSubject.selectedAnswer)
    }

    @Test
    fun getSelectedAnswer_returnsNull_beforeSelectedAnswerSet() {
        val newQuestion = Question(1, "", "", null)
        assertNull(newQuestion.selectedAnswer)
    }
}
