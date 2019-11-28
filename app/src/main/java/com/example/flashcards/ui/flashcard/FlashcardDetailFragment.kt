package com.example.flashcards.ui.flashcard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.flashcards.R

private const val BUNDLE_TAG_FLASHCARD_ID: String = "BUNDLE_TAG_FLASHCARD_ID"

class FlashcardDetailFragment : Fragment() {

    companion object {
        fun openGifDetailActivity(startingActivity: Activity, flashcardId: Int) {
            val intent = Intent(startingActivity, FlashcardDetailFragment::class.java)
                .putExtra(BUNDLE_TAG_FLASHCARD_ID, flashcardId)

            startingActivity.startActivity(intent)
        }
    }

    private lateinit var viewModel: FlashcardDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.flashcard_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FlashcardDetailViewModel::class.java)

        val application = requireNotNull(this.activity).application
        val arguments = this.arguments

        // Add observer to state variable
        viewModel.navigate_to_home_state.observe(this, Observer {
            if(it == true) {
                this.findNavController().navigate(R.id.navigation_home)
            }
        })
    }
}