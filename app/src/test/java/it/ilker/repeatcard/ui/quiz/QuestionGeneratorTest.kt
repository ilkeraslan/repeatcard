package it.ilker.repeatcard.ui.quiz

import org.junit.Test

import it.ilker.repeatcard.models.question.Question
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class QuestionGeneratorTest {

    private val firstQuestionCorrectAnswer = "42"
    private val secondQuestionCorrectAnswer = "1337"
    private val thirdQuestionCorrectAnswer = "8675309"

    private val question1 = Question(
        id = 1,
        imageUri = "",
        correctAnswer = firstQuestionCorrectAnswer,
        description = "First question description"
    )

    private val question2 = Question(
        id = 2,
        imageUri = "",
        correctAnswer = secondQuestionCorrectAnswer,
        description = "Second question description"
    )

    private val question3 = Question(
            id = 3,
            imageUri = "",
            correctAnswer = thirdQuestionCorrectAnswer,
            description = "Second question description"
    )

    @Test
    fun generate_returnsEmptyList_whenEmptyListProvided() {
        val result = QuestionGenerator.generate(mutableListOf())

        assertTrue(result.isEmpty())
    }

    @Test
    fun generate_returnsSingleItemList_whenSingleItemProvided() {
        val result = QuestionGenerator.generate(mutableListOf(question1))

        assertEquals(1, result.size)
    }

    @Test
    fun generate_returnsTwoItemList_whenTwoItemsProvided() {
        val result = QuestionGenerator.generate(mutableListOf(
                question1,
                question2
        ))

        assertEquals(2, result.size)
    }

    @Test
    fun generate_addsOptionsToQuestions_whenMoreThanOneQuestionProvided() {
        val result = QuestionGenerator.generate(mutableListOf(
                question1,
                question2
        ))

        val firstQuestion = result.first { it.correctAnswer == firstQuestionCorrectAnswer }

        assertTrue(secondQuestionCorrectAnswer in firstQuestion.options)
    }

    @Test
    fun generate_shufflesResultingQuestions_byDefault() {
        var seenShuffledResult = false

        // This test could fail incorrectly if the shuffle operation fails to change the index of
        // the first item 100 times in a row. This is very unlikely. It only requires one
        // successful shuffle to pass.
        for (i in 0..100) {
            val result = QuestionGenerator.generate(mutableListOf(
                    question1,
                    question2,
                    question3
            ))

            if (result[0].correctAnswer != firstQuestionCorrectAnswer) {
                seenShuffledResult = true
            }
        }

        assertTrue(seenShuffledResult)
    }
}