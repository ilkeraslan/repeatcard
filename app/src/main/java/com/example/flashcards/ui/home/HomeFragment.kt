package com.example.flashcards.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.AddFlashcardActivity
import com.example.flashcards.R
import com.example.flashcards.db.directory.Directory
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.ui.directories.DirectoriesViewModel
import com.example.flashcards.ui.flashcard_review.FlashcardReviewScreen
import com.example.flashcards.ui.notifications.NotificationEvent
import com.example.flashcards.ui.notifications.NotificationsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    @ExperimentalCoroutinesApi
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var directoriesViewModel: DirectoriesViewModel
    private lateinit var homeViewModel: HomeViewModel
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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModels()
        setUpRecyclerView()
        setUpViews()
        observeViewModel()
    }

    @ExperimentalCoroutinesApi
    private fun setupViewModels() {
        directoriesViewModel = ViewModelProvider(this).get(DirectoriesViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
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

        deleteAll.setOnClickListener { alertToDelete() }

        review.setOnClickListener {
            val intent = Intent(activity, FlashcardReviewScreen::class.java)
            startActivity(intent)
        }
    }

    @ExperimentalCoroutinesApi
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
                    directory_id = null,
                    imageUri = data.extras?.get("ADD_FLASHCARD_IMAGE_RESULT").toString()
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

    private fun getDirectories(): MutableList<Directory> {
        val directoriesToAdd: MutableList<Directory> = mutableListOf()

        directoriesViewModel.allDirectories.observe(
            viewLifecycleOwner,
            Observer { directory ->
                directory.forEach { dir ->
                    directoriesToAdd.add(dir)
                }
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
            homeViewModel.send(
                FlashcardEvent.AddToDirectory(
                    flashcardId,
                    radioGroup.checkedRadioButtonId // TODO: Doesn't work
                )
            )
            notificationsViewModel.send(NotificationEvent.DeleteFlashcard(flashcardId))
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

        dialogBuilder.setNegativeButton("No") { dialog, which ->
            homeViewModel.send(FlashcardEvent.Load)
        }

        dialogBuilder.create().show()
    }

    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Are you sure you want to delete this?")

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
        Toast.makeText(context, error.localizedMessage?.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun showFlashcards(flashcards: List<Flashcard>) {
        homeAdapter.submitList(flashcards)
    }
}
