package com.thepetot.mindcraft.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.thepetot.mindcraft.data.remote.response.signup.SignupResponse
import com.thepetot.mindcraft.databinding.ActivitySignupBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.utils.Result

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.right)
            insets
        }

        setupObserver()
        setupView()
        setupButton()
    }

    private fun setupObserver() {
        viewModel.signupResult.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.INVISIBLE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    viewModel.clearSignup()
                }
                is Result.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressIndicator.visibility = View.INVISIBLE
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    viewModel.clearSignup()
                    finish()
                }
                null -> {}
            }
        }
    }

    private fun setupView() {
        binding.etFirstName.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (!it.matches("^[a-zA-Z]*$".toRegex())) {
                    binding.etLayoutFirstName.error = "Only letters"
                } else {
                    binding.etLayoutFirstName.error = null
                }
            }
        }

        binding.etLastName.doOnTextChanged { text, _, _, _ ->
            text?.let {
                if (!it.matches("^[a-zA-Z]*$".toRegex())) {
                    binding.etLayoutLastName.error = "Only letters"
                } else {
                    binding.etLayoutLastName.error = null
                }
            }
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            if (text != null && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                binding.etLayoutEmail.error = "Invalid email"
            } else {
                binding.etLayoutEmail.error = null
            }
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.length < 8) {
                binding.etLayoutPassword.error = "Minimum 8 characters"
            } else {
                binding.etLayoutPassword.error = null
            }
        }

        binding.etConfirmPassword.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.toString() != binding.etPassword.text.toString()) {
                binding.etLayoutConfirmPassword.error = "Password doesn't match"
            } else {
                binding.etLayoutConfirmPassword.error = null
            }
        }
    }

    private fun setupButton() {
        binding.btnLogin.apply {
            contentDescription = "Used for login"
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            setOnClickListener {
                finish()
            }
        }

        binding.btnSignup.setOnClickListener {
            when {
                binding.etLayoutFirstName.error != null || binding.etFirstName.text.isNullOrEmpty() -> {
                    binding.etFirstName.requestFocus()
                    binding.etLayoutFirstName.error = binding.etLayoutFirstName.error ?: "First Name is required"
                }
                binding.etLayoutLastName.error != null || binding.etLastName.text.isNullOrEmpty() -> {
                    binding.etLastName.requestFocus()
                    binding.etLayoutLastName.error = binding.etLayoutLastName.error ?: "Last Name is required"
                }
                binding.etLayoutEmail.error != null || binding.etEmail.text.isNullOrEmpty() -> {
                    binding.etEmail.requestFocus()
                    binding.etLayoutEmail.error = binding.etLayoutEmail.error ?: "Email is required"
                }
                binding.etLayoutPassword.error != null || binding.etPassword.text.isNullOrEmpty() -> {
                    binding.etPassword.requestFocus()
                    binding.etLayoutPassword.error = binding.etLayoutPassword.error ?: "Password is required"
                }
                binding.etLayoutConfirmPassword.error != null || binding.etConfirmPassword.text.isNullOrEmpty() -> {
                    binding.etConfirmPassword.requestFocus()
                    binding.etLayoutConfirmPassword.error = binding.etLayoutConfirmPassword.error ?: "Confirm Password is required"
                }
                else -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                    // TODO: Implement actual signup mechanism
                    val firstName = binding.etFirstName.text.toString()
                    val lastName = binding.etLastName.text.toString()
                    val email = binding.etEmail.text.toString()
                    val password = binding.etPassword.text.toString()

                    println("Signup button pressed")
                    viewModel.signup(firstName, lastName, email, password)
                }
            }
        }
    }

}
