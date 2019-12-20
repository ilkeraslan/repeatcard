package com.example.flashcards.ui.directories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.flashcards.R

class AddDirectoryScreen : AppCompatActivity() {

    companion object {
        fun openAddDirectoryScreen(startindActivity: Activity) {
            val addDirectoryIntent = Intent(startindActivity, AddDirectoryScreen::class.java)
            startindActivity.startActivityForResult(addDirectoryIntent, 2000)
        }
    }

    private lateinit var directoryTitle: TextView
    private lateinit var directoryTitleEdit: EditText
    private lateinit var directorySaveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_directory)

        setUpViews()
    }

    private fun setUpViews() {
        directoryTitle = findViewById(R.id.directory_title)
        directoryTitleEdit = findViewById(R.id.directory_title_editText)
        directorySaveButton = findViewById(R.id.directory_save_button)

        directorySaveButton.setOnClickListener { turnToMain() }
    }

    private fun turnToMain() {
        val intentToMain = Intent()

        if (directoryTitleEdit.text.isNotEmpty()) {
            intentToMain.putExtra(
                "ADD_DIRECTORY_TITLE_RESULT",
                directoryTitleEdit.text.toString()
            )

            setResult(Activity.RESULT_OK, intentToMain)
        } else {
            setResult(Activity.RESULT_CANCELED)
        }

        finish()
    }
}