package com.thepetot.mindcraft.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.data.remote.response.ListQuestionsItem
import com.thepetot.mindcraft.databinding.ItemQuizBinding

class QuestionsAdapter : ListAdapter<ListQuestionsItem, QuestionsAdapter.QuestionsViewHolder>(DIFF_CALLBACK) {

    private val selectedAnswers = mutableMapOf<Int, String>() // Map to store user's selected answers

    class QuestionsViewHolder(private val binding: ItemQuizBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(
            question: ListQuestionsItem,
            position: Int,
            onAnswerSelected: (Int, String) -> Unit
        ) {
            binding.tvQuestion.text = question.question
            binding.tvNumber.text = String.format("%d", position + 1)
            binding.rbAnswer1.text = question.options.a
            binding.rbAnswer2.text = question.options.b
            binding.rbAnswer3.text = question.options.c
            binding.rbAnswer4.text = question.options.d

            // Set up RadioGroup listener
            binding.rgAnswer.setOnCheckedChangeListener { _, checkedId ->
                val selectedAnswer = when (checkedId) {
                    binding.rbAnswer1.id -> "A"
                    binding.rbAnswer2.id -> "B"
                    binding.rbAnswer3.id -> "C"
                    binding.rbAnswer4.id -> "D"
                    else -> ""
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

    fun getSelectedAnswers(): Map<Int, String> {
        return selectedAnswers
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListQuestionsItem>() {
            override fun areItemsTheSame(
                oldItem: ListQuestionsItem,
                newItem: ListQuestionsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListQuestionsItem,
                newItem: ListQuestionsItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}