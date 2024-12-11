package com.thepetot.mindcraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.data.local.database.SearchHistoryEntity
import com.thepetot.mindcraft.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter : ListAdapter<SearchHistoryEntity, SearchHistoryAdapter.SearchHistoryViewHolder>(DIFF_CALLBACK) {

    class SearchHistoryViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: SearchHistoryEntity) {
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchHistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: SearchHistoryEntity,
                newItem: SearchHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SearchHistoryEntity,
                newItem: SearchHistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}