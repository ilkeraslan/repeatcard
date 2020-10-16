package it.ilker.repeatcard.ui.directories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.directory.Directory
import it.ilker.repeatcard.ui.AppNavigator
import it.ilker.repeatcard.ui.util.exhaustive
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber

const val ADD_DIRECTORY_INTENT = 3000

class DirectoriesFragment : Fragment() {

    @ExperimentalCoroutinesApi
    private val viewModel: DirectoriesViewModel by inject()
    private val navigator: AppNavigator by inject()

    private lateinit var directoriesAdapter: DirectoriesAdapter
    private lateinit var directoriesListener: DirectoriesListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var addDirectoryButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.directories_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        viewModel.send(DirectoriesEvent.Load)
        setupRecyclerView()
        setupViews(view)
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

    private fun setupViews(view: View) {
        addDirectoryButton = view.findViewById(R.id.add_directory_button)
        addDirectoryButton.setOnClickListener {
            val intent = Intent(activity, AddDirectoryScreen::class.java)
            startActivityForResult(intent, ADD_DIRECTORY_INTENT)
        }
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.directoriesState.collect { state ->
                when (state) {
                    is DirectoriesState.Loading -> {}
                    is DirectoriesState.Success -> showDirectories(state.directories)
                    is DirectoriesState.Error -> Timber.d(Error())
                }.exhaustive
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Delete this directory?")
        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            viewModel.send(DirectoriesEvent.DeleteDirectories(id))
        }
        dialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
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
            viewModel.send(DirectoriesEvent.AddDirectories(directory))
        }
    }

    private fun showDirectories(directories: List<Directory>) {
        directoriesAdapter.submitList(directories)
        directoriesAdapter.notifyDataSetChanged()
    }
}
