package com.example.flashcards.ui.notifications

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.flashcards.R
import kotlinx.android.synthetic.main.notifications_fragment.*

class NotificationsFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationsFragment()
    }

    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notifications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NotificationsViewModel::class.java)

        // LayoutManager and Adapter
        recyclerView_notifications.layoutManager = LinearLayoutManager(this.context)
        recyclerView_notifications.adapter = NotificationsAdapter()

        // Observer on notifications_list variable
        viewModel.notifications_list.observe(this, Observer {
            it.let { (recyclerView_notifications.adapter as NotificationsAdapter).notification_titles = it }
        }

        )
    }

}
