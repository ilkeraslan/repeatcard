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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

class HomeScreen : Fragment() {

    private val homeViewModel: HomeViewModel by inject()
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeListener: HomeListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var noFlashcardText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        homeViewModel.send(FlashcardEvent.Load)
        setupViews(view)
        setupRecyclerView(view)
    }

    private fun setupViews(view: View) {
        noFlashcardText = view.findViewById(R.id.noFlashcardTextHome)
    }

    @ExperimentalCoroutinesApi
    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView_home)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        homeListener = object : HomeListener {
            override fun cardClicked(flashcard: Flashcard) {
                FlashcardDetailActivity.openFlashcardDetailActivity(view.context, flashcard.id)
            }
        }
        homeAdapter = HomeAdapter(homeListener)
        recyclerView.adapter = homeAdapter
    }

    private fun observeViewModel() {
        homeViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is FlashcardState.Error -> showError()
                is FlashcardState.Success -> showFlashcards(state.flashcards)
            }.exhaustive
        })
    }

    private fun showError() {
        noFlashcardText.visibility = VISIBLE
        homeAdapter.submitList(listOf())
        homeAdapter.notifyDataSetChanged()
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        noFlashcardText.visibility = INVISIBLE
        homeAdapter.submitList(flashcards)
        homeAdapter.notifyDataSetChanged()
    }
}
