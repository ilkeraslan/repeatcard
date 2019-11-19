package com.example.flashcards.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.AddFlashcardActivity
import com.example.flashcards.R
import com.example.flashcards.ui.flashcard.Flashcard
import com.example.flashcards.ui.notifications.NotificationsAdapter
import com.example.flashcards.ui.notifications.SHARED_PREFS_NOTIFICATION_TEXT_TAG
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.notifications_fragment.*
import kotlin.random.Random

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

        // Shared Preferences
        sharedPrefs = requireActivity().getSharedPreferences(SHARED_PREFS_HOME_NAME, Context.MODE_PRIVATE)

        // LayoutManger and Adapter
        recyclerView_home.layoutManager = LinearLayoutManager(this.context)
        recyclerView_home.adapter = HomeAdapter()

        // Get variables from shared preferences
        setUpViews()

        // Observer on flashcards_list variable
        viewModel.flashcards_list.observe(this, Observer {
            it.let {
                (recyclerView_home.adapter as HomeAdapter).flashcards = it
            }
        })
    }

    private fun setUpViews() {

        val open_add_fragment_action: Button = requireActivity().findViewById(R.id.add_flashcard_button)
        open_add_fragment_action.setOnClickListener {
            AddFlashcardActivity.openAddFlashcardActivity(this.requireActivity())
        }

        // Get Flashcard name from shared preferences
        val flashcard_name = sharedPrefs.getString(
            SHARED_PREFS_NOTIFICATION_TEXT_TAG, "New Flashcard"
        )

        // Create new Flashcard
        val newFlashcard = Flashcard(Random.nextInt(),flashcard_name.toString())

        // Add new Flashcard to ViewModel
        viewModel.addFlashcard(newFlashcard)

        // Observer on notifications_list variable
        viewModel.flashcards_list.observe(this, Observer {
            it.let {
                (recyclerView_home.adapter as HomeAdapter).flashcards =
                    it
            }
        })
    }

    private fun refreshDataActions(prefs: SharedPreferences, title_view: TextView, description_view: TextView, saveDataAction: Button) {
        if(prefs.contains(SHARED_PREFS_HOME_TEXT_TAG)) {
            title_view.text = prefs.getString(SHARED_PREFS_HOME_TEXT_TAG, "No title.")
        }
        if(prefs.contains(SHARED_PREFS_HOME_DESC_TAG)) {
            description_view.text = prefs.getString(SHARED_PREFS_HOME_DESC_TAG, "No description.")
        }
    }

}
