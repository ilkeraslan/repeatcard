package com.repeatcard.app.ui.directories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.repeatcard.app.R
import com.repeatcard.app.db.directory.Directory
import com.repeatcard.app.ui.AppNavigator
import com.repeatcard.app.ui.notifications.NotificationEvent
import com.repeatcard.app.ui.notifications.NotificationsViewModel
import com.repeatcard.app.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

const val ADD_DIRECTORY_INTENT = 3000

class DirectoriesFragment : Fragment() {

    @ExperimentalCoroutinesApi
    private val notificationsViewModel: NotificationsViewModel by inject()
    private val directoriesViewModel: DirectoriesViewModel by inject()
    private val navigator: AppNavigator by inject()

    private lateinit var directoriesAdapter: DirectoriesAdapter
    private lateinit var directoriesListener: DirectoriesListener
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.directories_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViews()
        observeViewModel()
    }

    @ExperimentalCoroutinesApi
    private fun setupRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recyclerView_directories)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        directoriesListener = object : DirectoriesListener {
            override fun itemDeleted(id: Int) {
                alertToDelete(id)
            }

            override fun directoryClicked(id: Int) {
                navigator.goToDirectory(id)
            }
        }
        directoriesAdapter = DirectoriesAdapter(directoriesListener)
        recyclerView.adapter = directoriesAdapter
    }

    private fun setupViews() {
        val addDirectoryButton: FloatingActionButton = requireActivity().findViewById(R.id.add_directory_button)

        addDirectoryButton.setOnClickListener {
            val intent = Intent(activity, AddDirectoryScreen::class.java)
            startActivityForResult(intent, ADD_DIRECTORY_INTENT)
        }
    }

    private fun observeViewModel() {
        directoriesViewModel.directoriesState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is DirectoriesState.Success -> showDirectories(state.directories)
                is DirectoriesState.Error -> { /* Do nothing here */
                }
            }.exhaustive
        })
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Delete this directory?")

        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            directoriesViewModel.send(DirectoriesEvent.DeleteDirectories(id))
            notificationsViewModel.send(NotificationEvent.DeleteDirectory(id))
            Toast.makeText(context, "Deleted directory.", Toast.LENGTH_SHORT).show()
        }

        dialogBuilder.setNegativeButton("No") { dialog, which -> directoriesViewModel.send(DirectoriesEvent.Load) }
        dialogBuilder.create().show()
    }

    @ExperimentalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_DIRECTORY_INTENT && resultCode == Activity.RESULT_OK && data != null) {
            val directory = Directory(
                id = 0,
                title = data.getStringExtra("ADD_DIRECTORY_TITLE_RESULT")!!.toString(),
                creationDate = OffsetDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
            )
            directoriesViewModel.send(DirectoriesEvent.AddDirectories(directory))
            notificationsViewModel.send(NotificationEvent.AddDirectory(directory))
        }
    }

    private fun showDirectories(directories: List<Directory>) {
        directoriesAdapter.submitList(directories)
    }
}
