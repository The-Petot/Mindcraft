package com.thepetot.mindcraft.ui.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(
                // Notice we're using systemBars, not statusBar
                WindowInsetsCompat.Type.systemBars()
                        // Notice we're also accounting for the display cutouts
                        or WindowInsetsCompat.Type.displayCutout()
                // If using EditText, also add
                // "or WindowInsetsCompat.Type.ime()"
                // to maintain focus when opening the IME
            )
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

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