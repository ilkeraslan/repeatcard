package com.repeatcard.app.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.repeatcard.app.R
import com.repeatcard.app.db.notification.Notification
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject

class NotificationsFragment : Fragment() {

    @ExperimentalCoroutinesApi
    private val viewModel: NotificationsViewModel by inject()
    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter
    private lateinit var notificationsListener: NotificationsListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observe()
        setUpRecyclerView()
        setUpViews()
    }

    @ExperimentalCoroutinesApi
    private fun setUpRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recyclerView_notifications)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        notificationsListener = object : NotificationsListener {
            override fun itemDeleted(id: Int) {
                alertToDelete(id)
            }
        }
        notificationsAdapter = NotificationsAdapter(notificationsListener)
        recyclerView.adapter = notificationsAdapter
    }

    @ExperimentalCoroutinesApi
    private fun setUpViews() {
        val deleteAll: Button = requireActivity().findViewById(R.id.deleteAllNotifications)
        deleteAll.setOnClickListener { alertToDelete() }
    }

    @ExperimentalCoroutinesApi
    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is NotificationState.Error -> showError()
                is NotificationState.Success -> showNotifications(state.notifications)
            }
        })
    }

    private fun showError() {
        notificationsAdapter.notifyDataSetChanged()
        Toast.makeText(context, "No notification yet!", Toast.LENGTH_SHORT).show()
    }

    @ExperimentalCoroutinesApi
    private fun showNotifications(notifications: List<Notification>) {
        notificationsAdapter.submitList(notifications)
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete() {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Are you sure you want to delete ALL?")

        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            viewModel.send(NotificationEvent.DeleteAll)
            Toast.makeText(context, "Deleted all.", Toast.LENGTH_SHORT).show()
        }

        dialogBuilder.setNegativeButton("No") { dialog, which -> viewModel.send(NotificationEvent.Load) }
        dialogBuilder.create().show()
    }

    @ExperimentalCoroutinesApi
    private fun alertToDelete(id: Int) {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Delete this notification?")

        dialogBuilder.setPositiveButton("Yes") { dialog, which ->
            viewModel.send(NotificationEvent.DeleteNotification(id))
            Toast.makeText(context, "Deleted notification.", Toast.LENGTH_SHORT).show()
        }

        dialogBuilder.setNegativeButton("No") { dialog, which -> viewModel.send(NotificationEvent.Load) }
        dialogBuilder.create().show()
    }
}
