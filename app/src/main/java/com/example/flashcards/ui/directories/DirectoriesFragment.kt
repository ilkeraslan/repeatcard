package com.example.flashcards.ui.directories

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.flashcards.R
import kotlinx.android.synthetic.main.directories_fragment.*

class DirectoriesFragment : Fragment() {

    companion object {
        fun newInstance() = DirectoriesFragment()
    }

    private lateinit var viewModel: DirectoriesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.directories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DirectoriesViewModel::class.java)

        // LayoutManager and Adapter
        recyclerView_directories.layoutManager = LinearLayoutManager(this.context)
        recyclerView_directories.adapter = DirectoriesAdapter()

        var directories_adapter = (recyclerView_directories.adapter as DirectoriesAdapter)

        // Observer on directories_list variable
        viewModel.directories_list.observe(this, Observer {
            it.let { directories_adapter.directory_titles = it }
        })
    }
}
