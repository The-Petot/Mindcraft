package com.thepetot.mindcraft.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.thepetot.mindcraft.databinding.ActivityRegisterBinding
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.ui.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logoImageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.registerTitleTextView, View.ALPHA, 1f).setDuration(500)
        val firstNameEditText = ObjectAnimator.ofFloat(binding.firstNameEditText, View.ALPHA, 1f).setDuration(500)
        val lastNameEditText = ObjectAnimator.ofFloat(binding.lastNameEditText, View.ALPHA, 1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)
        val alreadyAccountTextView = ObjectAnimator.ofFloat(binding.alreadyAccountTextView, View.ALPHA, 1f).setDuration(500)
        val loginLinkTextView = ObjectAnimator.ofFloat(binding.loginLinkTextView, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                firstNameEditText,
                lastNameEditText,
                emailEditText,
                passwordEditText,
                registerButton,
                alreadyAccountTextView,
                loginLinkTextView
            )
            start()
        }
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val firstName = binding.firstNameEditText.text.toString()
            val lastName = binding.lastNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                firstName.isEmpty() -> binding.firstNameEditText.error = "Masukkan nama depan anda"
                lastName.isEmpty() -> binding.lastNameEditText.error = "Masukkan nama belakang anda"
                email.isEmpty() -> binding.emailEditText.error = "Masukkan email anda"
                password.isEmpty() -> binding.passwordEditText.error = "Masukkan password anda"
                else -> {
                    registerViewModel.register(email, password, firstName, lastName)

                    // Observasi proses loading
                    registerViewModel.isLoading.observe(this) { isLoading ->
                        showLoading(isLoading)
                    }

                    // Observasi hasil registrasi
                    registerViewModel.registerSuccess.observe(this) { success ->
                        if (success) {
                            Toast.makeText(this, "Register akun anda berhasil", Toast.LENGTH_SHORT).show()
                            finish() // Menutup activity setelah berhasil
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
