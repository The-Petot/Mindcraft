package com.thepetot.mindcraft.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.FragmentHomeBinding
import com.thepetot.mindcraft.ui.adapter.QuizHistoryAdapter
import com.thepetot.mindcraft.ui.adapter.SearchHistoryAdapter
import com.thepetot.mindcraft.ui.home.quiz.add.AddQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.utils.generateDummyQuizItems
import com.thepetot.mindcraft.utils.generateSearchHistoryDummy

class HomeFragment : Fragment(), QuizHistoryAdapter.OnQuizClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        binding.searchView.setStatusBarSpacerEnabled(false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupSearchView()
        setupAppBar()
        setupSearchHistory()
        setupQuizHistory()
        setupButton()
    }

    private fun setupSearchHistory() {
        searchHistoryAdapter = SearchHistoryAdapter()
        binding.rvSearchHistory.apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }

        searchHistoryAdapter.submitList(generateSearchHistoryDummy())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQuizClicked(quiz: ListQuizItem) {
        val quizDetailIntent = Intent(context, DetailQuizActivity::class.java)
        quizDetailIntent.putExtra(QUIZ_EXTRA, quiz)
        startActivity(quizDetailIntent)
    }

    private fun setupSearchView() {
        binding.searchView
            .editText
            .setOnEditorActionListener { textView, actionId, event ->
//                binding.searchBar.setText(binding.searchView.text)
//                val text = textView.text.toString()
                binding.searchView.hide()
//                Toast.makeText(requireActivity(), binding.searchView.text, Toast.LENGTH_SHORT).show()
                false
            }
    }

    private fun setupAppBar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    binding.searchView.show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupButton() {
        binding.btnAddQuiz.setOnClickListener { addNewQuiz() }
    }

    private fun setupQuizHistory() {
        val quizHistoryAdapter = QuizHistoryAdapter(this)
        binding.rvQuiz.apply {
            adapter = quizHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }

        quizHistoryAdapter.submitList(generateDummyQuizItems())
    }

    private fun addNewQuiz() {
        val addNewQuizIntent = Intent(context, AddQuizActivity::class.java)
        startActivity(addNewQuizIntent)
    }

}