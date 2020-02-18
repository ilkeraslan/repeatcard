package com.example.flashcards.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.AddFlashcardActivity
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.ui.flashcard_review.FlashcardReviewScreen
import com.example.flashcards.ui.notifications.NotificationEvent
import com.example.flashcards.ui.notifications.NotificationsViewModel
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeListener: HomeListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        observeViewModel()

        setUpRecyclerView()

        setUpViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                val flashcard = Flashcard(
                    id = 0,
                    title = data.extras?.get("ADD_FLASHCARD_TITLE_RESULT").toString(),
                    description = data.extras?.get("ADD_FLASHCARD_DESCRIPTION_RESULT").toString(),
                    creation_date = OffsetDateTime.now().format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                    ),
                    last_modified = OffsetDateTime.now().format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.MEDIUM,
                            FormatStyle.MEDIUM
                        ).withZone(ZoneId.systemDefault())
                    ),
                    directory_id = null
                )
                homeViewModel.send(FlashcardEvent.AddFlashcard(flashcard))
                notificationsViewModel.send(NotificationEvent.AddFlashcard(flashcard))
            } else {
                Toast.makeText(context, "Error, no data.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Error, please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recyclerView_home)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        homeListener = object : HomeListener {
            override fun itemDeleted(id: Int) {
                alertToDelete(id)
            }

            override fun addFlashcardToDirectory(id: Int) {
                addToDirectory(id)
            }
        }
        homeAdapter = HomeAdapter(homeListener)

        recyclerView.adapter = homeAdapter
    }

    private fun addToDirectory(id: Int) {
        homeViewModel.send(FlashcardEvent.AddToDirectory(id, 1))
    }

    private fun setUpViews() {
        val addFlashcardButton: Button =
            requireActivity().findViewById(R.id.add_flashcard_button)
        val deleteAll: Button = requireActivity().findViewById(R.id.delete_all_button)
        val review: Button = requireActivity().findViewById(R.id.review_flashcards_button)

        addFlashcardButton.setOnClickListener {
            //AddFlashcardActivity.openAddFlashcardActivity(this.requireActivity()) TODO: Doesn't work.
            val intent = Intent(activity, AddFlashcardActivity::class.java)
            startActivityForResult(intent, 1000)
        }

        deleteAll.setOnClickListener {
            alertToDelete()
        }

        review.setOnClickListener {
            val intent = Intent(activity, FlashcardReviewScreen::class.java)
            startActivity(intent)
        }
    }

    private fun alertToDelete() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater

        dialogBuilder.setTitle("Are you sure you want to delete?")

        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            homeViewModel.send(
                FlashcardEvent.DeleteAll
            )
            Toast.makeText(context, "Deleted all.", Toast.LENGTH_SHORT).show()
        }

        dialogBuilder.setNegativeButton("No") { dialog, which ->
            homeViewModel.send(FlashcardEvent.Load)
        }

        dialogBuilder.create().show()
    }

    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater

        dialogBuilder.setTitle("Are you sure you want to delete?")

        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            homeViewModel.send(
                FlashcardEvent.DeleteFlashcard(id)
            )
            Toast.makeText(context, "Deleted flashcard.", Toast.LENGTH_SHORT).show()
        }

        dialogBuilder.setNegativeButton("No") { dialog, which ->
            homeViewModel.send(FlashcardEvent.Load)
        }

        dialogBuilder.create().show()
    }

    private fun observeViewModel() {
        homeViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is FlashcardState.Error -> showError(state.error)
                is FlashcardState.Success -> showFlashcards(state.flashcards)
            }
        })
    }

    private fun showError(error: Throwable) {
        Log.i("SHOW_ERROR", "Error: ", error)
        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        homeAdapter.submitList(flashcards)
    }
}
