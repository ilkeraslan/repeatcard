package com.repeatcard.app.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.repeatcard.app.R
import com.repeatcard.app.db.flashcard.Flashcard
import com.repeatcard.app.ui.flashcarddetail.FlashcardDetailActivity

class HomeAdapter(private val clickListener: HomeListener) : ListAdapter<Flashcard, HomeViewHolder>(FlashcardsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.home_row, parent, false)
        return HomeViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val flashcard = getItem(position)
        holder.flashcard.text = flashcard.title

        holder.flashcard.setOnClickListener {
            FlashcardDetailActivity.openFlashcardDetailActivity(
                holder.flashcard.context as Activity,
                flashcard.id
            )
        }

        holder.addFlashcardToDirectory.setOnClickListener {
            clickListener.addFlashcardToDirectory(flashcard.id)
        }

        holder.flashcardDelete.setOnClickListener { clickListener.itemDeleted(flashcard.id) }
    }
}

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val flashcard: TextView = view.findViewById(R.id.textViewHomeRow)
    val addFlashcardToDirectory: View = view.findViewById(R.id.addToDirectoryButtonHomeRow)
    val flashcardDelete: View = view.findViewById(R.id.deleteButtonHomeRow)
}

interface HomeListener {
    fun itemDeleted(id: Int)
    fun addFlashcardToDirectory(id: Int)
}

class FlashcardsDiffUtil : DiffUtil.ItemCallback<Flashcard>() {
    override fun areItemsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }
}
