package com.example.flashcards.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flashcards.R
import com.example.flashcards.models.Question

class QuizAdapter : ListAdapter<Question, QuizSliderViewHolder>(QuestionDiffUtil()) {
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

    fun bind(question: Question) {
        if (question.imageUri != "No image") {
            Glide.with(this.itemView.context).load(question.imageUri).into(image)
        }
        questionTitle.text = question.questionText
        option1.text = question.option1
        option2.text = question.option2
        option3.text = question.option3
        option4.text = question.option4
    }
}

class QuestionDiffUtil : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.id == newItem.id
    }
}

