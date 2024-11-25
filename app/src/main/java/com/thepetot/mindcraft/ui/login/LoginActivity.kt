package com.thepetot.mindcraft.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thepetot.mindcraft.databinding.ActivityLoginBinding
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        val title = ObjectAnimator.ofFloat(binding.loginTitleTextView, View.ALPHA, 1f).setDuration(500)
        val emailField = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)
        val passwordField = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val divider = ObjectAnimator.ofFloat(binding.dividerView, View.ALPHA, 1f).setDuration(500)
        val orTextView = ObjectAnimator.ofFloat(binding.orTextView, View.ALPHA, 1f).setDuration(500)
        val registerTextView = ObjectAnimator.ofFloat(binding.registerTextView, View.ALPHA, 1f).setDuration(500)
        val registerLink = ObjectAnimator.ofFloat(binding.registerLinkTextView, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(title, emailField, passwordField, loginButton, divider, orTextView, registerTextView, registerLink)
            start()
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this, { user ->
            this.user = user
        })
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditText.error = "Masukkan email anda"
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = "Masukkan password anda"
                }
                else -> {
                    loginViewModel.login(email, password)
                    loginViewModel.isLoading.observe(this, {
                        showLoading(it)
                    })
                    loginViewModel.loginUser()
                    loginViewModel.loginSuccess.observe(this, Observer { success ->
                        if (success) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
