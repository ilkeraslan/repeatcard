package com.example.flashcards.ui.directories

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.db.flashcard_directory.FlashcardDirectory
import com.example.flashcards.ui.directory.DirectoryScreen

class DirectoriesAdapter :
    ListAdapter<FlashcardDirectory, DirectoriesViewHolder>(DirectoriesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.directories_row, parent, false)
        return DirectoriesViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DirectoriesViewHolder, position: Int) {
        val directory = getItem(position)
        holder.directory.text = directory.title

        holder.directoryView.setOnClickListener {
            DirectoryScreen.openDirectoryScreen(
                holder.directory.context as Activity,
                directory.id
            )
        }
    }
}

class DirectoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val directory: TextView = view.findViewById(R.id.textViewDirectoriesRow)
    val directoryView: ConstraintLayout = view.findViewById(R.id.directoriesRow)
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
