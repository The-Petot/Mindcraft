package com.thepetot.mindcraft.ui.home.quiz.add

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.ListQuizItem
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeResponse
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.databinding.ActivityAddQuizBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizActivity.Companion.QUIZ_EXTRA
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.checkTagsInput
import com.thepetot.mindcraft.utils.parseTags

class AddQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddQuizBinding
    private val viewModel: AddQuizViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityAddQuizBinding.inflate(layoutInflater)
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

        viewModel.createQuizResult.observe(this) { result ->
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
                    val startQuizIntent = Intent(this, DetailQuizActivity::class.java)
                    // TODO: first name dan last name
                    val data = DataItem(
                        result.data.data.summary,
                        result.data.data.totalQuestions,
                        result.data.data.createdAt,
                        result.data.data.timeSeconds,
                        result.data.data.description,
                        result.data.data.id,
                        result.data.data.title,
                        result.data.data.authorId,
                        result.data.data.tags,
                        result.data.data.updatedAt,
                        result.data.data.authorFirstName,
                        result.data.data.authorLastName
                    )
//                    val quiz = ListQuizItem(
//                        900,
//                        "2024-11-24T10:00:00",
//                        5,
//                        "Azka",
//                        binding.textInputDescription.text.toString(),
//                        "id_0",
//                        binding.textInputTitle.text.toString()
//                    )

                    startQuizIntent.putExtra(QUIZ_EXTRA, data)
                    startActivity(startQuizIntent)
                    finish()
                }
                null -> {}
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.textInputMaterial.doOnTextChanged { text, start, before, count ->
            if (text.toString().length < 100 || text.toString().length > 3000) {
                binding.textInputMaterialLayout.errorIconDrawable = null
                binding.textInputMaterialLayout.error = "Material must be between 100 and 3000 characters"
            } else {
                binding.textInputMaterialLayout.error = null
            }
        }

        binding.textInputTags.doOnTextChanged { text, start, before, count ->
            if (checkTagsInput(text.toString())) {
                binding.textInputTagsLayout.error = null
            } else {
                binding.textInputTagsLayout.error = "Invalid tags format"
            }
        }

        binding.textInputTitle.doOnTextChanged { text, start, before, count ->
            if (text.toString().isEmpty()) {
                binding.textInputTitleLayout.error = "Title is required"
            } else {
                binding.textInputTitleLayout.error = null
            }
        }

        binding.textInputDescription.doOnTextChanged { text, start, before, count ->
            if (text.toString().isEmpty()) {
                binding.textInputDescriptionLayout.error = "Description is required"
            } else {
                binding.textInputDescriptionLayout.error = null
            }
        }

        binding.btnSubmit.setOnClickListener {
            val titleLayout = binding.textInputTitleLayout
            val title = binding.textInputTitle

            val descriptionLayout = binding.textInputDescriptionLayout
            val description = binding.textInputDescription

            val materialLayout = binding.textInputMaterialLayout
            val material = binding.textInputMaterial

            val tagsLayout = binding.textInputTagsLayout
            val tags = binding.textInputTags

            when {
                titleLayout.error != null || title.text.isNullOrEmpty() -> {
                    title.requestFocus()
                    titleLayout.error = titleLayout.error ?: "Title is required"
                }
                descriptionLayout.error != null || description.text.isNullOrEmpty() -> {
                    description.requestFocus()
                    descriptionLayout.error = descriptionLayout.error ?: "Description is required"
                }
                materialLayout.error != null || material.text.isNullOrEmpty() -> {
                    material.requestFocus()
                    materialLayout.error = materialLayout.error ?: "Material is required"
                }
                tagsLayout.error != null -> {
                    tags.requestFocus()
                }
                else -> {
                    binding.progressBar.visibility = View.VISIBLE
                    val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
                    if (tags.text.toString().isEmpty()) {
                        viewModel.createQuiz(
                            userId,
                            title.text.toString(),
                            description.text.toString(),
                            material.text.toString(),
                            900
                        )
                    } else {
                        val listTags = parseTags(tags.text.toString())
                        viewModel.createQuiz(
                            userId,
                            title.text.toString(),
                            description.text.toString(),
                            material.text.toString(),
                            900,
                            listTags
                        )
                    }
                }
            }
        }
    }
}