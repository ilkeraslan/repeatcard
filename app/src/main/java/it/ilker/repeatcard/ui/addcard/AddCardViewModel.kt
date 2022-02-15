package it.ilker.repeatcard.ui.addcard

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.ilker.repeatcard.db.FlashcardDatabase
import it.ilker.repeatcard.db.flashcard.FlashcardRepository
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.ilker.business.flashcard.Flashcard

sealed class AddCardState {
    object Error : AddCardState()
    data class Success(val flashcard: Flashcard) : AddCardState()
    object Loading : AddCardState()
    object Initial : AddCardState()
}

@SuppressLint("StaticFieldLeak")
@ExperimentalCoroutinesApi
class AddCardViewModel(private val context: Context) : ViewModel() {

    private val repository: FlashcardRepository

    private var _state = MutableStateFlow<AddCardState>(AddCardState.Initial)
    val state: StateFlow<AddCardState>
        get() = _state

    init {
        val flashcardsDao = FlashcardDatabase.getDatabase(context).flashcardDao()
        repository = FlashcardRepository(flashcardsDao)
    }

    private fun Bitmap.toUri(): Uri? {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageInQ(this, filename)
        } else {
            getImageUri(this, filename)
        }

        return uri
    }

    //Make sure to call this function on a worker thread, else it will block main thread
    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImageInQ(
        bitmap: Bitmap,
        filename: String
    ): Uri? {
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        //use application context to get contentResolver
        val contentResolver = context.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        imageUri?.let { contentResolver.update(it, contentValues, null, null) }

        return imageUri
    }

    private fun getImageUri(
        inImage: Bitmap,
        filename: String
    ): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, filename, null)
        return Uri.parse(path)
    }

    internal fun addCard(
        title: String,
        image: Bitmap?
    ) {
        viewModelScope.launch {
            val card = Flashcard(
                id = Random.nextInt(),
                title = title,
                imageUri = image?.toUri().toString()
            )

            val addedCardId = repository.insert(card)
            val addedCard = repository.getFlashcard(addedCardId.toInt())

            _state.value = AddCardState.Success(addedCard)
        }
    }
}
