package com.example.flashcards.ui.directories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flashcards.R
import kotlinx.android.synthetic.main.directory_row.view.*

class DirectoriesAdapter : RecyclerView.Adapter<DirectoriesViewHolder>() {

    var directory_titles = listOf("foo", "bar", "baz")

    // Custom setter
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoriesViewHolder {
        val layout_inflater = LayoutInflater.from(parent.context)
        val cell_for_row = layout_inflater.inflate(R.layout.directory_row, parent, false)
        return DirectoriesViewHolder(cell_for_row)
    }

    override fun getItemCount(): Int {
        return directory_titles.size
    }

    override fun onBindViewHolder(holder: DirectoriesViewHolder, position: Int) {
        val directory_titles = directory_titles[position]
        holder.view.textView_directory_row.text = directory_titles
    }
}

class DirectoriesViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}