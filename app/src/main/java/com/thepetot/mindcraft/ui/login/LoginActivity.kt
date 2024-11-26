package com.thepetot.mindcraft.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.databinding.ActivityLoginBinding
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.ui.main.MainActivity
import com.thepetot.mindcraft.ui.signup.SignupActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupButton()
    }

    private fun setupView() {
        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            if (text != null && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                binding.etLayoutEmail.error = "Invalid email"
            } else {
                binding.etLayoutEmail.error = null
            }
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.etLayoutPassword.error = null
            }
        }
    }

    private fun setupButton() {
        binding.btnSignup.apply {
            contentDescription = "Used for sign up"
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
            setOnClickListener {
                val signupIntent = Intent(context, SignupActivity::class.java)
                startActivity(signupIntent)
            }
        }

        binding.btnLogin.setOnClickListener {
            when {
                binding.etLayoutEmail.error != null || binding.etEmail.text.isNullOrEmpty() -> {
                    binding.etEmail.requestFocus()
                    binding.etLayoutEmail.error = binding.etLayoutEmail.error ?: "Email is required"
                }
                binding.etPassword.text.isNullOrEmpty() -> {
                    binding.etPassword.requestFocus()
                    binding.etLayoutPassword.error = binding.etLayoutPassword.error ?: "Password is required"
                }
                else -> {
                    // TODO: Implement actual login mechanism
                    showOtpDialog()
                }
            }
        }
    }

    private fun showOtpDialog() {
        // Inflate the custom layout
        var condition = false
        val dialogView = LayoutInflater.from(this).inflate(R.layout.otp_dialog, null)

        // Get references to input fields
        val otpInputLayout = dialogView.findViewById<TextInputLayout>(R.id.otp_input_layout)
        val otpEditText = dialogView.findViewById<TextInputEditText>(R.id.et_otp)

        // Build the dialog
        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setPositiveButton("Submit", null)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.show()

        // Set the positive button behavior
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val otpCode = otpEditText.text?.toString()

            if (otpCode.isNullOrEmpty() || otpCode.length != 6) {
                otpInputLayout.error = "Please enter a valid 6-digit OTP"
                otpInputLayout.errorIconDrawable = null
            } else {
                otpInputLayout.error = null
                // Handle OTP submission
//                handleOtpSubmission(otpCode)
                // TODO: Implement actual OTP mechanism
                dialog.dismiss() // Dismiss only if there's no error
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
    }
}
