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
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        observeViewModel()

        recyclerView = requireActivity().findViewById(R.id.recyclerView_home)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        homeAdapter = HomeAdapter()
        recyclerView.adapter = homeAdapter

        setUpViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                viewModel.send(
                    FlashcardEvent.AddFlashcard(
                        Flashcard(
                            id = 0,
                            title = data.extras?.get("ADD_FLASHCARD_TITLE_RESULT").toString(),
                            description = data.extras?.get("ADD_FLASHCARD_DESCRIPTION_RESULT").toString(),
                            creation_date = null,
                            last_modified = null,
                            directory_id = null
                        )
                    )
                )
            } else {
                Toast.makeText(context, "Error, no data.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Error, please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpViews() {
        val addFlashcardButton: Button =
            requireActivity().findViewById(R.id.add_flashcard_button)
        val deleteAll: Button = requireActivity().findViewById(R.id.delete_all_button)
        val deleteFlashcardButton: Button? = activity?.findViewById(R.id.deleteButtonHomeRow)
        val review: Button = requireActivity().findViewById(R.id.review_flashcards_button)

        addFlashcardButton.setOnClickListener {
            //AddFlashcardActivity.openAddFlashcardActivity(this.requireActivity()) TODO: Doesn't work.
            val intent = Intent(activity, AddFlashcardActivity::class.java)
            startActivityForResult(intent, 1000)
        }

        deleteAll.setOnClickListener {
            viewModel.send(FlashcardEvent.DeleteAll)
            Toast.makeText(context, "Deleted all.", Toast.LENGTH_SHORT).show()
        }

        deleteFlashcardButton?.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val inflater = this.layoutInflater

            dialogBuilder.setTitle("Are you sure you want to delete?")

            dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                viewModel.send(
                    FlashcardEvent.DeleteFlashcard(
                        recyclerView.findContainingViewHolder(
                            it
                        )?.itemId as Int
                    )
                )
            }

            dialogBuilder.setNegativeButton("No") { dialog, which ->
                viewModel.send(FlashcardEvent.Load)
            }

            dialogBuilder.create().show()
        }

        review.setOnClickListener {
            val intent = Intent(activity, FlashcardReviewScreen::class.java)
            intent.putExtra("FLASHCARD_COUNT", viewModel.allFlashcards.size)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
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
