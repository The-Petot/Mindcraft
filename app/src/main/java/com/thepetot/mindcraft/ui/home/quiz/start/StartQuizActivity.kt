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

import com.thepetot.mindcraft.data.remote.response.challenges.questions.QuestionsResponse
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.databinding.ActivityStartQuizBinding
import com.thepetot.mindcraft.ui.adapter.QuestionsAdapter
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.ui.home.quiz.result.ResultQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.result.ResultQuizActivity.Companion.QUIZ_MOD
import com.thepetot.mindcraft.ui.home.quiz.result.ResultQuizActivity.Companion.QUIZ_SCORE
import com.thepetot.mindcraft.utils.formatDuration
import com.thepetot.mindcraft.utils.formatSecondsToTimer
import com.thepetot.mindcraft.utils.generateDummyQuestions
import com.thepetot.mindcraft.utils.withDateFormat

class StartQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartQuizBinding
    private var quiz: DataItem? = null
    private var questions: QuestionsResponse? = null
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStartQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        val questionsAdapter = QuestionsAdapter()
        binding.rvQuiz.apply {
            adapter = questionsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        quiz = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(QUIZ_EXTRA, DataItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(QUIZ_EXTRA) as? DataItem
        }

        questions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(QUIZ_QUESTIONS, QuestionsResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(QUIZ_QUESTIONS) as? QuestionsResponse
        }

        quiz?.let {
            binding.apply {
                tvTitle.text = it.title
                tvDescription.text = it.description
//                tvTimer.text = formatSecondsToTimer(it.duration)
                startTimer(it.timeSeconds)
            }
        }

//        questionsAdapter.submitList(generateDummyQuestions())
        questionsAdapter.submitList(questions?.data)
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
//        val questions = generateDummyQuestions() // Replace with actual quiz questions if fetched from server
        val questionss = questions?.data

        val correctAnswers: MutableMap<Int, MutableList<Pair<Int, Boolean>>> = mutableMapOf()

        if (questionss != null) {
            for ((number, question) in questionss.withIndex()) {
                val option = question.answers
                correctAnswers[number] = mutableListOf(
                    Pair(0, option[0].correct),
                    Pair(1, option[1].correct),
                    Pair(2, option[2].correct),
                    Pair(3, option[3].correct)
                )
            }
        }

        println(correctAnswers)
        println(selectedAnswers)

        var correctCount = 0
//        for ((key, value) in selectedAnswers) {
//            println("Key: ${key.toString()}, Value: $value")
//            if (value) correctCount++
//        }

//        var correctCount = 0
//        for ((index, question) in questions?.withIndex()!!) {
//            val selectedAnswer = selectedAnswers[index]
//            if (selectedAnswer == question.answers.any { it.correct }) {
//                correctCount++
//            }
//        }

        for ((index, question) in questionss?.withIndex()!!) {
            val correctIndex = question.answers.indexOfFirst { it.correct }
            val selectedAnswer = selectedAnswers[index]
            if (selectedAnswer == correctIndex) {
                correctCount++
            }
        }

        val totalQuestions = questionss.size
        val score = (correctCount.toDouble() / totalQuestions * 100).toInt() // Convert to percentage


//        for (question in questions) {
//            question.checked = selectedAnswers[questions.indexOf(question)] ?: -1
//        }

        for ((index, question) in questions?.data?.withIndex()!!) {
            question.checked = selectedAnswers[index]
        }

        // Pass the score to the ResultActivity
        val resultIntent = Intent(this, ResultQuizActivity::class.java).apply {
            putExtra(QUIZ_EXTRA, quiz)
            putExtra(QUIZ_SCORE, score)
            putExtra(QUIZ_MOD, questions)
        }
        startActivity(resultIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel() // Ensure the timer is canceled to prevent memory leaks
    }

    companion object {
        const val QUIZ_QUESTIONS = "quiz_questions"
    }
}