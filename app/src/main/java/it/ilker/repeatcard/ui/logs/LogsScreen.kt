package it.ilker.repeatcard.ui.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.notification.Notification
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

class LogsScreen : Fragment() {

    @ExperimentalCoroutinesApi
    private val viewModel: LogsViewModel by inject()
    private lateinit var recyclerView: RecyclerView
    private lateinit var logsAdapter: LogsAdapter
    private lateinit var logsListener: LogsListener
    private lateinit var delete: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        viewModel.send(LogEvent.Load)
        setUpViews(view)
        setUpRecyclerView()
    }

    @ExperimentalCoroutinesApi
    private fun setUpRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recyclerView_notifications)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        logsListener = object : LogsListener {
            override fun itemDeleted(id: Int) {
                alertToDelete(id)
            }
        }
        logsAdapter = LogsAdapter(logsListener)
        recyclerView.adapter = logsAdapter
    }

    @ExperimentalCoroutinesApi
    private fun setUpViews(view: View) {
        delete = view.findViewById(R.id.deleteAllNotifications)
        delete.setOnClickListener { alertToDelete() }
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is LogState.Error -> showError()
                is LogState.Success -> showLogs(state.notifications)
            }
        })
    }

    private fun showError() {
        delete.visibility = INVISIBLE
        logsAdapter.submitList(listOf())
        logsAdapter.notifyDataSetChanged()
    }

    @ExperimentalCoroutinesApi
    private fun showLogs(notifications: List<Notification>) {
        delete.visibility = VISIBLE
        logsAdapter.submitList(notifications)
        logsAdapter.notifyDataSetChanged()
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Are you sure you want to delete ALL?")
        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            viewModel.send(LogEvent.DeleteAll)
        }
        dialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        dialogBuilder.create().show()
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("Delete this notification?")
        dialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            viewModel.send(LogEvent.DeleteLog(id))
        }
        dialogBuilder.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        dialogBuilder.create().show()
    }
}
