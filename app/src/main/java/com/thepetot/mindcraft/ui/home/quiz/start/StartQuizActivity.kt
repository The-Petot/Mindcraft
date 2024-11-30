package com.thepetot.mindcraft.ui.home.quiz.start

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.ActivityStartQuizBinding
import com.thepetot.mindcraft.ui.adapter.QuestionsAdapter
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.ui.home.quiz.result.ResultQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.result.ResultQuizActivity.Companion.QUIZ_SCORE
import com.thepetot.mindcraft.utils.formatDuration
import com.thepetot.mindcraft.utils.formatSecondsToTimer
import com.thepetot.mindcraft.utils.generateDummyQuestions
import com.thepetot.mindcraft.utils.withDateFormat

class StartQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartQuizBinding
    private var quiz: ListQuizItem? = null
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupView()
        setupButton()
    }

    private fun setupView() {

        val questionsAdapter = QuestionsAdapter()
        binding.rvQuiz.apply {
            adapter = questionsAdapter
            layoutManager = LinearLayoutManager(context)
        }

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
//                tvTimer.text = formatSecondsToTimer(it.duration)
                startTimer(it.duration)
            }
        }

        questionsAdapter.submitList(generateDummyQuestions())
    }

    private fun startTimer(durationInSeconds: Int) {
        // Convert seconds to milliseconds for CountDownTimer
        timer = object : CountDownTimer(durationInSeconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                binding.tvTimer.text = formatSecondsToTimer(secondsRemaining)
            }

            override fun onFinish() {
                binding.tvTimer.text = getString(R.string.timer_finished) // Add a string resource for "00:00" or similar
                showTimerExpiredDialog()
            }
        }
        timer?.start()
    }

    private fun setupButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackConfirmationDialog()
            }
        })

        binding.toolbar.setNavigationOnClickListener { showBackConfirmationDialog() }
        binding.btnSubmit.setOnClickListener { showConfirmationDialog() }
    }

    private fun showBackConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Are you sure you want to leave?")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Submit Quiz?")
            .setMessage("You're about to submit your quiz.\n- Have you reviewed all your answers?\n- Once submitted, you won't be able to make changes.")
            .setPositiveButton("Yes") { _, _ ->
                calculateScoreAndFinish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showTimerExpiredDialog() {
        var hasNavigated = false
        MaterialAlertDialogBuilder(this)
            // TODO: Use Generative AI Gemini to make message for time's up! dialogs
            .setTitle("Time's Up! 🕛")
            .setMessage("Your quiz session has ended. Don't worry—every attempt is a step forward!")
            .setNeutralButton("Ok") { _, _ ->
                if (!hasNavigated) {
                    hasNavigated = true
                    calculateScoreAndFinish()
                }
            }
            .setOnDismissListener {
                if (!hasNavigated) {
                    hasNavigated = true
                    calculateScoreAndFinish()
                }
            }
            .show()
    }

    private fun calculateScoreAndFinish() {
        val selectedAnswers = (binding.rvQuiz.adapter as QuestionsAdapter).getSelectedAnswers()
        val questions = generateDummyQuestions() // Replace with actual quiz questions if fetched from server

        var correctCount = 0
        for ((index, question) in questions.withIndex()) {
            val selectedAnswer = selectedAnswers[index]
            if (selectedAnswer == question.correctAnswer) {
                correctCount++
            }
        }

        val totalQuestions = questions.size
        val score = (correctCount.toDouble() / totalQuestions * 100).toInt() // Convert to percentage

        // Pass the score to the ResultActivity
        val resultIntent = Intent(this, ResultQuizActivity::class.java).apply {
            putExtra(QUIZ_EXTRA, quiz)
            putExtra(QUIZ_SCORE, score)
        }
        startActivity(resultIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // Ensure the timer is canceled to prevent memory leaks
    }
}