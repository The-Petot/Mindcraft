package com.thepetot.mindcraft.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.databinding.FragmentHomeBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.adapter.ChallengesAdapter
import com.thepetot.mindcraft.ui.adapter.LoadingStateAdapter
import com.thepetot.mindcraft.ui.adapter.SearchHistoryAdapter
import com.thepetot.mindcraft.ui.home.quiz.add.AddQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.ui.login.LoginViewModel
import com.thepetot.mindcraft.utils.generateDummyQuizItems
import com.thepetot.mindcraft.utils.generateSearchHistoryDummy
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), ChallengesAdapter.OnQuizClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var challengesAdapter: ChallengesAdapter
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

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

//        viewModel.updateSearchQuery()

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

        viewModel.getSearchQueries().observe(viewLifecycleOwner) { result ->
            searchHistoryAdapter.submitList(result.take(15))
        }

//        searchHistoryAdapter.submitList(generateSearchHistoryDummy())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQuizClicked(quiz: DataItem) {
        val quizDetailIntent = Intent(context, DetailQuizActivity::class.java)
        quizDetailIntent.putExtra(QUIZ_EXTRA, quiz)
        startActivity(quizDetailIntent)
    }

    private fun setupSearchView() {
        if (viewModel.searchState)
            binding.toolbar.menu.findItem(R.id.action_search).icon =
                AppCompatResources.getDrawable(requireActivity(), R.drawable.ic_rounded_close)

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
//                    challengesAdapter.submitList(filteredList)
                }
                viewModel.insertSearchQuery(text)
                viewModel.updateSearchQuery(text)

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
//                            challengesAdapter.submitList(viewModel.quizHistory)  // Update list
                            viewModel.updateSearchQuery("")
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
        challengesAdapter = ChallengesAdapter(this)
        binding.rvQuiz.apply {
            adapter = challengesAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    challengesAdapter.retry()
                }
            )
            layoutManager = LinearLayoutManager(context)
        }

//        challengesAdapter.submitList(viewModel.quizHistory)
        viewModel.challenges.observe(viewLifecycleOwner) {
            challengesAdapter.submitData(lifecycle, it)
        }
    }

    private fun addNewQuiz() {
        val addNewQuizIntent = Intent(context, AddQuizActivity::class.java)
        startActivity(addNewQuizIntent)
    }

}