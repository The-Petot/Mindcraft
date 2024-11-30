package com.thepetot.mindcraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.data.dummy.SearchHistoryModel
import com.thepetot.mindcraft.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter : ListAdapter<SearchHistoryModel, SearchHistoryAdapter.SearchHistoryViewHolder>(DIFF_CALLBACK) {

    class SearchHistoryViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: SearchHistoryModel) {
            binding.tvQuery.text = history.query
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchHistoryViewHolder {
        val binding = ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistoryModel>() {
            override fun areItemsTheSame(
                oldItem: SearchHistoryModel,
                newItem: SearchHistoryModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SearchHistoryModel,
                newItem: SearchHistoryModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}