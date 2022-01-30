package it.ilker.repeatcard.ui.results

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.ilker.repeatcard.R
import me.ilker.business.question.Question

class ResultsAdapter(private val clickListener: ResultListener) : ListAdapter<Question, ResultsViewHolder>(QuestionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.result_row, parent, false)
        return ResultsViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val question = getItem(holder.adapterPosition)
        holder.questionNumber.text = (holder.adapterPosition + 1).toString()
        holder.questionText.text = question.answer
        holder.answerText.text = if (question.selectedAnswer.isNullOrEmpty()) "No answer" else question.selectedAnswer

        holder.card.setOnClickListener { clickListener.showResultDetails(question) }
    }
}

class ResultsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val card: CardView = view.findViewById(R.id.cardResultRow)
    val questionNumber: TextView = view.findViewById(R.id.questionNumberResultRow)
    val questionText: TextView = view.findViewById(R.id.questionTextResultRow)
    val answerText: TextView = view.findViewById(R.id.answerTextResultRow)
}

interface ResultListener {
    fun showResultDetails(question: Question)
}

class QuestionDiffUtil : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }
}
