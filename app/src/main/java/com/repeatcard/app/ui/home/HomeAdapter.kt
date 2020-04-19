package com.repeatcard.app.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.repeatcard.app.R
import com.repeatcard.app.db.flashcard.Flashcard

class HomeAdapter(private val clickListener: HomeListener) : ListAdapter<Flashcard, HomeViewHolder>(FlashcardsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.home_row, parent, false)
        return HomeViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val flashcard = getItem(position)
        holder.bind(flashcard)
        holder.layout.setOnClickListener { clickListener.cardClicked(flashcard) }
    }
}

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val layout: ConstraintLayout = view.findViewById(R.id.home_row)
    private val flashcardTitle: TextView = view.findViewById(R.id.textViewHomeRow)
    private val flashcardImage: ImageView = view.findViewById(R.id.imageViewHomeRow)

    fun bind(flashcard: Flashcard) {
        Glide.with(this.itemView.context)
            .load(
                if (flashcard.imageUri.isNullOrEmpty()) this.itemView.context.getDrawable(R.drawable.photography)
                else flashcard.imageUri
            )
            .into(flashcardImage)
        flashcardTitle.text = flashcard.title
    }
}

interface HomeListener {
    fun cardClicked(flashcard: Flashcard)
}

class FlashcardsDiffUtil : DiffUtil.ItemCallback<Flashcard>() {
    override fun areItemsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }
}
