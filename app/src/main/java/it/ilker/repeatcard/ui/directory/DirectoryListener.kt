package it.ilker.repeatcard.ui.directory

import me.ilker.business.flashcard.Flashcard

interface DirectoryListener {
    fun itemDeleted(flashcard: Flashcard)
    fun itemEdit(flashcard: Flashcard)
}
