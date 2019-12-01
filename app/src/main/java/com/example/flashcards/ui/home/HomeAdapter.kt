package com.example.flashcards.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.ui.flashcard.Flashcard
import com.example.flashcards.ui.flashcard.FlashcardDetailActivity

class HomeAdapter : ListAdapter<Flashcard, HomeViewHolder>(FlashcardsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.home_row, parent, false)
        return HomeViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val flashcard = getItem(position)
        holder.flashcard.text = flashcard.name

        holder.flashcard.setOnClickListener {
            FlashcardDetailActivity.openFlashcardDetailActivity(
                holder.flashcard.context as Activity,
                flashcard.id
            )
        }
    }
}

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val flashcard = view.findViewById<TextView>(R.id.textView_home_row)
}

class HomeListener(val click_listener: (flashcard_id: Int) -> Unit) {
    fun onClick(flashcard: Flashcard) = click_listener(flashcard.id)
}

class FlashcardsDiffUtil : DiffUtil.ItemCallback<Flashcard>() {
    override fun areItemsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem == newItem
    }
}