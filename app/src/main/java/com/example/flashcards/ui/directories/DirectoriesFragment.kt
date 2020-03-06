package com.example.flashcards.ui.directories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flashcards.R
import com.example.flashcards.db.directory.Directory
import com.example.flashcards.ui.notifications.NotificationEvent
import com.example.flashcards.ui.notifications.NotificationsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.directories_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class DirectoriesFragment : Fragment() {

    companion object {
        fun newInstance() = DirectoriesFragment()
    }

    private lateinit var directoriesViewModel: DirectoriesViewModel

    @ExperimentalCoroutinesApi
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var directoriesAdapter: DirectoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.directories_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        directoriesViewModel = ViewModelProvider(this).get(DirectoriesViewModel::class.java)
        notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        recyclerViewDirectory.layoutManager = LinearLayoutManager(this.context)
        directoriesAdapter = DirectoriesAdapter()
        recyclerViewDirectory.adapter = directoriesAdapter

        observeViewModel()
        setupViews()
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                val directory = Directory(
                    id = 0,
                    title = data.getStringExtra("ADD_DIRECTORY_TITLE_RESULT")!!.toString(),
                    creationDate = OffsetDateTime.now().format(
                        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                    )
                )
                directoriesViewModel.send(DirectoryEvent.AddDirectory(directory))
                notificationsViewModel.send(NotificationEvent.AddDirectory(directory))
            } else {
                Toast.makeText(context, "Error, no data.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Error, please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViews() {
        val addDirectoryButton: FloatingActionButton =
            requireActivity().findViewById(R.id.add_directory_button)

        addDirectoryButton.setOnClickListener {
            val intent = Intent(activity, AddDirectoryScreen::class.java)
            startActivityForResult(intent, 2000)
        }
    }

    private fun observeViewModel() {
        directoriesViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is DirectoryState.Error -> showError(state.error)
                is DirectoryState.Success -> showDirectories(state.directories)
            }
        })
    }

    private fun showDirectories(directories: List<Directory>) {
        directoriesAdapter.submitList(directories)
    }

    private fun showError(error: Throwable) {
        Log.i("STATE ERROR", "Error: ", error)
        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
    }
}
