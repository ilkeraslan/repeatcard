package com.repeatcard.app.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.repeatcard.app.R
import com.repeatcard.app.db.notification.Notification

class NotificationsAdapter(private val clickListener: NotificationsListener) : ListAdapter<Notification, NotificationsViewHolder>(NotificationsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val layoutInflater = LayoutInflater.from((parent.context))
        val cellForRow = layoutInflater.inflate(R.layout.notification_row, parent, false)
        return NotificationsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notification = getItem(position)
        holder.title.text = notification.notificationTitle
        holder.date.text = notification.creationDate

        holder.cancel.setOnClickListener { clickListener.itemDeleted(notification.notificationId) }
    }
}

class NotificationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cancel: ImageView = view.findViewById(R.id.imageView_notification_row)
    val title: TextView = view.findViewById(R.id.titleText_notification_row)
    val date: TextView = view.findViewById(R.id.dateText_notification_row)
}

interface NotificationsListener {
    fun itemDeleted(id: Int)
}

class NotificationsDiffUtil : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }
}
