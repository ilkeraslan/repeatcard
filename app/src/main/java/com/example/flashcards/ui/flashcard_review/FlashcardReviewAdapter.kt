package com.example.flashcards.ui.flashcard_review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard

class FlashcardReviewAdapter(private val flashcards: List<Flashcard>) :
    RecyclerView.Adapter<FlashcardReviewAdapter.FlashcardSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardSliderViewHolder {
        return FlashcardSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.flashcard_review_base_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }

    override fun onBindViewHolder(holder: FlashcardSliderViewHolder, position: Int) {
        holder.bind(flashcards[position])
    }

    inner class FlashcardSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textTitle = view.findViewById<TextView>(R.id.flashcard_review_title)
        private val textDescription = view.findViewById<TextView>(R.id.flashcard_review_description)

        fun bind(flashcard: Flashcard) {
            textTitle.text = flashcard.title
            textDescription.text = flashcard.description
        }
    }
}
