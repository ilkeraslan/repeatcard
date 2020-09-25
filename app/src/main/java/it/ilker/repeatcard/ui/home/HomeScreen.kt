package it.ilker.repeatcard.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailActivity
import it.ilker.repeatcard.ui.util.exhaustive
import org.koin.android.ext.android.inject

class HomeScreen : Fragment() {

    private val homeViewModel: HomeViewModel by inject()
    private lateinit var latestCardsAdapter: HomeAdapter
    private lateinit var latestQuizResultsAdapter: HomeAdapter
    private lateinit var latestCardsListener: HomeListener
    private lateinit var latestCards: RecyclerView
    private lateinit var latestQuizResults: RecyclerView
    private lateinit var noFlashcardText: TextView
    private lateinit var noQuizResultText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        homeViewModel.send(FlashcardEvent.Load)
        setupViews(view)
        setupRecyclerView(view)
    }

    private fun setupViews(view: View) {
        noFlashcardText = view.findViewById(R.id.no_cards)
        noQuizResultText = view.findViewById(R.id.no_quiz)
        noFlashcardText.visibility = VISIBLE
        noQuizResultText.visibility = VISIBLE
    }

    private fun setupRecyclerView(view: View) {
        latestCards = view.findViewById(R.id.latest_cards)
        latestQuizResults = view.findViewById(R.id.latest_quiz_results)
        latestCards.layoutManager = LinearLayoutManager(this.context)
        latestQuizResults.layoutManager = LinearLayoutManager(this.context)

        latestCardsListener = object : HomeListener {
            override fun cardClicked(flashcard: Flashcard) {
                FlashcardDetailActivity.openFlashcardDetailActivity(view.context, flashcard.id)
            }
        }

        latestCardsAdapter = HomeAdapter(latestCardsListener)
        latestQuizResultsAdapter = HomeAdapter(latestCardsListener)
        latestCards.adapter = latestCardsAdapter
        latestQuizResults.adapter = latestQuizResultsAdapter
    }

    private fun observeViewModel() {
        homeViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is FlashcardState.Error -> showError()
                is FlashcardState.Success -> showResults(state.flashcards)
            }.exhaustive
        })
    }

    private fun showError() {
        noFlashcardText.visibility = VISIBLE
        latestCardsAdapter.submitList(listOf())
        latestCardsAdapter.notifyDataSetChanged()
    }

    private fun showResults(flashcards: List<Flashcard>) {
        noFlashcardText.visibility = INVISIBLE
        latestCardsAdapter.submitList(flashcards)
        latestCardsAdapter.notifyDataSetChanged()
    }
}
