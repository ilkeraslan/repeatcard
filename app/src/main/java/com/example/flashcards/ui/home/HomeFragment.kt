package com.example.flashcards.ui.home

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.AddFlashcardActivity
import com.example.flashcards.R
import com.example.flashcards.ui.flashcard.Flashcard
import kotlinx.android.synthetic.main.home_fragment.*
import kotlin.random.Random


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // LayoutManger and Adapter
        recyclerView_home.layoutManager = LinearLayoutManager(this.context)
        recyclerView_home.adapter = HomeAdapter()

        // Observer on flashcards_list variable
        observeViewModel()

        setUpViews()
    }

    private fun setUpViews() {

        val openAddFlashcardActivity: Button =
            requireActivity().findViewById(R.id.add_flashcard_button)

        // Set onClickListener on add flashcard button
        openAddFlashcardActivity.setOnClickListener {
            //AddFlashcardActivity.openAddFlashcardActivity(this.requireActivity()) TODO: Doesn't work.
            val intent = Intent(activity, AddFlashcardActivity::class.java)
            startActivityForResult(intent, 1000)
        }
    }

    /*
     * Function to check "AddFlashcardActivity" result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                viewModel.send(
                    FlashcardEvent.AddFlashcard(
                        Flashcard(
                            Random.nextInt(),
                            data.extras?.get("ADD_FLASHCARD_TITLE_RESULT").toString()
                        )
                    )
                )
            } else {
                Toast.makeText(context, "Error, no data.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Error, please try again.", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    private fun observeViewModel() {
        // Observer on flashcards_list variable
        // TODO: Observe the state
        viewModel.flashcards_list.observe(viewLifecycleOwner, Observer { flashcards ->
            flashcards.let {
                (recyclerView_home.adapter as HomeAdapter).flashcards =
                    flashcards
            }
        })
    }
}
