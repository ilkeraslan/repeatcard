package com.repeatcard.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repeatcard.app.R
import com.repeatcard.app.db.directory.Directory
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.ui.directories.DirectoriesViewModel
import com.repeatcard.app.ui.notifications.NotificationEvent
import com.repeatcard.app.ui.notifications.NotificationsViewModel
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    @ExperimentalCoroutinesApi
    private val notificationsViewModel: NotificationsViewModel by inject()
    private val directoriesViewModel: DirectoriesViewModel by inject()
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

        setUpRecyclerView()
        observeViewModel()
    }

    @ExperimentalCoroutinesApi
    private fun setUpRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recyclerView_home)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        homeListener = object : HomeListener {
            override fun itemDeleted(id: Int) {
                alertToDelete(id)
            }

            override fun addFlashcardToDirectory(id: Int) {
                if (getDirectories().isNotEmpty()) {
                    alertToAdd(id)
                } else showError(KotlinNullPointerException("No directory."))
            }
        }
        homeAdapter = HomeAdapter(homeListener)
        recyclerView.adapter = homeAdapter
    }

    private fun getDirectories(): MutableList<Directory> {
        val directoriesToAdd: MutableList<Directory> = mutableListOf()

        directoriesViewModel.allDirectories.observe(
            viewLifecycleOwner,
            Observer { directory ->
                directory.forEach { dir -> directoriesToAdd.add(dir) }
            })

        return directoriesToAdd
    }

    @ExperimentalCoroutinesApi
    private fun alertToAdd(flashcardId: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val directories = getDirectories()

        val radioGroup = RadioGroup(this.context)
        val scroll = ScrollView(this.context)

        directories.forEach { directory ->
            val radioButton = RadioButton(this.context)
            radioButton.id = directory.id
            radioButton.text = directory.title
            radioGroup.addView(radioButton)
            radioButton.text
        }

        radioGroup.check(directories.first().id)

        scroll.addView(radioGroup, RadioGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))

        dialogBuilder.setTitle("Select Directory")
        dialogBuilder.setPositiveButton("Select") { dialog, which ->
            homeViewModel.send(FlashcardEvent.AddToDirectory(flashcardId, radioGroup.checkedRadioButtonId))
            notificationsViewModel.send(NotificationEvent.AddToDirectory(flashcardId))
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        dialogBuilder.setView(scroll).create().show()
    }

    private fun alertToDelete() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Are you sure you want to delete ALL?")
        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            homeViewModel.send(FlashcardEvent.DeleteAll)
            Toast.makeText(context, "Deleted all.", Toast.LENGTH_SHORT).show()
        }
        dialogBuilder.setNegativeButton("No") { dialog, which -> homeViewModel.send(FlashcardEvent.Load) }
        dialogBuilder.create().show()
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Are you sure you want to delete this?")
        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            homeViewModel.send(FlashcardEvent.DeleteFlashcard(id))
            notificationsViewModel.send(NotificationEvent.DeleteFlashcard(id))
            Toast.makeText(context, "Deleted flashcard.", Toast.LENGTH_SHORT).show()
        }
        dialogBuilder.setNegativeButton("No") { dialog, which -> homeViewModel.send(FlashcardEvent.Load) }
        dialogBuilder.create().show()
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
