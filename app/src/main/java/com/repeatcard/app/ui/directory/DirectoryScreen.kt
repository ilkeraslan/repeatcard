package com.repeatcard.app.ui.directory

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.repeatcard.app.R
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.ui.flashcardadd.AddFlashcardActivity
import com.repeatcard.app.ui.flashcardedit.EditFlashcardScreen
import com.repeatcard.app.ui.home.FlashcardEvent
import com.repeatcard.app.ui.home.HomeViewModel
import com.repeatcard.app.ui.notifications.NotificationEvent
import com.repeatcard.app.ui.notifications.NotificationsViewModel
import com.repeatcard.app.ui.review.FlashcardReviewScreen
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

const val ADD_FLASHCARD_INTENT = 1000
const val BUNDLE_TAG_DIRECTORY_ID: String = "BUNDLE_TAG_DIRECTORY_ID"

class DirectoryScreen : AppCompatActivity() {

    @ExperimentalCoroutinesApi
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var directoryViewModel: DirectoryViewModel
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var adapter: DirectoryAdapter
    private lateinit var directoryListener: DirectoryListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var noFlashcardText: TextView
    private lateinit var addFlashcard: FloatingActionButton
    private lateinit var review: FloatingActionButton

    private var directoryId = 0

    companion object {
        fun openDirectoryScreen(startingActivity: Activity, directoryId: Int) {
            val intent = Intent(startingActivity, DirectoryScreen::class.java).putExtra(BUNDLE_TAG_DIRECTORY_ID, directoryId)
            startingActivity.startActivity(intent)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.directory_layout)

        directoryId = intent.extras!!.getInt("BUNDLE_TAG_DIRECTORY_ID")

        setViewModels()
        setUpRecyclerView()
        setUpViews()
        observe()
    }

    override fun onResume() {
        super.onResume()
        directoryViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
        observe()
    }

    @ExperimentalCoroutinesApi
    private fun setViewModels() {
        directoryViewModel = DirectoryViewModelFactory(application, directoryId).create(DirectoryViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        directoryViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
    }

    private fun setUpViews() {
        noFlashcardText = findViewById(R.id.noFlashcardText)
        addFlashcard = findViewById(R.id.add_flashcard_to_directory)
        review = findViewById(R.id.reviewButton)
        noFlashcardText.visibility = INVISIBLE

        addFlashcard.setOnClickListener {
            val intent = Intent(this, AddFlashcardActivity::class.java)
            startActivityForResult(intent, ADD_FLASHCARD_INTENT)
        }
    }

    @ExperimentalCoroutinesApi
    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewDirectory)
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        directoryListener = object : DirectoryListener {
            override fun itemDeleted(id: Int) {
                alertToDelete(id)
            }

            override fun itemEdit(id: Int) {
                EditFlashcardScreen.openScreen(applicationContext, id)
            }
        }
        adapter = DirectoryAdapter(directoryListener)
        recyclerView.adapter = adapter
    }

    private fun observe() {
        directoryViewModel.state.observe(this, Observer { state ->
            when (state) {
                is DirectoryState.NoContent -> {
                    noFlashcardText.visibility = VISIBLE
                    review.visibility = INVISIBLE
                }
                is DirectoryState.HasContent -> {
                    showFlashcards(state.flashcards)
                    review.visibility = VISIBLE
                    review.setOnClickListener {
                        FlashcardReviewScreen.openReviewScreen(this, this.directoryId)
                    }
                }
            }.exhaustive
        })
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_FLASHCARD_INTENT && resultCode == Activity.RESULT_OK && data != null) {
            val flashcard = Flashcard(
                id = 0,
                title = data.extras?.get("ADD_FLASHCARD_TITLE_RESULT").toString(),
                description = data.extras?.get("ADD_FLASHCARD_DESCRIPTION_RESULT").toString(),
                creationDate = OffsetDateTime.now().format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                ),
                lastModified = OffsetDateTime.now().format(
                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)
                        .withZone(ZoneId.systemDefault())
                ),
                directoryId = directoryId,
                imageUri = data.extras?.get("ADD_FLASHCARD_IMAGE_RESULT") as String?
            )
            homeViewModel.send(FlashcardEvent.AddFlashcard(flashcard))
            notificationsViewModel.send(NotificationEvent.AddFlashcard(flashcard))
            directoryViewModel.send(DirectoryEvent.GetDirectoryContent(this.directoryId))
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

        directoryViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
        adapter.notifyDataSetChanged()
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        noFlashcardText.visibility = INVISIBLE
        adapter.submitList(flashcards)
        adapter.notifyDataSetChanged()
    }
}
