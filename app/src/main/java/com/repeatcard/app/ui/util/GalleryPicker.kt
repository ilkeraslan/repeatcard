package com.repeatcard.app.ui.util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.repeatcard.app.R
import com.yalantis.ucrop.UCrop
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import java.io.File
import java.io.IOException
import java.io.InputStream
import timber.log.Timber

const val GALLERY_IMAGE_URI = "com.repeatcard.app.GALLERY_URI"

private const val PICK_IMAGE_REQUEST = 689

private const val DEFAULT_QUALITY_GALLERY = 100

private const val DEFAULT_QUALITY_CROP = 80

private const val IMAGE_RATIO_X = 4f

private const val IMAGE_RATIO_Y = 3f

private const val IMAGE_SIZE_MAX = 500

class GalleryPicker : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, GalleryPicker::class.java)
    }

    private lateinit var imageSaver: ImageSaver

    private var bitmapCreationSubscription = Disposables.disposed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageSaver = InternalImageSaver(this)

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        if (savedInstanceState == null) {
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.image_chooser)),
                PICK_IMAGE_REQUEST
            )
        }
    }

    override fun onStop() {
        super.onStop()
        bitmapCreationSubscription.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && intent != null) {
            passGalleryImageForCropping(intent)
        } else if (requestCode == UCrop.REQUEST_CROP && intent != null) {
            returnCroppingResult(resultCode, intent)
        } else {
            showError(Throwable("Expected result from gallery or cropping!"))
            finish()
        }
    }

    private fun passGalleryImageForCropping(intent: Intent) {

        if (!bitmapCreationSubscription.isDisposed) {
            bitmapCreationSubscription.dispose()
        }

        bitmapCreationSubscription = Single.create<Bitmap> { singleEmitter ->
            try {
                singleEmitter.onSuccess(contentResolver.getBitmapFromUri(intent.data!!))
            } catch (exception: IOException) {
                singleEmitter.onError(exception)
            }
        }.flatMap { bitmap ->
            imageSaver.saveImage(
                GALLERY_IMAGE_NAME,
                bitmap,
                DEFAULT_QUALITY_GALLERY
            )
        }
            .subscribeOn(AndroidSchedulers.mainThread())
            .map { stringUri -> Uri.fromFile(File(stringUri)) }
            .subscribe(
                { fileUri -> openCropping(fileUri) },
                { throwable ->
                    showError(throwable)
                    finish()
                })
    }

    private fun openCropping(galleryImageUri: Uri) {

        val croppedImageUri = Uri.fromFile(imageSaver.createImageFile(CROPPED_IMAGE_NAME + System.nanoTime().toString()))
        val options = UCrop.Options()
        options.setCompressionQuality(DEFAULT_QUALITY_CROP)
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)

        UCrop.of(galleryImageUri, croppedImageUri)
            .withAspectRatio(IMAGE_RATIO_X, IMAGE_RATIO_Y)
            .withMaxResultSize(IMAGE_SIZE_MAX, IMAGE_SIZE_MAX)
            .withOptions(options)
            .start(this)
    }

    private fun returnCroppingResult(resultCode: Int, intent: Intent) {
        if (resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(intent)
            val finalUri = FileProvider.getUriForFile(
                applicationContext,
                "$packageName.provider",
                File(resultUri?.path)
            )

            val resultIntent = Intent(GALLERY_IMAGE_URI, finalUri)
            resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setResult(Activity.RESULT_OK, resultIntent)
        } else if (resultCode == UCrop.RESULT_ERROR) {
            showError(UCrop.getError(intent))
        }

        finish()
    }

    private fun showError(error: Throwable?) {
        Timber.d(error.toString())
    }
}

@Throws(IOException::class)
fun ContentResolver.getBitmapFromUri(uri: Uri): Bitmap {
    val angle = getRotationFromExif(uri, this)
    val parcelFileDescriptor = this.openFileDescriptor(uri, "r")
    checkNotNull(parcelFileDescriptor) { "Unable to get file descriptor for uri $uri" }

    parcelFileDescriptor?.let {
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        val bitmap =
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, BitmapFactory.Options())
        parcelFileDescriptor.close()
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }
}

@Suppress("ComplexMethod", "MagicNumber")
private fun getRotationFromExif(uri: Uri, contentResolver: ContentResolver): Int {
    var inputStream: InputStream? = null
    try {
        inputStream = contentResolver.openInputStream(uri)
        return if (inputStream == null) {
            0
        } else {
            val exif = ExifInterface(inputStream)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)) {
                1 -> 0
                3 -> 180
                6 -> 90
                8 -> 270
                else -> 0
            }
        }
    } catch (e: IOException) {
        return 0
    } finally {
        if (inputStream != null) {
            try {
                inputStream.close()
            } catch (ignored: IOException) {
            }
        }
    }
}
