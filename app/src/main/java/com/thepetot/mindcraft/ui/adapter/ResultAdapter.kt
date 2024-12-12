package com.thepetot.mindcraft.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.remote.response.challenges.questions.DataItem
import com.thepetot.mindcraft.databinding.ItemQuizBinding
import com.thepetot.mindcraft.databinding.ItemQuizResultBinding

class ResultAdapter : ListAdapter<DataItem, ResultAdapter.ResultViewHolder>(DIFF_CALLBACK) {

    class ResultViewHolder(private val binding: ItemQuizResultBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(
            question: DataItem,
            position: Int
        ) {
            binding.rbAnswer1.isClickable = false
            binding.rbAnswer2.isClickable = false
            binding.rbAnswer3.isClickable = false
            binding.rbAnswer4.isClickable = false

            binding.tvQuestion.text = question.question
            binding.tvNumber.text = String.format("%d", position + 1)
            binding.rbAnswer1.text = question.answers[0].answer
            binding.rbAnswer2.text = question.answers[1].answer
            binding.rbAnswer3.text = question.answers[2].answer
            binding.rbAnswer4.text = question.answers[3].answer

            val correctOption = question.answers.indexOfFirst { it.correct }
            val selectedOption = question.checked

            if (correctOption == selectedOption) {
                when (selectedOption) {
                    0 -> {
                        binding.rbAnswer1.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                        binding.rbAnswer1.isChecked = true
                        binding.rbAnswer1.setTypeface(null, Typeface.BOLD)
                    }
                    1 -> {
                        binding.rbAnswer2.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                        binding.rbAnswer2.isChecked = true
                        binding.rbAnswer2.setTypeface(null, Typeface.BOLD)
                    }
                    2 -> {
                        binding.rbAnswer3.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                        binding.rbAnswer3.isChecked = true
                        binding.rbAnswer3.setTypeface(null, Typeface.BOLD)
                    }
                    3 -> {
                        binding.rbAnswer4.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                        binding.rbAnswer4.isChecked = true
                        binding.rbAnswer4.setTypeface(null, Typeface.BOLD)
                    }
                }
            }

            if (correctOption != selectedOption) {
                when (selectedOption) {
                    0 -> {
                        binding.rbAnswer1.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner_incorrect)
                        binding.rbAnswer1.isChecked = true
                        binding.rbAnswer1.setTypeface(null, Typeface.BOLD)
                    }
                    1 -> {
                        binding.rbAnswer2.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner_incorrect)
                        binding.rbAnswer2.isChecked = true
                        binding.rbAnswer2.setTypeface(null, Typeface.BOLD)
                    }
                    2 -> {
                        binding.rbAnswer3.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner_incorrect)
                        binding.rbAnswer3.isChecked = true
                        binding.rbAnswer3.setTypeface(null, Typeface.BOLD)
                    }
                    3 -> {
                        binding.rbAnswer4.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner_incorrect)
                        binding.rbAnswer4.isChecked = true
                        binding.rbAnswer4.setTypeface(null, Typeface.BOLD)
                    }
                }
            }

//            when (selectedOption) {
//                0 -> {
//                    binding.rbAnswer1.isChecked = true
//                }
//                1 -> {
//                    binding.rbAnswer2.isChecked = true
//                }
//                2 -> {
//                    binding.rbAnswer3.isChecked = true
//                }
//                3 -> {
//                    binding.rbAnswer4.isChecked = true
//                }
//            }
//
            when (correctOption) {
                0 -> {
                    binding.rbAnswer1.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                    binding.rbAnswer1.setTypeface(null, Typeface.BOLD)
                    binding.rbAnswer1.isChecked = true
                }
                1 -> {
                    binding.rbAnswer2.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                    binding.rbAnswer2.setTypeface(null, Typeface.BOLD)
                    binding.rbAnswer2.isChecked = true
                }
                2 -> {
                    binding.rbAnswer3.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                    binding.rbAnswer3.setTypeface(null, Typeface.BOLD)
                    binding.rbAnswer3.isChecked = true
                }
                3 -> {
                    binding.rbAnswer4.background = AppCompatResources.getDrawable(binding.root.context, R.drawable.rounded_corner)
                    binding.rbAnswer4.setTypeface(null, Typeface.BOLD)
                    binding.rbAnswer4.isChecked = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemQuizResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question, position)
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
