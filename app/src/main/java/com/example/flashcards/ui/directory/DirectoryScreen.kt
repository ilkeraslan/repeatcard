package com.example.flashcards.ui.directory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.AddFlashcardActivity
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.ui.directories.DirectoriesViewModel
import com.example.flashcards.ui.directories.DirectoryEvent
import com.example.flashcards.ui.directories.DirectoryState
import com.example.flashcards.ui.flashcard_review.FlashcardReviewScreen
import com.example.flashcards.ui.home.FlashcardEvent
import com.example.flashcards.ui.home.HomeViewModel
import com.example.flashcards.ui.notifications.NotificationEvent
import com.example.flashcards.ui.notifications.NotificationsViewModel
import com.example.flashcards.ui.util.exhaustive
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Runnable
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

private const val BUNDLE_TAG_DIRECTORY_ID: String = "BUNDLE_TAG_DIRECTORY_ID"

class DirectoryScreen : AppCompatActivity() {

    private lateinit var directoriesViewModel: DirectoriesViewModel
    private lateinit var homeViewModel: HomeViewModel

    @ExperimentalCoroutinesApi
    private lateinit var notificationsViewModel: NotificationsViewModel

    private lateinit var adapter: DirectoryAdapter
    private lateinit var directoryListener: DirectoryListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var noFlashcardText: TextView
    private lateinit var addFlashcard: FloatingActionButton
    private lateinit var review: FloatingActionButton

    private var directoryId = 1

    companion object {
        fun openDirectoryScreen(startingActivity: Activity, flashcardId: Int) {
            val intent = Intent(startingActivity, DirectoryScreen::class.java).putExtra(BUNDLE_TAG_DIRECTORY_ID, flashcardId)
            startingActivity.startActivity(intent)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.directory_layout)

        directoryId = intent.extras!!.getInt("BUNDLE_TAG_DIRECTORY_ID")

        setViewModels()
        observe()
        setUpRecyclerView()
        setUpViews()
    }

    @ExperimentalCoroutinesApi
    private fun setViewModels() {
        directoriesViewModel = ViewModelProvider(this).get(DirectoriesViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    private fun setUpViews() {
        noFlashcardText = findViewById(R.id.noFlashcardText)
        addFlashcard = findViewById(R.id.add_flashcard_to_directory)
        review = findViewById(R.id.reviewButton)

        directoriesViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))

        addFlashcard.setOnClickListener {
            val intent = Intent(this, AddFlashcardActivity::class.java)
            startActivityForResult(intent, 1000)
        }
    }

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewDirectory)
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        directoryListener = object : DirectoryListener {
            override fun itemDeleted(id: Int) {
                alertToDelete(id)
            }
        }
        adapter = DirectoryAdapter(directoryListener)
        recyclerView.adapter = adapter

        // TODO: Add clickListener to show details of a flashcard
    }

    private fun observe() {
        directoriesViewModel.directoryState.observe(this, Observer { state ->
            when (state) {
                is DirectoryState.Error -> {
                    showError()
                    review.visibility = INVISIBLE
                }
                is DirectoryState.DirectoryContentSuccess -> {
                    showFlashcards(state.flashcards)

                    review.visibility = VISIBLE
                    review.setOnClickListener {
                        val intent = Intent(this, FlashcardReviewScreen::class.java)
                        startActivity(intent)
                    }
                }
                is DirectoryState.Success -> review.visibility = INVISIBLE
            }.exhaustive
        })
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK && data != null) {
            val flashcard = Flashcard(
                id = 0,
                title = data.extras?.get("ADD_FLASHCARD_TITLE_RESULT").toString(),
                description = data.extras?.get("ADD_FLASHCARD_DESCRIPTION_RESULT").toString(),
                creation_date = OffsetDateTime.now().format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                ),
                last_modified = OffsetDateTime.now().format(
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)
                        .withZone(ZoneId.systemDefault())
                ),
                directory_id = directoryId,
                imageUri = data.extras?.get("ADD_FLASHCARD_IMAGE_RESULT") as String?
            )
            homeViewModel.send(FlashcardEvent.AddFlashcard(flashcard))
            notificationsViewModel.send(NotificationEvent.AddFlashcard(flashcard))

            runOnUiThread(Runnable { directoriesViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId)) }) // Does not work
        }
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.DirectoryTheme))

        dialogBuilder.setTitle("Are you sure you want to delete this?")
        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            homeViewModel.send(FlashcardEvent.DeleteFlashcard(id))
            notificationsViewModel.send(NotificationEvent.DeleteFlashcard(id))
            Toast.makeText(this.applicationContext, "Deleted flashcard.", Toast.LENGTH_SHORT).show()
        }
        dialogBuilder.setNegativeButton("No") { dialog, which -> dialog.cancel() }
        dialogBuilder.create().show()

        directoriesViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
    }

    private fun showError() {
        noFlashcardText.visibility = VISIBLE
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        noFlashcardText.visibility = INVISIBLE
        adapter.submitList(flashcards)
    }
}
