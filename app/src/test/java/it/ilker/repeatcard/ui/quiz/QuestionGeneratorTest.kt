package it.ilker.repeatcard.ui.quiz

import me.ilker.business.answer.Answer
import me.ilker.business.question.Question
import me.ilker.business.quiz.QuestionGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuestionGeneratorTest {

    private val firstQuestionCorrectAnswer = Answer("42")
    private val secondQuestionCorrectAnswer = Answer("1337")
    private val thirdQuestionCorrectAnswer = Answer("8675309")

    private val question1 = Question(
        id = 1,
        imageUri = "",
        answer = firstQuestionCorrectAnswer,
        description = "First question description"
    )

    private val question2 = Question(
        id = 2,
        imageUri = "",
        answer = secondQuestionCorrectAnswer,
        description = "Second question description"
    )

    private val question3 = Question(
        id = 3,
        imageUri = "",
        answer = thirdQuestionCorrectAnswer,
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
        val result = QuestionGenerator.generate(
            mutableListOf(
                question1,
                question2
            )
        )

        assertEquals(2, result.size)
    }

    @Test
    fun generate_addsOptionsToQuestions_whenMoreThanOneQuestionProvided() {
        val result = QuestionGenerator.generate(
            mutableListOf(
                question1,
                question2
            )
        )

        val firstQuestion = result.first { it.answer == firstQuestionCorrectAnswer }

        assertTrue(secondQuestionCorrectAnswer in firstQuestion.options)
    }

    @Test
    fun generate_shufflesResultingQuestions_byDefault() {
        var seenShuffledResult = false

        // This test could fail incorrectly if the shuffle operation fails to change the index of
        // the first item 100 times in a row. This is very unlikely. It only requires one
        // successful shuffle to pass.
        for (i in 0..100) {
            val result = QuestionGenerator.generate(
                mutableListOf(
                    question1,
                    question2,
                    question3
                )
            )

            if (result[0].answer != firstQuestionCorrectAnswer) {
                seenShuffledResult = true
            }
        }

        assertTrue(seenShuffledResult)
    }
}