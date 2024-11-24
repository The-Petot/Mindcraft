package com.thepetot.mindcraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.ItemQuizBinding

class QuizHistoryAdapter : ListAdapter<ListQuizItem, QuizHistoryAdapter.QuizViewHolder>(
    DIFF_CALLBACK
) {
    class QuizViewHolder(private val binding: ItemQuizBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: ListQuizItem) {
            binding.tvTitle.text = quiz.title
            binding.tvDescription.text = quiz.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = getItem(position)
        holder.bind(quiz)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListQuizItem>() {
            override fun areItemsTheSame(oldItem: ListQuizItem, newItem: ListQuizItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListQuizItem, newItem: ListQuizItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}