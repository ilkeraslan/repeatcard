package com.example.flashcards.ui.flashcard_review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.ui.home.FlashcardState
import com.example.flashcards.ui.home.HomeViewModel

class FlashcardReviewFragment : Fragment() {

    companion object {
        private const val ARG_POSITION_NUMBER = "POSITION_NUMBER"
        fun newInstance(position: Int): FlashcardReviewFragment {
            return FlashcardReviewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION_NUMBER, position)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.flashcard_review_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val flashcardTitle: TextView? = activity?.findViewById(R.id.flashcard_review_title)

        flashcardTitle?.text = savedInstanceState?.getInt(ARG_POSITION_NUMBER).toString()
    }
}