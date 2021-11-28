package it.ilker.repeatcard.ui.flashcarddetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

private const val BUNDLE_TAG_FLASHCARD_ID: String = "BUNDLE_TAG_FLASHCARD_ID"

@ExperimentalCoroutinesApi
class FlashcardDetailActivity : AppCompatActivity() {

    private val viewModel: FlashcardDetailViewModel by inject()

    private var flashcardId = 0

    companion object {
        fun openFlashcardDetailActivity(context: Context, flashcardId: Int) {
            val intent = Intent(context, FlashcardDetailActivity::class.java).putExtra(BUNDLE_TAG_FLASHCARD_ID, flashcardId)
//            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val composeLayout = findViewById<ComposeView>(R.id.compose_layout)
        composeLayout.setContent {
            FlashcardDetail()
        }

        observeViewModel()
        flashcardId = intent.extras!!.getInt("BUNDLE_TAG_FLASHCARD_ID")
        viewModel.send(FlashcardDetailEvent.Load, flashcardId)
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    is FlashcardDetailState.Loading -> {
                    }
                    is FlashcardDetailState.Error -> {
                    }
                    is FlashcardDetailState.Success -> {
                    }
                }.exhaustive
            }
        }
    }
}
