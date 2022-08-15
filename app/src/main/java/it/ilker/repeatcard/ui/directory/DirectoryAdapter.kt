package it.ilker.repeatcard.ui.directory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.ilker.repeatcard.R
import it.ilker.repeatcard.ui.flashcarddetail.FlashcardDetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class DirectoryAdapter(
    private val clickListener: DirectoryListener,
    private val flashcards: List<me.ilker.business.flashcard.Flashcard>
) : RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.directory_row, parent, false)
        return DirectoryViewHolder(cellForRow)
    }

    override fun getItemCount() = flashcards.size

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {
        val flashcard = flashcards[position]

        Glide.with(holder.itemView.context)
            .load(
                if (flashcard.imageUri.isNullOrEmpty()) {
                    holder.itemView.context.getDrawable(R.drawable.photography)
                } else {
                    flashcard.imageUri
                }
            )
            .into(holder.flashcardImage)

        holder.flashcardTitle.text = flashcard.title

        holder.delete.setOnClickListener { clickListener.itemDeleted(flashcard) }
        holder.edit.setOnClickListener { clickListener.itemEdit(flashcard) }
        holder.flashcardTitle.setOnClickListener {
            FlashcardDetailActivity.openFlashcardDetailActivity(
                context = holder.itemView.context,
                flashcardId = flashcard.id
            )
        }
    }

    class DirectoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flashcardImage: ImageView = view.findViewById(R.id.imageViewDirectoryRow)
        val flashcardTitle: TextView = view.findViewById(R.id.textViewDirectoryRow)
        val edit: Button = view.findViewById(R.id.editDirectoryRow)
        val delete: Button = view.findViewById(R.id.deleteButtonDirectoryRow)
    }
}
