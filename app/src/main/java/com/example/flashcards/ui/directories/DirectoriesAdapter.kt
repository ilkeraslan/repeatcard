package com.example.flashcards.ui.directories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.db.flashcard_directory.FlashcardDirectory

class DirectoriesAdapter :
    ListAdapter<FlashcardDirectory, DirectoriesViewHolder>(DirectoriesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.directory_row, parent, false)
        return DirectoriesViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DirectoriesViewHolder, position: Int) {
        val directoryTitle = getItem(position)
        holder.directory.text = directoryTitle.title
    }
}

class DirectoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val directory: TextView = view.findViewById(R.id.textView_directory_row)
}

class DirectoriesDiffUtil : DiffUtil.ItemCallback<FlashcardDirectory>() {
    override fun areItemsTheSame(
        oldItem: FlashcardDirectory,
        newItem: FlashcardDirectory
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FlashcardDirectory,
        newItem: FlashcardDirectory
    ): Boolean {
        return oldItem.id == newItem.id
    }
}