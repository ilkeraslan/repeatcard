package com.example.flashcards.ui.home

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class HomeViewModelStoreOwner : ViewModelStoreOwner {
    override fun getViewModelStore(): ViewModelStore {
        return ViewModelStore()
    }

}