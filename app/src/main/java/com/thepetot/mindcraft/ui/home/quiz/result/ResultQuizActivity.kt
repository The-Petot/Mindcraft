package com.thepetot.mindcraft.ui.home.quiz.result

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.ActivityResultQuizBinding
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.ui.main.MainActivity
import com.thepetot.mindcraft.utils.withDateFormat

class ResultQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultQuizBinding
    private var quiz: ListQuizItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(
                // Notice we're using systemBars, not statusBar
                WindowInsetsCompat.Type.systemBars()
                        // Notice we're also accounting for the display cutouts
                        or WindowInsetsCompat.Type.displayCutout()
                // If using EditText, also add
                // "or WindowInsetsCompat.Type.ime()"
                // to maintain focus when opening the IME
            )
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupButton()
    }

    @SuppressLint("DefaultLocale")
    private fun setupView() {
        val score = intent.getIntExtra(QUIZ_SCORE, 0)

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
                tvAuthor.text = it.author
                tvTotalQuestions.text = String.format("%d", it.question)
                tvScore.text = String.format("%d", score)
                tvDateCreated.text = it.createdAt.withDateFormat()
            }
        }
    }

    private fun setupButton() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showConfirmationDialog()
            }
        })

        binding.btnOk.setOnClickListener { showConfirmationDialog() }
        binding.btnSave.setOnClickListener { finishResultIntent() }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setMessage("Are you sure you want to leave without saving the score?")
            .setPositiveButton("Yes") { _, _ ->
                finishResultIntent()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun finishResultIntent() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        const val QUIZ_SCORE = "quiz_score"
    }
}