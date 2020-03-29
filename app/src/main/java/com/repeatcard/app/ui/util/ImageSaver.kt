package com.repeatcard.app.ui.util

import android.content.Context
import android.graphics.Bitmap
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

const val GALLERY_IMAGE_NAME = "galleryImage"

const val CROPPED_IMAGE_NAME = "croppedImage"

private const val JPG = ".jpg"

interface ImageSaver {

    fun saveImage(name: String, bitmap: Bitmap, quality: Int): Single<String>

    fun createImageFile(name: String): File

}

class InternalImageSaver(val context: Context) : ImageSaver {

    private val internalImagesFiles: File = File(context.filesDir, "images")

    init {
        internalImagesFiles.mkdirs()
    }

    override fun saveImage(name: String, bitmap: Bitmap, quality: Int): Single<String> {
        return Single.create { singleEmitter ->
            try {
                val imageFile = createImageFile(name)
                val fileOutputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
                singleEmitter.onSuccess(imageFile.path)
            } catch (e: IOException) {
                singleEmitter.onError(e)
            }
        }
    }

    @Throws(IOException::class)
    override fun createImageFile(name: String): File {
        val imageFile = File(internalImagesFiles, name + JPG)
        imageFile.createNewFile()
        return imageFile
    }
}
