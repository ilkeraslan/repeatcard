package it.ilker.repeatcard.ui.review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.directory.BUNDLE_TAG_DIRECTORY_ID
import it.ilker.repeatcard.ui.directory.DirectoryEvent
import it.ilker.repeatcard.ui.directory.DirectoryState
import it.ilker.repeatcard.ui.directory.DirectoryViewModel
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.android.synthetic.main.flashcard_review_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

@ExperimentalCoroutinesApi
class FlashcardReviewScreen : AppCompatActivity() {

    private val viewModel: DirectoryViewModel by inject()

    private lateinit var reviewAdapter: FlashcardReviewAdapter
    private lateinit var closeButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var viewPager: ViewPager2
    private var directoryId = 1

    companion object {
        fun openReviewScreen(startingActivity: Activity, directoryId: Int) {
            val intent = Intent(startingActivity, FlashcardReviewScreen::class.java).putExtra(BUNDLE_TAG_DIRECTORY_ID, directoryId)
            startingActivity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_review_layout)
        directoryId = intent.extras!!.getInt("BUNDLE_TAG_DIRECTORY_ID")
        viewModel.send(DirectoryEvent.GetDirectoryContent(directoryId))
        setupViews()
        setupClickListeners()
        setupOnPageChangeListener()
        observe()
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is DirectoryState.Loading -> showLoader()
                    is DirectoryState.NoContent -> Toast.makeText(this@FlashcardReviewScreen, "error", Toast.LENGTH_SHORT).show()
                    is DirectoryState.HasContent -> {
                        progress_circular.visibility = GONE
                        content_group.visibility = VISIBLE
                        reviewAdapter.submitList(state.flashcards)
                    }
                }.exhaustive
            }
        }
    }

    private fun showLoader() {
        progress_circular.visibility = VISIBLE
        content_group.visibility = INVISIBLE
    }

    private fun setupViews() {
        reviewAdapter = FlashcardReviewAdapter()

        viewPager = findViewById(R.id.reviewPager)
        viewPager.adapter = reviewAdapter

        closeButton = findViewById(R.id.closeReviewButton)
        nextButton = findViewById(R.id.nextReviewButton)
        previousButton = findViewById(R.id.previousReviewButton)
    }

    private fun setupClickListeners() {
        closeButton.setOnClickListener { finish() }
        previousButton.setOnClickListener {
            if (viewPager.currentItem < 0) finish() else viewPager.currentItem--
        }
        nextButton.setOnClickListener {
            if (viewPager.currentItem == reviewAdapter.itemCount - 1) finish() else viewPager.currentItem++
        }
    }

    private fun setupOnPageChangeListener() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (viewPager.currentItem == 0) {
                    previousButton.visibility = INVISIBLE
                    nextButton.visibility = VISIBLE
                } else {
                    previousButton.visibility = VISIBLE
                    nextButton.visibility = VISIBLE
                }
            }
        })
    }
}
