package com.thepetot.mindcraft.ui.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorResponse
import com.thepetot.mindcraft.R as appR
import com.thepetot.mindcraft.databinding.FragmentSettingsBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.login.LoginActivity
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.SharedPreferencesManager
import com.thepetot.mindcraft.utils.getBitmapFromBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var auth: FirebaseAuth

    // profile
    private lateinit var profile: ConstraintLayout

    // two-step button
    private lateinit var twoStepVerification: ConstraintLayout
    private lateinit var switchTwoStepVerification: MaterialSwitch

    // notification button
    private lateinit var notification: ConstraintLayout
    private lateinit var switchNotification: MaterialSwitch

    // theme button
    private lateinit var theme: ConstraintLayout
    private lateinit var menuTheme: ImageView
    private lateinit var currentTheme: TextView

    private lateinit var progressBar: LinearProgressIndicator

    private lateinit var dialogView: View
    private lateinit var dialog: AlertDialog

    private var switchState: Boolean = false

    private val requestNotificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                showPermissionDialog()
            }
        }

    //region override section
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        profile = binding.profile

        twoStepVerification = binding.twoStepVerification
        switchTwoStepVerification = binding.switchTwoStepVerification

        notification = binding.notification
        switchNotification = binding.switchNotification

        theme = binding.theme
        menuTheme = binding.menuTheme
        currentTheme = binding.tvCurrentTheme

        progressBar = binding.progressBar

        setupTheme()
        setupNotification()
        setup2FA()
        setupLogout()
        setupProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region theme
    private fun setupTheme() {
        theme.setOnClickListener { showMenu() }

        val tempCurrentTheme = SharedPreferencesManager.get(requireContext().applicationContext, APP_THEME, SYSTEM_DEFAULT_THEME)
        viewModel.currentTheme = tempCurrentTheme
        currentTheme.text = viewModel.currentTheme
    }

    private fun showMenu() {
        val popup = PopupMenu(context, menuTheme)
        popup.menuInflater.inflate(appR.menu.popup_menu, popup.menu)

        popup.setOnMenuItemClickListener {
            // TODO: Fix all icon color on dark mode
            when (it.itemId) {
                appR.id.system_default -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, SYSTEM_DEFAULT_THEME)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    viewModel.currentTheme = SYSTEM_DEFAULT_THEME
                    currentTheme.text = viewModel.currentTheme
                    true
                }
                appR.id.light -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, LIGHT_THEME)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.currentTheme = LIGHT_THEME
                    currentTheme.text = viewModel.currentTheme
                    true
                }
                appR.id.dark -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, DARK_THEME)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    viewModel.currentTheme = DARK_THEME
                    currentTheme.text = viewModel.currentTheme
                    true
                }
                else -> false
            }
        }

        // Respond to popup being dismissed.
        // popup.setOnDismissListener { }
        popup.show()
    }
    //endregion

    //region 2fa
    // TODO: Not done yet
    private fun setup2FA() {
        // init state
        switchState = viewModel.getPreferenceSettings(UserPreference.TWO_FACTOR_ENABLE_KEY, false)
        switchTwoStepVerification.isChecked = switchState
        viewModel.is2FASwitchChecked = switchState
        switchTwoStepVerification.jumpDrawablesToCurrentState()

        // on click
        twoStepVerification.setOnClickListener {
            switchState = viewModel.getPreferenceSettings(UserPreference.TWO_FACTOR_ENABLE_KEY, false)
            if (switchState) {
                setTwoFactor()
                toggle2FAState(false)
            } else {
                progressBar.visibility = View.VISIBLE
                getTwoFactor()
                toggle2FAState(false)
            }
        }

        // on checked change
        switchTwoStepVerification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPreferenceSettings(UserPreference.TWO_FACTOR_ENABLE_KEY, isChecked)
            if (isChecked) viewModel.setPreferenceSettings(UserPreference.TWO_FACTOR_SECRET_KEY, viewModel.secretCode ?: "")
            else viewModel.deletePreferenceSettings(UserPreference.TWO_FACTOR_SECRET_KEY)
        }

        // observe
        viewModel.setTwoFactorResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    val progressBar = dialogView.findViewById<LinearProgressIndicator>(R.id.progress_bar)
                    progressBar.visibility = View.INVISIBLE
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.isClickable = true
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.isEnabled = true
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    viewModel.clearSetTwoFactor()
                }

                is Result.Loading -> {
                    val progressBar = dialogView.findViewById<LinearProgressIndicator>(R.id.progress_bar)
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    val progressBar = dialogView.findViewById<LinearProgressIndicator>(R.id.progress_bar)
                    progressBar.visibility = View.INVISIBLE
                    dialog.dismiss()
                    viewModel.clearSetTwoFactor()
                    toggle2FASwitch()
                    toggle2FAState(true)
                    Toast.makeText(requireContext(), result.data.message, Toast.LENGTH_SHORT).show()
                }

                null -> {}
            }
        }

        viewModel.getTwoFactorResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    toggle2FAState(true)
                    viewModel.clearGetTwoFactor()
                }
                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    show2FASecretDialog(result.data)
                }
                null -> {}
            }
        }
    }

    private fun setTwoFactor() {
        val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
        val secret = viewModel.getPreferenceSettings(UserPreference.TWO_FACTOR_SECRET_KEY, "")
        showTokenInputDialog(false, userId, secret)
    }

    private fun getTwoFactor() {
        viewModel.getTwoFactor()
    }

