package com.repeatcard.app.models

import com.repeatcard.app.models.question.Question

class QuestionGenerator {

    companion object {
        private val questionsGenerated = mutableListOf<Question>()

        fun generate(questions: MutableList<Question>): List<Question> {
            questions.forEach { question ->

                // Create a temporary list that doesn't contain the question that we are generating
                val temporaryList = questions.filter { temporaryQuestion -> temporaryQuestion != question }

                // Shuffle the temporaryList to create randomness
                temporaryList.shuffled()

                temporaryList.forEach { otherQuestion ->
                    // If the question we are producing doesn't have the otherQuestion title among it's options, add it as an option
                    if (question.options.size < 4 && !question.options.contains(otherQuestion.correctAnswer)) {
                        question.options.add(otherQuestion.correctAnswer)
                    }
                }

                // Add the generated question to the final list
                questionsGenerated.add(question)
            }

            return questionsGenerated
        }
    }
}
