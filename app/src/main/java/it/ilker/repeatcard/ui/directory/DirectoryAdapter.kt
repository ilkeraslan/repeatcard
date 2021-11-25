package it.ilker.repeatcard.ui.directory

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailActivity

class DirectoryAdapter(private val clickListener: DirectoryListener) : ListAdapter<Flashcard, DirectoryViewHolder>(DirectoryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.directory_row, parent, false)
        return DirectoryViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {
        val flashcard = getItem(holder.adapterPosition)
        holder.bind(flashcard)
        holder.delete.setOnClickListener { clickListener.itemDeleted(flashcard) }
        holder.edit.setOnClickListener { clickListener.itemEdit(flashcard) }
        holder.flashcardTitle.setOnClickListener {
            FlashcardDetailActivity.openFlashcardDetailActivity(holder.flashcardTitle.context as Activity, flashcard.id)
        }
    }
}

class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val flashcardImage: ImageView = view.findViewById(R.id.imageViewDirectoryRow)
    val flashcardTitle: TextView = view.findViewById(R.id.textViewDirectoryRow)
    val edit: Button = view.findViewById(R.id.editDirectoryRow)
    val delete: Button = view.findViewById(R.id.deleteButtonDirectoryRow)

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

interface DirectoryListener {
    fun itemDeleted(flashcard: Flashcard)
    fun itemEdit(flashcard: Flashcard)
}

class DirectoryDiffUtil : DiffUtil.ItemCallback<Flashcard>() {
    override fun areItemsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Flashcard, newItem: Flashcard): Boolean {
        return oldItem.id == newItem.id
    }
}
