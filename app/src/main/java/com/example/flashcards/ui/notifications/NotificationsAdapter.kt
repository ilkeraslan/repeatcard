package com.example.flashcards.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.db.notification.Notification
import kotlinx.android.synthetic.main.notification_row.view.*

class NotificationsAdapter :
    ListAdapter<Notification, NotificationsViewHolder>(NotificationsDiffUtil()) {

    var notification_titles = listOf<Flashcard>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val layout_inflater = LayoutInflater.from((parent.context))
        val cell_for_row = layout_inflater.inflate(R.layout.notification_row, parent, false)
        return NotificationsViewHolder(cell_for_row)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val notification_titles = notification_titles[position]
        holder.view.textView_notification_row.text = notification_titles.title
    }
}

class NotificationsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

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
