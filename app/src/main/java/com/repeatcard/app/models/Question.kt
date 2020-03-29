package com.repeatcard.app.models

data class Question(val id: Int, val imageUri: String, val correctAnswer: String, val description: String?) {
    var option1: String = ""
    var option2: String = ""
    var option3: String = ""
    var option4: String = ""
    var selectedAnswer: String? = null
}
