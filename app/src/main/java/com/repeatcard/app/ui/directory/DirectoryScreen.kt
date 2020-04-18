package com.repeatcard.app.ui.directory

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.repeatcard.app.R
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.ui.flashcardadd.AddFlashcardScreen
import com.repeatcard.app.ui.flashcardedit.EditFlashcardScreen
import com.repeatcard.app.ui.review.FlashcardReviewScreen
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

const val ADD_FLASHCARD_INTENT = 1000
const val BUNDLE_TAG_DIRECTORY_ID: String = "BUNDLE_TAG_DIRECTORY_ID"

class DirectoryScreen : AppCompatActivity() {

    private var directoryId = 1

    @ExperimentalCoroutinesApi
    private val directoryViewModel: DirectoryViewModel by inject()

    private lateinit var adapter: DirectoryAdapter
    private lateinit var directoryListener: DirectoryListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var noFlashcardText: TextView
    private lateinit var addFlashcard: FloatingActionButton
    private lateinit var review: FloatingActionButton

    companion object {
        fun openDirectoryScreen(context: Context, directoryId: Int) {
            val intent = Intent(context, DirectoryScreen::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(BUNDLE_TAG_DIRECTORY_ID, directoryId)
            context.startActivity(intent)
        }
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.directory_layout)
        observe()
        directoryId = intent.extras!!.getInt("BUNDLE_TAG_DIRECTORY_ID")
        directoryViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
        setUpRecyclerView()
        setUpViews()
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()
        directoryViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
    }

    private fun setUpViews() {
        noFlashcardText = findViewById(R.id.noFlashcardText)
        addFlashcard = findViewById(R.id.add_flashcard_to_directory)
        review = findViewById(R.id.reviewButton)
        noFlashcardText.visibility = INVISIBLE

        addFlashcard.setOnClickListener {
            val intent = Intent(this, AddFlashcardScreen::class.java)
            startActivityForResult(intent, ADD_FLASHCARD_INTENT)
        }
    }

    @ExperimentalCoroutinesApi
    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewDirectory)
        recyclerView.layoutManager = LinearLayoutManager(this.applicationContext)
        directoryListener = object : DirectoryListener {
            override fun itemDeleted(flashcard: Flashcard) {
                alertToDelete(flashcard)
            }

            override fun itemEdit(flashcard: Flashcard) {
                EditFlashcardScreen.openScreen(applicationContext, flashcard.id)
            }
        }
        adapter = DirectoryAdapter(directoryListener)
        recyclerView.adapter = adapter
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        directoryViewModel.state.observe(this, Observer { state ->
            when (state) {
                is DirectoryState.NoContent -> showNoContent(state.flashcards)
                is DirectoryState.HasContent -> showFlashcards(state.flashcards)
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
            directoryViewModel.send(DirectoryEvent.CardAdded(directoryId, flashcard))
        }
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(flashcard: Flashcard) {
        val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.DirectoryTheme))
        dialogBuilder.setTitle("Are you sure you want to delete this?")
        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            directoryViewModel.send(DirectoryEvent.CardDeleted(directoryId, flashcard))
        }
        dialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        dialogBuilder.create().show()
    }

    private fun showNoContent(flashcards: List<Flashcard>) {
        adapter.submitList(flashcards)
        adapter.notifyDataSetChanged()
        noFlashcardText.visibility = VISIBLE
        review.visibility = INVISIBLE
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        adapter.submitList(flashcards)
        adapter.notifyDataSetChanged()
        noFlashcardText.visibility = INVISIBLE
        review.visibility = VISIBLE
        review.setOnClickListener { FlashcardReviewScreen.openReviewScreen(this, this.directoryId) }
    }
}
