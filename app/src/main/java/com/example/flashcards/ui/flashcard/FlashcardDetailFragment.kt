package com.example.flashcards.ui.flashcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.flashcards.R

class FlashcardDetailFragment : Fragment() {

    private lateinit var viewModel: FlashcardDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.flashcard_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FlashcardDetailViewModel::class.java)

        val application = requireNotNull(this.activity).application
        val arguments = this.arguments

        // Add observer to state variable
        viewModel.navigate_to_home_state.observe(this, Observer {
            if(it == true) {
                this.findNavController().navigate(R.id.navigation_home)
            }
        })
    }
}