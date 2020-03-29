package com.repeatcard.app.ui.directory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DirectoryViewModelFactory(private val application: Application, private val directoryId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = DirectoryViewModel(application, directoryId) as T
}
