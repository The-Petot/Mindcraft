package com.thepetot.mindcraft.ui.home.quiz.result

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.databinding.ActivityResultQuizBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.home.quiz.add.AddQuizViewModel
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.ui.main.MainActivity
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.withDateFormat

class ResultQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultQuizBinding
    private var quiz: DataItem? = null
    private val viewModel: AddQuizViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var score: Int = 0

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


        score = intent.getIntExtra(QUIZ_SCORE, 0)

        quiz = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(QUIZ_EXTRA, DataItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(QUIZ_EXTRA) as? DataItem
        }

        quiz?.let {
            binding.apply {
                tvTitle.text = it.title
                tvSummary.text = it.summary
                tvAuthor.text = it.authorFirstName
                tvTotalQuestions.text = String.format("%d", it.totalQuestions)
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
        binding.btnSave.setOnClickListener {
            quiz?.let {
                val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
                viewModel.createParticipations(userId, it.id, score).observe(this) { result ->
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
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            finishResultIntent()
                        }
                    }
                }
            }
//            finishResultIntent()
        }
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