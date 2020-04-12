package com.repeatcard.app.ui.results

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.repeatcard.app.R
import com.repeatcard.app.models.question.Question

class ResultsAdapter : ListAdapter<Question, ResultsViewHolder>(AnswerDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.result_row, parent, false)
        return ResultsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val question = getItem(position)
        holder.questionNumber.text = (position + 1).toString()
        holder.questionText.text = question.correctAnswer
        holder.answerText.text = if (question.selectedAnswer.isNullOrEmpty()) "No answer" else question.selectedAnswer
    }
}

class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val questionNumber: TextView = view.findViewById(R.id.questionNumberResultRow)
    val questionText: TextView = view.findViewById(R.id.questionTextResultRow)
    val answerText: TextView = view.findViewById(R.id.answerTextResultRow)
}

class AnswerDiffUtil : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }
}
