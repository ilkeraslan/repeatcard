package it.ilker.repeatcard.ui.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.ilker.repeatcard.R
import it.ilker.repeatcard.db.flashcard.Flashcard
import it.ilker.repeatcard.ui.home.FlashcardsDiffUtil

class FlashcardReviewAdapter : ListAdapter<Flashcard, FlashcardSliderViewHolder>(FlashcardsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardSliderViewHolder {
        return FlashcardSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.flashcard_review_base_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FlashcardSliderViewHolder, position: Int) {
        val flashcard = getItem(position)
        holder.bind(flashcard)
    }
}

class FlashcardSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.findViewById<ImageView>(R.id.reviewImageView)
    private val textTitle = view.findViewById<TextView>(R.id.reviewTitle)
    private val textDescription = view.findViewById<TextView>(R.id.reviewDescription)

    fun bind(flashcard: Flashcard) {
        Glide.with(this.itemView.context)
            .load(
                if (flashcard.imageUri.isNullOrEmpty()) this.itemView.context.getDrawable(R.drawable.photography)
                else flashcard.imageUri
            )
            .into(image)
        textTitle.text = flashcard.title
        textDescription.text = flashcard.description
    }
}
