package com.thepetot.mindcraft.ui.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.databinding.FragmentRankingBinding
import com.thepetot.mindcraft.ui.adapter.RankingAdapter
import com.thepetot.mindcraft.utils.generateRankingUsers

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rankingAdapter = RankingAdapter()
        binding.rvUser.apply {
            adapter = rankingAdapter
            layoutManager = LinearLayoutManager(context)
        }

        rankingAdapter.submitList(generateRankingUsers())

        binding.tvRank.text = "1"
        binding.imgProfile.setImageResource(R.drawable.img_profile)
        binding.tvName.text = "Admin"
        binding.tvScore.text = "999999"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}