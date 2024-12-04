package com.thepetot.mindcraft.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.databinding.ActivityLoginBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.main.MainActivity
import com.thepetot.mindcraft.ui.onboarding.OnboardingActivity.Companion.USER_LOGGED_IN
import com.thepetot.mindcraft.ui.signup.SignupActivity
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.SharedPreferencesManager
import com.thepetot.mindcraft.utils.logMessage
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        auth = Firebase.auth

        setupObserver()
        setupView()
        setupButton()
    }

    private fun setupObserver() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    binding.progressIndicator.visibility = View.INVISIBLE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    viewModel.clearLogin()
                }
                is Result.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    binding.progressIndicator.visibility = View.INVISIBLE
                    viewModel.saveUserData(result.data)
                    SharedPreferencesManager.set(applicationContext, USER_LOGGED_IN, true)
                    viewModel.clearLogin()
                    navigateToMainPage()
                }
                null -> {}
            }
        }
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
                    binding.progressIndicator.visibility = View.VISIBLE
                    // TODO: Implement actual login mechanism
                    val email = binding.etEmail.text.toString()
                    val password = binding.etPassword.text.toString()
                    viewModel.login(email, password)
                    //showOtpDialog()
                }
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            val credentialManager = CredentialManager.create(this)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
                .addCredentialOption(googleIdOption)
                .build()

            lifecycleScope.launch {
                try {
                    binding.progressIndicator.visibility = View.VISIBLE
                    val result: GetCredentialResponse = credentialManager.getCredential( //import from androidx.CredentialManager
                        request = request,
                        context = this@LoginActivity,
                    )
                    handleSignIn(result)
                } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                    binding.progressIndicator.visibility = View.INVISIBLE
                    logMessage("Error", e.message.toString())
                }
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
//                        Log.e(TAG, "Received an invalid google id token response", e)
                        logMessage(Log::e, TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    logMessage(TAG, "Unexpected type of credential")
                }
            }
            else -> {
                // Catch any unrecognized credential type here.
                logMessage(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logMessage(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    if (user != null) {
                        viewModel.oAuthLogin(idToken)
//                        SharedPreferencesManager.set(applicationContext, USER_LOGGED_IN, true)
//                        navigateToMainPage()
                    }
                } else {
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    logMessage(Log::w, TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun showOtpDialog() {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_otp, null)

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

            }
        }
    }

    private fun navigateToMainPage() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}

// TODO: Self note (don't delete)
//lifecycleScope.launch {
//    withContext(Dispatchers.IO) {
//        // Perform heavy operations here (e.g., database, network, file operations)
//    }
//    withContext(Dispatchers.Main) {
//        // Update the UI here after the operation
//    }
//}
