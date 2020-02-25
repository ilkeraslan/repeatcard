package com.example.flashcards.ui.notifications

import android.os.Bundle
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
import com.example.flashcards.R
import com.example.flashcards.db.notification.Notification
import kotlinx.coroutines.ExperimentalCoroutinesApi

class NotificationsFragment : Fragment() {

    companion object {
        fun openNotificationsFragment() = NotificationsFragment()
    }

    @ExperimentalCoroutinesApi
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        observe()
        setUpRecyclerView()
        setUpViews()
    }

    private fun setUpRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recyclerView_notifications)
        notificationsAdapter = NotificationsAdapter()
        recyclerView.adapter = notificationsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
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
                is NotificationState.Error -> showError(state.error)
                is NotificationState.Success -> showNotifications(state.notifications)
            }
        })
    }

    private fun showError(error: Throwable) {
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
            viewModel.send(
                NotificationEvent.DeleteAll
            )
            Toast.makeText(context, "Deleted all.", Toast.LENGTH_SHORT).show()
        }

        dialogBuilder.setNegativeButton("No") { dialog, which ->
            viewModel.send(NotificationEvent.Load)
        }

        dialogBuilder.create().show()
    }
}
