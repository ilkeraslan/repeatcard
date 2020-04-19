package com.repeatcard.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repeatcard.app.R
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.ui.flashcarddetail.FlashcardDetailActivity
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by inject()
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeListener: HomeListener
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        homeViewModel.send(FlashcardEvent.Load)
        setUpRecyclerView(view)
    }

    @ExperimentalCoroutinesApi
    private fun setUpRecyclerView(view: View) {
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
                is FlashcardState.Error -> showError(state.error)
                is FlashcardState.Success -> showFlashcards(state.flashcards)
            }.exhaustive
        })
    }

    private fun showError(error: Throwable) {
        Toast.makeText(context, error.localizedMessage?.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        homeAdapter.submitList(flashcards)
    }
}
