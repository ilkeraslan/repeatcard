package com.example.flashcards.ui.directories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.flashcards.R
import com.example.flashcards.db.flashcard_directory.FlashcardDirectory
import kotlinx.android.synthetic.main.directories_fragment.*

class DirectoriesFragment : Fragment() {

    companion object {
        fun newInstance() = DirectoriesFragment()
    }

    private lateinit var viewModel: DirectoriesViewModel
    private lateinit var directoriesAdapter: DirectoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.directories_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(DirectoriesViewModel::class.java)

        // LayoutManager and Adapter
        recyclerView_directories.layoutManager = LinearLayoutManager(this.context)
        directoriesAdapter = DirectoriesAdapter()
        recyclerView_directories.adapter = directoriesAdapter

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is DirectoryState.Error -> showError(state.error)
                is DirectoryState.Success -> showDirectories(state.directories)
            }
        })
    }

    private fun showDirectories(directories: List<FlashcardDirectory>) {
        directoriesAdapter.submitList(directories)
    }

    private fun showError(error: Throwable) {
        Log.i("STATE ERROR", "Error: ", error)
        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
    }
}
