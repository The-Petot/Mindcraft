package com.thepetot.mindcraft.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.dummy.RankingUserModel
import com.thepetot.mindcraft.data.remote.response.user.all_users.DataItem
import com.thepetot.mindcraft.databinding.ItemUserBinding

class RankingAdapter : ListAdapter<DataItem, RankingAdapter.RankingViewHolder>(DIFF_CALLBACK) {

    class RankingViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(users: DataItem) {
            binding.tvRank.text = String.format("%d", users.currentRank)
//            binding.imgProfile.setImageResource(R.drawable.img_profile)
            Glide
                .with(binding.root.context)
                .load(users.profileImgUrl)
                .placeholder(R.drawable.img_profile)
                .error(R.drawable.img_profile)
                .into(binding.imgProfile)
            binding.tvName.text = String.format("%s %s", users.firstName, users.lastName)
            binding.tvScore.text = users.totalScore.toString()
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