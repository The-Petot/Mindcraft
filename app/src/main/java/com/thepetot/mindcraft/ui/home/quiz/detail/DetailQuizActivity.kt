package com.thepetot.mindcraft.ui.home.quiz.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.ActivityDetailQuizBinding
import com.thepetot.mindcraft.ui.home.quiz.start.StartQuizActivity
import com.thepetot.mindcraft.utils.formatDuration
import com.thepetot.mindcraft.utils.withDateFormat

class DetailQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailQuizBinding
    private var quiz: ListQuizItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupView()
        setupButton()
    }

    private fun setupView() {
        quiz = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(QUIZ_EXTRA, ListQuizItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(QUIZ_EXTRA) as? ListQuizItem
        }

        quiz?.let {
            binding.apply {
                tvTitle.text = it.title
                tvDescription.text = it.description
                tvTotalQuestions.text = String.format(it.question.toString())
                tvDuration.text = formatDuration(it.duration)
                tvAuthor.text = it.author
                tvDateCreated.text = it.createdAt.withDateFormat()
            }
        }
    }

    private fun setupButton() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnStart.setOnClickListener { startQuiz() }
    }

    private fun startQuiz() {
        val startQuizIntent = Intent(this, StartQuizActivity::class.java)
        startQuizIntent.putExtra(QUIZ_EXTRA, quiz)
        startActivity(startQuizIntent)
    }

    companion object {
        const val QUIZ_EXTRA = "quiz_extra"
    }
}