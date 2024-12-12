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

    private val selectedAnswers: MutableMap<Int, Int?> = mutableMapOf(
        0 to null,
        1 to null,
        2 to null,
        3 to null,
        4 to null
    ) // Map to store user's selected answers

    class QuestionsViewHolder(private val binding: ItemQuizBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(
            question: DataItem,
            position: Int,
            onAnswerSelected: (Int, Int?) -> Unit
        ) {
            binding.tvQuestion.text = question.question
            binding.tvNumber.text = String.format("%d", position + 1)
            binding.rbAnswer1.text = question.answers[0].answer
            binding.rbAnswer2.text = question.answers[1].answer
            binding.rbAnswer3.text = question.answers[2].answer
            binding.rbAnswer4.text = question.answers[3].answer

//            when (question.checked) {
//                0 -> binding.rbAnswer1.isChecked = true
//                1 -> binding.rbAnswer2.isChecked = true
//                2 -> binding.rbAnswer3.isChecked = true
//                3 -> binding.rbAnswer4.isChecked = true
//            }

            // Set up RadioGroup listener
            binding.rgAnswer.setOnCheckedChangeListener { _, checkedId ->
                var option: Int? = null
                var correct = false
                when (checkedId) {
                    binding.rbAnswer1.id -> {
                        correct = question.answers[0].correct
                        option = 0
                    }
                    binding.rbAnswer2.id -> {
                        correct = question.answers[1].correct
                        option = 1
                    }
                    binding.rbAnswer3.id -> {
                        correct = question.answers[2].correct
                        option = 2
                    }
                    binding.rbAnswer4.id -> {
                        correct = question.answers[3].correct
                        option = 3
                    }
                }
                onAnswerSelected(position, option) // Pass selected answer to the activity
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

    fun getSelectedAnswers(): Map<Int, Int?> {
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