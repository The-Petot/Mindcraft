package com.thepetot.mindcraft.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.data.remote.response.ListQuestionsItem
import com.thepetot.mindcraft.data.remote.response.challenges.questions.DataItem

import com.thepetot.mindcraft.databinding.ItemQuizBinding

class QuestionsAdapter : ListAdapter<DataItem, QuestionsAdapter.QuestionsViewHolder>(DIFF_CALLBACK) {

    private val selectedAnswers = mutableMapOf<Int, Boolean>() // Map to store user's selected answers

    class QuestionsViewHolder(private val binding: ItemQuizBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(
            question: DataItem,
            position: Int,
            onAnswerSelected: (Int, Boolean) -> Unit
        ) {
            binding.tvQuestion.text = question.question
            binding.tvNumber.text = String.format("%d", position + 1)
            binding.rbAnswer1.text = question.answers[0].answer
            binding.rbAnswer2.text = question.answers[1].answer
            binding.rbAnswer3.text = question.answers[2].answer
            binding.rbAnswer4.text = question.answers[3].answer

            // Set up RadioGroup listener
            binding.rgAnswer.setOnCheckedChangeListener { _, checkedId ->
                val selectedAnswer = when (checkedId) {
                    binding.rbAnswer1.id -> question.answers[0].correct
                    binding.rbAnswer2.id -> question.answers[1].correct
                    binding.rbAnswer3.id -> question.answers[2].correct
                    binding.rbAnswer4.id -> question.answers[3].correct
                    else -> false
                }
                onAnswerSelected(position, selectedAnswer) // Pass selected answer to the activity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question, position) { pos, answer ->
            selectedAnswers[pos] = answer // Save the selected answer
        }
    }

    fun getSelectedAnswers(): Map<Int, Boolean> {
        return selectedAnswers
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataItem,
                newItem: DataItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}