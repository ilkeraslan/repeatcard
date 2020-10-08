package it.ilker.repeatcard.ui.logs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.notification.Notification

class LogsAdapter(private val clickListener: LogsListener) : ListAdapter<Notification, LogsViewHolder>(LogsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.notification_row, parent, false)
        return LogsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val log = getItem(holder.adapterPosition)
        holder.title.text = log.notificationTitle
        holder.date.text = log.creationDate
        holder.cancel.setOnClickListener { clickListener.itemDeleted(log.notificationId) }
    }
}

class LogsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cancel: ImageView = view.findViewById(R.id.imageView_notification_row)
    val title: TextView = view.findViewById(R.id.titleText_notification_row)
    val date: TextView = view.findViewById(R.id.dateText_notification_row)
}

interface LogsListener {
    fun itemDeleted(id: Int)
}

class LogsDiffUtil : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notificationId == newItem.notificationId
    }
}
