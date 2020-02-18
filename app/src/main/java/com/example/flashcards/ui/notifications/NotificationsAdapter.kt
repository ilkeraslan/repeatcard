package com.example.flashcards.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.notification.Notification

class NotificationsAdapter :
    ListAdapter<Notification, NotificationsViewHolder>(NotificationsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val layoutInflater = LayoutInflater.from((parent.context))
        val cellForRow = layoutInflater.inflate(R.layout.notification_row, parent, false)
        return NotificationsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notificationTitle = getItem(position)
        holder.title.text = notificationTitle.notificationTitle
        holder.date.text = notificationTitle.creationDate
    }
}

class NotificationsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.imageView_notification_row)
    val title: TextView = view.findViewById(R.id.titleText_notification_row)
    val date: TextView = view.findViewById(R.id.dateText_notification_row)
}

class NotificationsListener(val clickListener: (flashcard_id: Int) -> Unit) {
    fun onClick(flashcard: Flashcard) = clickListener(flashcard.id)
}

class NotificationsDiffUtil : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }
}
