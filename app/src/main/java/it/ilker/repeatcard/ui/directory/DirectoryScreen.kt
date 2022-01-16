package it.ilker.repeatcard.ui.directory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.ui.flashcardedit.EditFlashcardScreen
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.android.synthetic.main.directory_layout.content_group
import kotlinx.android.synthetic.main.directory_layout.progress_circular
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import timber.log.Timber

const val ADD_FLASHCARD_INTENT = 1000
const val BUNDLE_TAG_DIRECTORY_ID: String = "BUNDLE_TAG_DIRECTORY_ID"

@ExperimentalCoroutinesApi
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.directory_layout)
        observe()
        directoryId = intent.extras!!.getInt("BUNDLE_TAG_DIRECTORY_ID")
        directoryViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
        setUpRecyclerView()
        setUpViews()
    }

    override fun onResume() {
        super.onResume()
        directoryViewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        lifecycleScope.launchWhenStarted {
            directoryViewModel.state.collect { state ->
                when (state) {
                    is DirectoryState.Loading -> showLoader()
                    is DirectoryState.NoContent -> { /* no-op */
                    }
                    is DirectoryState.HasContent -> { /* no-op */
                    }
                }.exhaustive
            }
        }
    }

    private fun setUpViews() {
        noFlashcardText = findViewById(R.id.noFlashcardText)
        addFlashcard = findViewById(R.id.add_flashcard_to_directory)
        review = findViewById(R.id.reviewButton)
        noFlashcardText.visibility = INVISIBLE
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

    private fun showLoader() {
        progress_circular.visibility = VISIBLE
        content_group.visibility = INVISIBLE
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(flashcard: Flashcard) {
        val dialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.DirectoryTheme))
        dialogBuilder.setTitle("Are you sure you want to delete this?")
        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            Timber.d(flashcard.toString())
        }
        dialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        dialogBuilder.create().show()
    }
}
