package com.example.flashcards.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import kotlinx.android.synthetic.main.notification_row.view.*

class NotificationsAdapter : RecyclerView.Adapter<NotificationsViewHolder>() {

    val notification_titles = listOf<String>("Notification-1", "Notification-2", "Notification-3", "Notification-4")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val layout_inflater = LayoutInflater.from((parent.context))
        val cell_for_row = layout_inflater.inflate(R.layout.notification_row, parent, false)
        return NotificationsViewHolder(cell_for_row)
    }

    override fun getItemCount(): Int {
        return notification_titles.size
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notification_titles = notification_titles[position]
        holder.view.textView_notification_row.text = notification_titles
    }

}

class NotificationsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}