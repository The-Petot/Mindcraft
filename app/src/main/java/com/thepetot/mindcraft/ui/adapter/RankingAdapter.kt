package com.thepetot.mindcraft.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.dummy.RankingUserModel
import com.thepetot.mindcraft.databinding.ItemUserBinding

class RankingAdapter : ListAdapter<RankingUserModel, RankingAdapter.RankingViewHolder>(DIFF_CALLBACK) {

    class RankingViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(ranking: RankingUserModel) {
            binding.tvRank.text = String.format("%d", ranking.rank)
            binding.imgProfile.setImageResource(R.drawable.img_profile)
            binding.tvName.text = "Dummy user"
            binding.tvScore.text = "9999"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val ranking = getItem(position)
        holder.bind(ranking)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RankingUserModel>() {
            override fun areItemsTheSame(
                oldItem: RankingUserModel,
                newItem: RankingUserModel
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun areContentsTheSame(
                oldItem: RankingUserModel,
                newItem: RankingUserModel
            ): Boolean {
                TODO("Not yet implemented")
            }


        }
    }
}