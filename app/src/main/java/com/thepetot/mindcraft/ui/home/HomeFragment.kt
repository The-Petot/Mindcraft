package com.thepetot.mindcraft.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.FragmentHomeBinding
import com.thepetot.mindcraft.ui.adapter.QuizHistoryAdapter
import com.thepetot.mindcraft.ui.home.quiz.add.AddQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.utils.generateDummyData

class HomeFragment : Fragment(), QuizHistoryAdapter.OnQuizClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizHistoryAdapter = QuizHistoryAdapter(this)
        binding.rvQuiz.apply {
            adapter = quizHistoryAdapter
            layoutManager = LinearLayoutManager(context)
        }

        quizHistoryAdapter.submitList(generateDummyData(10))

        binding.btnAddQuiz.setOnClickListener { addNewQuiz() }
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

    private fun addNewQuiz() {
        val addNewQuizIntent = Intent(context, AddQuizActivity::class.java)
        startActivity(addNewQuizIntent)
    }

}