//    private fun change2FASwitchState() {
//        viewModel.is2FASwitchChecked = !viewModel.is2FASwitchChecked
//    }

    private fun toggle2FASwitch() {
        viewModel.is2FASwitchChecked = !viewModel.is2FASwitchChecked
        switchTwoStepVerification.isChecked = viewModel.is2FASwitchChecked
    }

    private fun toggle2FAState(state: Boolean) {
        switchTwoStepVerification.isEnabled = state
        twoStepVerification.isClickable = state
    }

    private fun show2FASecretDialog(response: TwoFactorResponse) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(appR.layout.dialog_qr_code, null)

        lifecycleScope.launch {
            val progressBar = dialogView.findViewById<CircularProgressIndicator>(appR.id.progress_bar)
            progressBar.visibility = View.VISIBLE
            val bitmap = withContext(Dispatchers.IO) {
                getBitmapFromBase64(response.data.qrCode)
            }
            dialogView.findViewById<ImageView>(appR.id.img_qr_code).setImageBitmap(bitmap)
            progressBar.visibility = View.INVISIBLE
        }
        dialogView.findViewById<TextView>(appR.id.tv_secret_key).text = response.data.secret

        // Build the dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("OK", null)
            .setCancelable(false)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
            val secret = response.data.secret
            viewModel.clearGetTwoFactor()
            dialog.dismiss()
            showTokenInputDialog(true, userId, secret)
        }
    }

    private fun showTokenInputDialog(state: Boolean, userId: Int, secret: String) {
        // Inflate the custom layout
        dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_otp, null)

        // Get references to input fields
        val otpInputLayout = dialogView.findViewById<TextInputLayout>(R.id.otp_input_layout)
        val otpEditText = dialogView.findViewById<TextInputEditText>(R.id.et_otp)
        val progressBar = dialogView.findViewById<LinearProgressIndicator>(R.id.progress_bar)

        // Build the dialog
        dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Submit", null)
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val otpCode = otpEditText.text?.toString()

            if (otpCode.isNullOrEmpty() || otpCode.length != 6) {
                otpInputLayout.error = "Please enter a valid 6-digit OTP"
                otpInputLayout.errorIconDrawable = null
            } else {
                // TODO: add verification OTP by hitting the endpoint
                viewModel.secretCode = secret
                progressBar.visibility = View.VISIBLE
                otpInputLayout.error = null
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.isClickable = false
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.isEnabled = false
                viewModel.setTwoFactor(state, userId, secret, otpCode)
            }
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setOnClickListener {
            viewModel.clearSetTwoFactor()
            println("CLEAR SET TWO FACTOR")
            toggle2FAState(true)
            dialog.dismiss()
        }
    }
    //endregion

    //region notification
    private fun setupNotification() {
        // Take switch state when the view first created
        val switchState = viewModel.getPreferenceSettings(UserPreference.NOTIFICATION_ENABLED_KEY, false)
        switchNotification.isChecked = switchState
        viewModel.isNotificationSwitchChecked = switchState
        // Ignore animation and jump to the current state of the switch
        switchNotification.jumpDrawablesToCurrentState()


        // Set click listener
        notification.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                handleNotificationPermission()
            } else {
                toggleNotificationSwitch()
            }
        }

        // Set switch state listener
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setPreferenceSettings(UserPreference.NOTIFICATION_ENABLED_KEY, isChecked)
            val done = viewModel.getPreferenceSettings(UserPreference.NOTIFICATION_ONE, false)
            if (isChecked && !done) {
                showNotificationOnce()
            }
        }
    }

    private fun showNotificationOnce() {
        viewModel.setPreferenceSettings(UserPreference.NOTIFICATION_ONE, true)
        val title = "Mindcraft is the feature of quizzes!"
        val message = "Thanks for using this app, we hope you enjoy it!"
        val notificationBuilder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_mindcraft_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)

