package com.example.flashcards.ui.directory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.ui.directories.DirectoriesViewModel
import com.example.flashcards.ui.directories.DirectoryEvent
import com.example.flashcards.ui.directories.DirectoryState


private const val BUNDLE_TAG_DIRECTORY_ID: String = "BUNDLE_TAG_DIRECTORY_ID"

class DirectoryScreen : AppCompatActivity() {

    companion object {
        fun openDirectoryScreen(startingActivity: Activity, flashcardId: Int) {
            val intent = Intent(startingActivity, DirectoryScreen::class.java)
                .putExtra(BUNDLE_TAG_DIRECTORY_ID, flashcardId)

            startingActivity.startActivity(intent)
        }
    }

    private lateinit var viewModel: DirectoriesViewModel
    private lateinit var adapter: DirectoryAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.directory_layout)

        viewModel = ViewModelProvider(this).get(DirectoriesViewModel::class.java)

        observeViewModel()
        setUpRecyclerView()
        setUpViews()
    }

    private fun setUpViews() {
        val directoryId = intent.extras!!.getInt("BUNDLE_TAG_DIRECTORY_ID")

        viewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewDirectory)
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        adapter = DirectoryAdapter()
        recyclerView.adapter = adapter

        // TODO: Add clickListener to delete a flashcard
        // TODO: Add clickListener to show details of a flashcard
    }

    private fun observeViewModel() {
        viewModel.directoryState.observe(this, Observer { state ->
            when (state) {
                is DirectoryState.Error -> showError(state.error)
                is DirectoryState.DirectoryContentSuccess -> showFlashcards(state.flashcards)
            }
        })
    }

    private fun showError(error: Throwable) {
        Log.i("SHOW_ERROR", "Error: ", error)
        Toast.makeText(this, "No flashcards yet!", Toast.LENGTH_SHORT).show()
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        adapter.submitList(flashcards)
    }
}
