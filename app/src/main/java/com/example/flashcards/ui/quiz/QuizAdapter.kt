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

class QuizAdapter(private val clickListener: QuizListener) : ListAdapter<Question, QuizSliderViewHolder>(QuestionDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizSliderViewHolder {
        return QuizSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_quiz_base, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuizSliderViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question)

        holder.option1.setOnClickListener {
            clickListener.itemSelected(holder.option1, listOf(holder.option2, holder.option3, holder.option4))
        }
        holder.option2.setOnClickListener {
            clickListener.itemSelected(holder.option2, listOf(holder.option1, holder.option3, holder.option4))
        }
        holder.option3.setOnClickListener {
            clickListener.itemSelected(holder.option3, listOf(holder.option1, holder.option2, holder.option4))
        }
        holder.option4.setOnClickListener {
            clickListener.itemSelected(holder.option4, listOf(holder.option1, holder.option2, holder.option3))
        }
    }
}

interface QuizListener {
    fun itemSelected(selectedView: TextView, otherViews: List<TextView>)
}

class QuizSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.findViewById<ImageView>(R.id.quizImageView)
    val option1: TextView = view.findViewById(R.id.quizOptionText1)
    val option2: TextView = view.findViewById(R.id.quizOptionText2)
    val option3: TextView = view.findViewById(R.id.quizOptionText3)
    val option4: TextView = view.findViewById(R.id.quizOptionText4)

    fun bind(question: Question) {
        if (question.imageUri != "No image") {
            Glide.with(this.itemView.context).load(question.imageUri).into(image)
        }
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
