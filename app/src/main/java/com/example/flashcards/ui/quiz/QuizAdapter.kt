package com.example.flashcards.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flashcards.R
import com.example.flashcards.db.flashcard.Flashcard
import com.example.flashcards.ui.home.FlashcardsDiffUtil

class QuizAdapter : ListAdapter<Flashcard, QuizSliderViewHolder>(FlashcardsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizSliderViewHolder {
        return QuizSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_quiz_base, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuizSliderViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question)
    }
}

class QuizSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.findViewById<ImageView>(R.id.quizImageView)
    private val questionTitle = view.findViewById<TextView>(R.id.quizQuestionTitle)
    private val option1 = view.findViewById<TextView>(R.id.quizOptionText1)
    private val option2 = view.findViewById<TextView>(R.id.quizOptionText2)
    private val option3 = view.findViewById<TextView>(R.id.quizOptionText3)
    private val option4 = view.findViewById<TextView>(R.id.quizOptionText4)

    fun bind(question: Flashcard) {
        if (question.imageUri != "No image") {
            Glide.with(this.itemView.context).load(question.imageUri).into(image)
        }
        questionTitle.text = question.title
        option1.text = question.description
        option2.text = question.description
        option3.text = question.description
        option4.text = question.description
    }
}
