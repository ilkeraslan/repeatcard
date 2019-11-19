package com.example.flashcards.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import com.example.flashcards.ui.flashcard.Flashcard
import kotlinx.android.synthetic.main.home_row.view.*

class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {

    var flashcards = listOf<Flashcard>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layout_inflater = LayoutInflater.from(parent.context)
        val cell_for_row = layout_inflater.inflate(R.layout.home_row, parent, false)
        return HomeViewHolder(cell_for_row)
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val flashcard_title = flashcards[position]
        holder.view.textView_home_row.text = flashcard_title.name
        holder.view.setOnClickListener { flashcard_title }
    }
}

class HomeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}

class HomeListener(val click_listener: (flashcard_id: Int) -> Unit) {
    fun onClick(flashcard: Flashcard) = click_listener(flashcard.id)
}