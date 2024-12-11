package com.thepetot.mindcraft.ui.home.quiz.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeResponse
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.databinding.ActivityDetailQuizBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.home.quiz.add.AddQuizViewModel
import com.thepetot.mindcraft.ui.home.quiz.start.StartQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.start.StartQuizActivity.Companion.QUIZ_QUESTIONS
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.formatDuration
import com.thepetot.mindcraft.utils.withDateFormat

class DetailQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailQuizBinding
    private var quiz: DataItem? = null
    private val viewModel: DetailQuizViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

        setupView()
        setupButton()
    }

    private fun setupView() {
        quiz = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(QUIZ_EXTRA, DataItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(QUIZ_EXTRA) as? DataItem
        }

        quiz?.let {
            binding.apply {
                tvTitle.text = it.title
                tvDescription.text = it.description
                tvTotalQuestions.text = String.format(it.totalQuestions.toString())
                tvDuration.text = formatDuration(it.timeSeconds)
                tvAuthor.text = it.authorFirstName
                tvTags.text = String.format("Tags: ${it.tags}")
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

        viewModel.getQuestionsByChallengeId(quiz?.id!!).observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    val startQuizIntent = Intent(this, StartQuizActivity::class.java)
                    startQuizIntent.putExtra(QUIZ_EXTRA, quiz)
                    startQuizIntent.putExtra(QUIZ_QUESTIONS, result.data)
                    startActivity(startQuizIntent)
                }
            }
        }

//        val startQuizIntent = Intent(this, StartQuizActivity::class.java)
//        startQuizIntent.putExtra(QUIZ_EXTRA, quiz)
//        startActivity(startQuizIntent)
    }

    companion object {
        const val QUIZ_EXTRA = "quiz_extra"
    }
}