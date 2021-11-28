package it.ilker.repeatcard.ui.directories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.directory.Directory
import it.ilker.repeatcard.db.directory.FlashcardDirectoryRepository
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import it.ilker.repeatcard.db.notification.Notification
import it.ilker.repeatcard.db.notification.NotificationRepository
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

const val DEFAULT_DIRECTORY_NAME = "Miscellaneous"

sealed class DirectoriesEvent {
    data class AddDirectories(val directory: Directory) : DirectoriesEvent()
    data class DeleteDirectories(val id: Int) : DirectoriesEvent()
}

sealed class DirectoriesState {
    object Loading : DirectoriesState()
    data class Error(val error: Throwable) : DirectoriesState()
    data class Success(val directories: List<Directory>) : DirectoriesState()
}

@ExperimentalCoroutinesApi
class DirectoriesViewModel(context: Context) : ViewModel() {

    private val repository: FlashcardDirectoryRepository
    private val flashcardRepository: FlashcardRepository
    private val logsRepository: NotificationRepository

    private var _directoriesState = MutableStateFlow<DirectoriesState>(DirectoriesState.Loading)
    val directoriesState
        get() = _directoriesState

    init {
        val directoriesDao = FlashcardDatabase.getDatabase(context).directoryDao()
        val flashcardDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        val logsDao = FlashcardDatabase.getDatabase(context).notificationsDao()
        repository = FlashcardDirectoryRepository(directoriesDao)
        flashcardRepository = FlashcardRepository(flashcardDao)
        logsRepository = NotificationRepository(logsDao)

        viewModelScope.launch {
            val directories = repository.getDirectories()

            if (directories.isEmpty()) {
                directoriesState.value = DirectoriesState.Error(NullPointerException())
            } else {
                directoriesState.value = DirectoriesState.Success(directories)
            }
        }
    }

    fun send(event: DirectoriesEvent) {
        when (event) {
            is DirectoriesEvent.AddDirectories -> insert(event.directory)
            is DirectoriesEvent.DeleteDirectories -> deleteDirectory(event.id)
        }.exhaustive
    }

    private fun deleteDirectory(id: Int) = viewModelScope.launch {
        // Disassociate any Flashcard before deleting
        val flashcards = repository.getDirectoryContent(id)
        if (flashcards.isNotEmpty()) {
            flashcards.forEach { flashcard ->
                val updated = flashcard.copy(directoryId = null)
                flashcardRepository.updateFlashcard(updated)
            }
        }
        repository.deleteDirectory(id)
        logsRepository.insertNotification(createNotification("Deleted directory"))
    }

    private fun insert(directory: Directory) = viewModelScope.launch {
        val directories = repository.getDirectories()
        var canAddDirectory = true

        directories.forEach { existingDirectory ->
            if (existingDirectory.title == directory.title) canAddDirectory = false
        }

        if (canAddDirectory) {
            repository.addDirectory(directory)
            logsRepository.insertNotification(createNotification("Added directory"))
        }
    }

    private fun createNotification(title: String): Notification {
        return Notification(
            notificationId = 0,
            notificationTitle = title,
            notificationType = "directory",
            creationDate = OffsetDateTime.now().format(
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM).withZone(ZoneId.systemDefault())
            )
        )
    }
}
