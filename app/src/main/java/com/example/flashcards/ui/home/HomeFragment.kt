package com.example.flashcards.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.AddFlashcardActivity
import com.example.flashcards.R
import kotlinx.android.synthetic.main.home_fragment.*

const val SHARED_PREFS_HOME_NAME = "AddFlashcardActivity_prefs"
const val SHARED_PREFS_HOME_TEXT_TAG = "add_flashcard_text_data"
const val SHARED_PREFS_HOME_DESC_TAG = "add_flashcard_desc_data"

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // Set listener for add_flashcard button
        // add_flashcard.setOnClickListener { addFlashcard() }

        // Shared Preferences
        sharedPrefs = activity!!.getSharedPreferences(SHARED_PREFS_HOME_NAME, Context.MODE_PRIVATE)

        // LayoutManger and Adapter
        recyclerView_home.layoutManager = LinearLayoutManager(this.context)
        recyclerView_home.adapter = HomeAdapter()

        // Observer on flashcards_list variable
        viewModel.flashcards_list.observe(this, Observer {
            it.let {
                (recyclerView_home.adapter as HomeAdapter).flashcards = it
            }
        })
    }

    // Called when user taps the Add Flashcard button
    // TODO: Implement companion object to hide the logic
    fun addFlashcard() {
        val intent = Intent(activity, AddFlashcardActivity::class.java)
        activity?.startActivity(intent)
    }

}