//                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun toggleNotificationSwitch() {
        viewModel.isNotificationSwitchChecked = !viewModel.isNotificationSwitchChecked
        switchNotification.isChecked = viewModel.isNotificationSwitchChecked
    }

    @SuppressLint("InlinedApi")
    private fun handleNotificationPermission() {
        if (checkPermission()) {
            toggleNotificationSwitch()
        } else {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Notification permission was rejected. To use this feature, please grant permission to receive notifications in your settings.")
            .setPositiveButton("OK", null)
            .show()
    }

    @SuppressLint("InlinedApi")
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
    //endregion

    //region logout
    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            lifecycleScope.launch {
                val credentialManager = CredentialManager.create(requireContext())

                auth.signOut()
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
                val userId = viewModel.getPreferenceSettings(UserPreference.USERID_KEY, 0)
                viewModel.logout(userId)
            }
        }

        viewModel.logoutResult.observe(viewLifecycleOwner) { result ->
            val progressBar = binding.progressBar
            when (result) {
                is Result.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    viewModel.clearLogout()
                }

                is Result.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    progressBar.visibility = View.INVISIBLE
                    SharedPreferencesManager.set(requireActivity().applicationContext, USER_LOGGED_IN, false)
                    viewModel.deleteDataUser()
                    viewModel.clearLogout()
                    navigateToLoginPage()
                }

                null -> {}
            }
        }
    }
    //endregion

    //region profile
    private fun setupProfile() {
        viewModel.getUser().observe(viewLifecycleOwner) {user ->
            Glide
                .with(binding.root.context)
                .load(user.profilePicture)
                .placeholder(appR.drawable.img_profile)
                .error(appR.drawable.img_profile)
                .into(binding.imgProfile)
            binding.tvName.text = String.format("${user.firstName} ${user.lastName}")
            binding.tvEmail.text = user.email
        }

        binding.profile.setOnClickListener { navigateToProfilePage() }
    }
    //endregion

    private fun navigateToLoginPage() {
        val loginIntent = Intent(context, LoginActivity::class.java)
        startActivity(loginIntent)
        requireActivity().finish()
    }

    private fun navigateToProfilePage() {
        val intent = Intent(context, SettingsProfileActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val NOTIFICATION_ID = 1

        const val CHANNEL_ID = "push_notification"
        const val CHANNEL_NAME = "Push Notification"
        const val CHANNEL_DESC = "This channel used for push notification"
        const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH

        private const val USER_LOGGED_IN = "user_logged_in"

        const val APP_THEME = "app_theme"
        const val DARK_THEME = "Dark"
        const val LIGHT_THEME = "Light"
        const val SYSTEM_DEFAULT_THEME = "System Default"
    }
}
