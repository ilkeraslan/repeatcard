package com.repeatcard.app.ui.directory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.repeatcard.app.R
import com.repeatcard.app.db.flashcard.Flashcard

class DirectoryAdapter(private val clickListener: DirectoryListener) :
    ListAdapter<Flashcard, DirectoryViewHolder>(DirectoryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.directory_row, parent, false)
        return DirectoryViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {
        val flashcard = getItem(position)
        holder.flashcard.text = flashcard.title
        holder.delete.setOnClickListener { clickListener.itemDeleted(flashcard.id) }
    }
}

class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val flashcard: TextView = view.findViewById(R.id.textViewDirectoryRow)
    val delete: Button = view.findViewById(R.id.deleteButtonDirectoryRow)
}

interface DirectoryListener {
    fun itemDeleted(id: Int)
}

class DirectoryDiffUtil : DiffUtil.ItemCallback<Flashcard>() {
    override fun areItemsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }
}
