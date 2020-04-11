package com.repeatcard.app.models.answer

import com.repeatcard.app.models.question.Question

data class Answer(val question: Question, val answer: String)
