package com.repeatcard.app.ui.directory

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.repeatcard.app.db.FlashcardDatabase
import com.repeatcard.app.db.directory.FlashcardDirectoryRepository
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.db.flashcard.FlashcardRepository
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.launch

sealed class DirectoryEvent {
    data class GetDirectoryContent(val directoryId: Int) : DirectoryEvent()
    data class CardDeleted(val directoryId: Int) : DirectoryEvent()
}

sealed class DirectoryState {
    data class HasContent(val flashcards: List<Flashcard>) : DirectoryState()
    object NoContent : DirectoryState()
}

class DirectoryViewModel(context: Context, directoryId: Int) : ViewModel() {

    private val directoryRepository: FlashcardDirectoryRepository
    private val flashcardRepository: FlashcardRepository
    var state: MutableLiveData<DirectoryState> = MutableLiveData()

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(context).directoryDao()
        val flashcardDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        directoryRepository = FlashcardDirectoryRepository(directoriesDao)
        flashcardRepository = FlashcardRepository(flashcardDao)
    }

    fun send(event: DirectoryEvent) {
        when (event) {
            is DirectoryEvent.GetDirectoryContent -> getDirectoryContent(event.directoryId)
            is DirectoryEvent.CardDeleted -> getDirectoryContent(event.directoryId)
        }.exhaustive
    }

    private fun getDirectoryContent(directoryId: Int) = viewModelScope.launch {
        val directoryContent = flashcardRepository.getFlashcardsForDirectory(directoryId)

        if (directoryContent.isEmpty()) {
            state.postValue(DirectoryState.NoContent)
        } else {
            state.postValue(DirectoryState.HasContent(directoryContent))
        }
    }
}
