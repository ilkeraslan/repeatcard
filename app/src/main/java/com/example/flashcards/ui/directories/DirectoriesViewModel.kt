package com.example.flashcards.ui.directories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class   DirectoriesViewModel : ViewModel() {

    private val directories_data = MutableLiveData<MutableList<String>>()

    init {
        directories_data.value = ArrayList()

        addDirectoriesData("directory-1")
        addDirectoriesData("directory-2")
        addDirectoriesData("directory-3")
        addDirectoriesData("directory-4")
    }

    val directories_list : LiveData<MutableList<String>> = directories_data

    // Public method to add data
    fun addDirectoriesData(directory: String) {
        directories_data.value?.add(directory)
    }
}
