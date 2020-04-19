package it.ilker.repeatcard.ui.directories

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.directory.Directory

class DirectoriesAdapter(private val clickListener: DirectoriesListener) :
    ListAdapter<Directory, DirectoriesViewHolder>(DirectoriesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.directories_row, parent, false)
        return DirectoriesViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DirectoriesViewHolder, position: Int) {
        val directory = getItem(position)
        holder.directory.text = directory.title

        // Set delete button to invisible if default directory
        holder.directoryDelete.visibility = if (holder.directory.text == DEFAULT_DIRECTORY_NAME) INVISIBLE else VISIBLE

        holder.directoryView.setOnClickListener { clickListener.directoryClicked(directory.id) }
        holder.directoryDelete.setOnClickListener { clickListener.itemDeleted(directory.id) }
    }
}

class DirectoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val directory: TextView = view.findViewById(R.id.textViewDirectoriesRow)
    val directoryView: ConstraintLayout = view.findViewById(R.id.directoriesRow)
    val directoryDelete: View = view.findViewById(R.id.deleteButtonDirectoriesRow)
}

interface DirectoriesListener {
    fun itemDeleted(id: Int)
    fun directoryClicked(id: Int)
}

class DirectoriesDiffUtil : DiffUtil.ItemCallback<Directory>() {
    override fun areItemsTheSame(oldItem: Directory, newItem: Directory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Directory, newItem: Directory): Boolean {
        return oldItem.id == newItem.id
    }
}
