package it.ilker.repeatcard.ui

import android.content.Context
import it.ilker.repeatcard.ui.directory.DirectoryScreen
import it.ilker.repeatcard.ui.main.MainScreen
import it.ilker.repeatcard.ui.quiz.QuizScreen

interface Navigator {
    fun goToDirectory(id: Int)
    fun goToMain()
    fun goToQuiz()
}

class AppNavigator(private val context: Context) : Navigator {

    override fun goToDirectory(id: Int) {
        DirectoryScreen.openDirectoryScreen(context, id)
    }

    override fun goToMain() {
        MainScreen.openScreen(context)
    }

    override fun goToQuiz() {
        QuizScreen.openScreen(context)
    }
}
