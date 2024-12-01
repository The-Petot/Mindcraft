package com.thepetot.mindcraft.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), QuizHistoryAdapter.OnQuizClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var quizHistoryAdapter: QuizHistoryAdapter
    private val viewModel: HomeViewModel by viewModels()

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

        // Init dummy data TODO: Delete this after backend is implemented
        lifecycleScope.launch {
            viewModel.quizHistory = generateDummyQuizItems()
        }

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

        setupSearchView()
        setupAppBar()
        setupSearchHistory()
        setupQuizHistory()
        setupButton()
    }

    private fun setupSearchHistory() {
        val searchHistoryAdapter = SearchHistoryAdapter()
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
                val text = textView.text.toString()
                lifecycleScope.launch {
                    val filteredList = viewModel.quizHistory.filter {
                        it.description.contains(text, ignoreCase = true) ||
                                it.title.contains(text, ignoreCase = true)
                    }
                    quizHistoryAdapter.submitList(filteredList)
                }

                viewModel.searchState = true
                binding.toolbar.menu.findItem(R.id.action_search).icon = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_rounded_close)

                binding.searchView.hide()
//                Toast.makeText(requireActivity(), binding.searchView.text, Toast.LENGTH_SHORT).show()
                false
            }
    }

    private fun setupAppBar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    if (viewModel.searchState) {
                        viewModel.searchState = false
                        binding.toolbar.menu.findItem(R.id.action_search).icon = AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_rounded_search)
//                        quizHistoryAdapter.submitList(viewModel.quizHistory)
//                        binding.rvQuiz.scrollToPosition(0)
                        // Launch coroutine for delay without blocking UI
                        // line val layoutManager will not execute until the delay is finished (that is the reason we are using coroutine here)
                        lifecycleScope.launch {
                            quizHistoryAdapter.submitList(viewModel.quizHistory)  // Update list
                            delay(500)  // Sleep for 500 milliseconds (non-blocking)
                            // Scroll smoothly to the top
                            val layoutManager = binding.rvQuiz.layoutManager as? LinearLayoutManager
                            layoutManager?.smoothScrollToPosition(binding.rvQuiz, RecyclerView.State(), 0)
                        }
                    } else {
                        binding.searchView.show()
                    }
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
        quizHistoryAdapter = QuizHistoryAdapter(this)
        binding.rvQuiz.apply {
            adapter = quizHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }

        quizHistoryAdapter.submitList(viewModel.quizHistory)
    }

    private fun addNewQuiz() {
        val addNewQuizIntent = Intent(context, AddQuizActivity::class.java)
        startActivity(addNewQuizIntent)
    }

}