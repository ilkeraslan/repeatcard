package it.ilker.repeatcard.models.question

import me.ilker.business.answer.Answer
import me.ilker.business.question.Question
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class QuestionTest {

    private val emptyCorrectAnswer = Answer("")
    private val populatedCorrectAnswer = Answer("Never Gonna Give You Up")
    private val selectedAnswer = Answer("Some Answer")

    private val minimalSubject = Question(
        id = 0,
        imageUri = "",
        answer = emptyCorrectAnswer,
        description = null
    )

    private val fullSubject = Question(
        id = 1337,
        imageUri = "https://en.wikipedia.org/wiki/Rick_Astley#/media/File:Rick_Astely.jpg",
        answer = populatedCorrectAnswer,
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
    fun getSelectedAnswer_returnsNull_beforeSelectedAnswerSet() {
        val newQuestion = Question(
            id = 1,
            imageUri = "",
            answer = Answer(""),
            description = null
        )

        assertNull(newQuestion.selectedAnswer)
    }
}
