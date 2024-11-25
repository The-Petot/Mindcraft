package com.thepetot.mindcraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.ItemQuizHistoryBinding


class QuizHistoryAdapter(
    private val listener: OnQuizClickListener
) : ListAdapter<ListQuizItem, QuizHistoryAdapter.QuizViewHolder>(
    DIFF_CALLBACK
) {

    interface OnQuizClickListener {
        fun onQuizClicked(quiz: ListQuizItem)
    }

    class QuizViewHolder(
        private val binding: ItemQuizHistoryBinding,
        private val listener: OnQuizClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: ListQuizItem) {
            binding.tvTitle.text = quiz.title
            binding.tvDescription.text = quiz.description

            binding.root.setOnClickListener {
                listener.onQuizClicked(quiz)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = ItemQuizHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding, listener)
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