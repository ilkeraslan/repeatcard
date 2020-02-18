package com.example.flashcards.ui.notifications

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
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.db.notification.Notification

class NotificationsFragment : Fragment() {

    companion object {
        fun openNotificationsFragment() = NotificationsFragment()
    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        setUpViews()

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = requireActivity().findViewById(R.id.recyclerView_notifications)
        notificationsAdapter = NotificationsAdapter()
        recyclerView.adapter = notificationsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    private fun setUpViews() {
        observe()
    }

    private fun observe() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is NotificationState.Error -> showError(state.error)
                is NotificationState.Success -> showNotifications(state.notifications)
            }
        })
    }

    private fun showError(error: Throwable) {
        Log.i("SHOW_ERROR", "Error: ", error)
        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
    }

    private fun showNotifications(notifications: List<Notification>) {
        notificationsAdapter.submitList(notifications)
    }
}
