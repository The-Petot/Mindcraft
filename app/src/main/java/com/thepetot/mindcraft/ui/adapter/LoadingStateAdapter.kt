package com.thepetot.mindcraft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }
    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
    class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}

//class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
//        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return LoadingStateViewHolder(binding, retry)
//    }
//
//    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
//        holder.bind(loadState)
//    }
//
//    class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        init {
//            binding.retryButton.setOnClickListener { retry.invoke() }
//        }
//
//        fun bind(loadState: LoadState) {
//            // Disable retry button if no more data to load
//            binding.progressBar.isVisible = loadState is LoadState.Loading
//            val isError = loadState is LoadState.Error
//            binding.retryButton.isVisible = isError
//            binding.errorMsg.isVisible = isError
//
//            // Prevent showing retry if there is no more data to load
//            if (loadState is LoadState.NotLoading && loadState.endOfPaginationReached) {
//                binding.retryButton.isVisible = false
//            }
//        }
//    }
//}