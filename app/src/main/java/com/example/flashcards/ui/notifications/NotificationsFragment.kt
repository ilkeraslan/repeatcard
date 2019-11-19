package com.example.flashcards.ui.notifications

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.flashcards.R
import com.example.flashcards.ui.flashcard.Flashcard
import kotlinx.android.synthetic.main.notifications_fragment.*
import kotlin.random.Random

const val SHARED_PREFS_NOTIFICATIONS_NAME = "AddFlashcardActivity_prefs"
const val SHARED_PREFS_NOTIFICATION_TEXT_TAG = "add_flashcard_text_data"
const val SHARED_PREFS_NOTIFICATION_DESC_TAG = "add_flashcard_desc_data"

class NotificationsFragment : Fragment() {

    companion object {
        fun openNotificationsFragment() = NotificationsFragment()
    }

    private lateinit var viewModel: NotificationsViewModel
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)

        // Shared Preferences
        sharedPrefs =
            requireActivity().getSharedPreferences(
                SHARED_PREFS_NOTIFICATIONS_NAME,
                Context.MODE_PRIVATE
            )

        // LayoutManager and Adapter
        recyclerView_notifications.layoutManager = LinearLayoutManager(this.context)
        recyclerView_notifications.adapter = NotificationsAdapter()

        // Get variables from shared preferences
        setUpViews()

        // Observer on notifications_list variable
        viewModel.notifications_list.observe(this, Observer {
            it.let {
                (recyclerView_notifications.adapter as NotificationsAdapter).notification_titles =
                    it
            }
        })
    }

    private fun setUpViews() {

        // Get Flashcard name from shared preferences
        val flashcard_name = sharedPrefs.getString(
            SHARED_PREFS_NOTIFICATION_TEXT_TAG, "New Flashcard"
        )

        // Create new Flashcard
        val newFlashcard = Flashcard(Random.nextInt(), flashcard_name.toString())

        // Add new Flashcard to ViewModel
        viewModel.addNotificationsData(newFlashcard)

        // Observer on notifications_list variable
        viewModel.notifications_list.observe(this, Observer {
            it.let {
                (recyclerView_notifications.adapter as NotificationsAdapter).notification_titles =
                    it
            }
        })
    }
}
