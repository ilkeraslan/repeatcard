package com.repeatcard.app.ui

import android.content.Context
import com.repeatcard.app.ui.directory.DirectoryScreen

interface Navigator {
    fun goToDirectory(id: Int)
}

class AppNavigator(private val context: Context) : Navigator {

    override fun goToDirectory(id: Int) {
        DirectoryScreen.openDirectoryScreen(context, id)
    }
}
