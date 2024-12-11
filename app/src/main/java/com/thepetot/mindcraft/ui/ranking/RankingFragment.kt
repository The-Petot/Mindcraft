package com.thepetot.mindcraft.ui.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.databinding.FragmentRankingBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.adapter.RankingAdapter
import com.thepetot.mindcraft.ui.home.quiz.add.AddQuizViewModel
import com.thepetot.mindcraft.utils.Result
import kotlinx.coroutines.launch

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddQuizViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

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

        val density = resources.displayMetrics.density
        val insetStart = (10 * density).toInt() // 50dp converted to pixels
        val insetEnd = (10 * density).toInt()

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.isLastItemDecorated = false
        divider.dividerInsetStart = insetStart
        divider.dividerInsetEnd = insetEnd
        val rankingAdapter = RankingAdapter()
        binding.rvUser.apply {
            adapter = rankingAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(divider)
        }

        val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
        viewModel.getUserById(userId).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.scrollView.visibility = View.VISIBLE
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.INVISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.scrollView.visibility = View.VISIBLE
                    val rank = result.data.data.currentRank
                    val score = result.data.data.totalScore
                    viewModel.setPreferenceSettings(UserPreference.CURRENT_RANK_KEY, rank)
                    viewModel.setPreferenceSettings(UserPreference.TOTAL_SCORE_KEY, score)
                    binding.tvRank.text = rank.toString()
                    binding.tvScore.text = score.toString()
                }
            }
        }

        viewModel.getAllUsers().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.scrollView.visibility = View.VISIBLE
                    binding.notify.visibility = View.VISIBLE
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.INVISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.scrollView.visibility = View.VISIBLE
                    val users = result.data.data.filter {
                        it.currentRank > 0
                    }.sortedBy {
                        it.currentRank
                    }.take(25)
                    rankingAdapter.submitList(users)
                }
            }
        }

//        rankingAdapter.submitList(generateRankingUsers())

        lifecycleScope.launch {
            val rank = viewModel.getPreferenceSettings(UserPreference.CURRENT_RANK_KEY, 0)
            val img = viewModel.getPreferenceSettings(UserPreference.PROFILE_PICTURE_KEY, "")
            val firstName = viewModel.getPreferenceSettings(UserPreference.FIRST_NAME_KEY, "")
            val lastName = viewModel.getPreferenceSettings(UserPreference.LAST_NAME_KEY, "")
            val score = viewModel.getPreferenceSettings(UserPreference.TOTAL_SCORE_KEY, 0)

            binding.tvRank.text = rank.toString()
            Glide
                .with(binding.root.context)
                .load(img)
                .placeholder(R.drawable.img_profile)
                .error(R.drawable.img_profile)
                .into(binding.imgProfile)
            binding.tvName.text = String.format("%s %s", firstName, lastName)
            binding.tvScore.text = score.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}