package com.repeatcard.app.ui

import android.content.Context
import com.repeatcard.app.ui.directory.DirectoryScreen
import com.repeatcard.app.ui.quiz.QuizScreen

interface Navigator {
    fun goToDirectory(id: Int)
    fun goToQuiz()
}

class AppNavigator(private val context: Context) : Navigator {

    override fun goToDirectory(id: Int) {
        DirectoryScreen.openDirectoryScreen(context, id)
    }

    override fun goToQuiz() {
        QuizScreen.openScreen(context)
    }
}
