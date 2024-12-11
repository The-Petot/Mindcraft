package com.thepetot.mindcraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.databinding.ItemQuizHistoryBinding


class ChallengesAdapter(
    private val listener: OnQuizClickListener
) : PagingDataAdapter<DataItem, ChallengesAdapter.QuizViewHolder>(
    DIFF_CALLBACK
) {

    interface OnQuizClickListener {
        fun onQuizClicked(quiz: DataItem)
    }

    class QuizViewHolder(
        private val binding: ItemQuizHistoryBinding,
        private val listener: OnQuizClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quiz: DataItem) {
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
        if (quiz != null) holder.bind(quiz)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}