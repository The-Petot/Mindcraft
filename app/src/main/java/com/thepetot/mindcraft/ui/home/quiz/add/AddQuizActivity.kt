package com.thepetot.mindcraft.ui.home.quiz.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.databinding.ActivityAddQuizBinding
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA

class AddQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSubmit.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val startQuizIntent = Intent(this, DetailQuizActivity::class.java)

            val quiz = ListQuizItem(
                900,
                "2024-11-24T10:00:00",
                5,
                "Azka",
                binding.textInputDescription.text.toString(),
                "id_0",
                binding.textInputTitle.text.toString()
            )

            startQuizIntent.putExtra(QUIZ_EXTRA, quiz)
            startActivity(startQuizIntent)
        }
    }
}