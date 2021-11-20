package it.ilker.repeatcard.ui.quiz

import it.ilker.repeatcard.models.question.Question

private const val OPTION_COUNT = 4

object QuestionGenerator {

    fun generate(questions: MutableList<Question>): List<Question> {
        val questionsGenerated = mutableListOf<Question>()
        questions.forEach { question ->

            // Create a temporary list that doesn't contain the question that we are generating
            val temporaryList = questions.filter { temporaryQuestion -> temporaryQuestion != question }

            // Shuffle the temporaryList to create randomness
            temporaryList.shuffled()

            temporaryList.forEach { otherQuestion ->
                // If the question we are producing doesn't have the otherQuestion title among it's options, add it as an option
                if (question.options.size < OPTION_COUNT && !question.options.contains(otherQuestion.correctAnswer)) {
                    question.options.add(otherQuestion.correctAnswer)
                }
            }

            // Shuffle options
            question.options.shuffle()

            // Add the generated question to the final list
            questionsGenerated.add(question)
        }

        // Shuffle generated questions
        questionsGenerated.shuffle()

        return questionsGenerated
    }
}
