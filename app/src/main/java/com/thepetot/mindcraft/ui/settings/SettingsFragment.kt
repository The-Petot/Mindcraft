package com.thepetot.mindcraft.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.content.res.Configuration
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.thepetot.mindcraft.R as appR
import com.thepetot.mindcraft.databinding.FragmentSettingsBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.login.LoginActivity
import com.thepetot.mindcraft.ui.onboarding.OnboardingActivity
import com.thepetot.mindcraft.ui.onboarding.OnboardingActivity.Companion.USER_LOGGED_IN
import com.thepetot.mindcraft.utils.SharedPreferencesManager

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

//    private lateinit var userPreference: UserPreference
//    private lateinit var profileHeader: LinearLayout
//    private lateinit var firstNameTextView: TextView
//    private lateinit var emailTextView: TextView
//    private lateinit var logoutTextView: TextView
//    private lateinit var twoStepSwitch: SwitchMaterial // Change to SwitchMaterial
//    private lateinit var notificationsSwitch: SwitchMaterial // Change to SwitchMaterial

//    // For requesting notification permission (Android 13+)
//    private val requestNotificationPermission =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                Snackbar.make(requireView(), "Notification permission granted", Snackbar.LENGTH_SHORT).show()
//            } else {
//                Snackbar.make(requireView(), "Notification permission denied", Snackbar.LENGTH_SHORT).show()
//            }
//        }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        userPreference = UserPreference.getInstance(context.dataStore)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

//        binding.profile.setOnClickListener {
//            val intent = Intent(context, SettingProfileActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.twoStepVerification.setOnClickListener {
//            val switch2fa = binding.switch2fa
//            switch2fa.isChecked = !switch2fa.isChecked
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        viewModel.getTheme(requireActivity().applicationContext)
        viewModel.currentThemeSetting.observe(viewLifecycleOwner) {
            binding.tvCurrentTheme.text = it
            when (it) {
                SYSTEM_DEFAULT_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                LIGHT_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                DARK_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }

//        // Bind UI elements
//        profileHeader = view.findViewById(R.id.profileHeader)
//        firstNameTextView = view.findViewById(R.id.firstNameTextView)
//        emailTextView = view.findViewById(R.id.emailTextView)
//        logoutTextView = view.findViewById(R.id.logoutTextView)
//        twoStepSwitch = view.findViewById(R.id.twoStepSwitch)
//        notificationsSwitch = view.findViewById(R.id.notificationsSwitch)
//
//        // Load user data and setup UI
//        loadUserData()
//
//        // Check notification permission (for Android 13+)
//        checkAndRequestNotificationPermission()
//
//        // Handle profile header click
//        profileHeader.setOnClickListener {
//            navigateToProfileSettings()
//        }
//
//        // Handle logout
//        logoutTextView.setOnClickListener {
//            logout()
//        }
//
//        // Handle switch actions
//        twoStepSwitch.setOnCheckedChangeListener { _, isChecked ->
//            toggleTwoStepVerification(isChecked)
//        }
//
//        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
//            handleNotifications(isChecked)
//        }

        setupButton()
    }

    private fun setupButton() {
        val switch2fa = binding.switch2fa
        val switchNotification = binding.switchNotification

        switch2fa.isChecked = viewModel.is2FASwitchChecked
        switch2fa.jumpDrawablesToCurrentState()

        switchNotification.isChecked = viewModel.isNotificationSwitchChecked
        switchNotification.jumpDrawablesToCurrentState()

        switch2fa.setOnCheckedChangeListener { _, isChecked ->
            viewModel.is2FASwitchChecked = isChecked
        }

        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isNotificationSwitchChecked = isChecked
        }

        binding.profile.setOnClickListener { navigateToProfilePage() }

        binding.twoStepVerification.setOnClickListener {
            switch2fa.isChecked = !viewModel.is2FASwitchChecked
        }

        binding.notification.setOnClickListener {
            switchNotification.isChecked = !viewModel.isNotificationSwitchChecked
        }

        binding.btnLogout.setOnClickListener {
            SharedPreferencesManager.set(requireActivity().applicationContext, USER_LOGGED_IN, false)
            viewModel.deleteDataUser()
            navigateToLoginPage()
        }

        binding.theme.setOnClickListener { showMenu() }
    }

    private fun showMenu() {
        val popup = PopupMenu(context, binding.menuTheme)
        popup.menuInflater.inflate(appR.menu.popup_menu, popup.menu)

        popup.setOnMenuItemClickListener {
            // TODO: Fix all icon color on dark mode
            when (it.itemId) {
                appR.id.system_default -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, SYSTEM_DEFAULT_THEME)
                    viewModel.getTheme(requireActivity().applicationContext)
                    true
                }
                appR.id.light -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, LIGHT_THEME)
                    viewModel.getTheme(requireActivity().applicationContext)
                    true
                }
                appR.id.dark -> {
                    SharedPreferencesManager.set(requireActivity().applicationContext, APP_THEME, DARK_THEME)
                    viewModel.getTheme(requireActivity().applicationContext)
                    true
                }
                else -> false
            }
        }

        // Respond to popup being dismissed.
        // popup.setOnDismissListener { }
        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun loadUserData() {
//        lifecycleScope.launch {
//            val user = userPreference.getUserData().first()
//            firstNameTextView.text = "${user.firstName.ifEmpty { "First Name" }} ${user.lastName.ifEmpty { "Last Name" }}"
//            emailTextView.text = user.email.ifEmpty { "email@example.com" }
//
//            // Set initial state of switches
//            twoStepSwitch.isChecked = user.is2FA
//            notificationsSwitch.isChecked = false // Example: Assume notifications are enabled by default
//        }
//    }
//
//    private fun navigateToProfileSettings() {
//        // Buat Intent untuk menuju ke SettingProfileActivity
//        val intent = Intent(requireContext(), SettingProfileActivity::class.java)
//        startActivity(intent) // Memulai activity
//    }
//
//
//    private fun toggleTwoStepVerification(isChecked: Boolean) {
//        lifecycleScope.launch {
//            val user = userPreference.getUserData().first()
//            val updatedUser = user.copy(is2FA = isChecked)
//            userPreference.saveUserData(updatedUser)
//
//            val message = if (isChecked) {
//                "Two-step verification enabled"
//            } else {
//                "Two-step verification disabled"
//            }
//            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun handleNotifications(isChecked: Boolean) {
//        if (isChecked) {
//            // Check if permission is granted before showing notifications
//            if (isNotificationPermissionGranted()) {
//                showNotification()
//            } else {
//                Snackbar.make(requireView(), "Notification permission required", Snackbar.LENGTH_SHORT).show()
//                notificationsSwitch.isChecked = false // Reset the switch state
//            }
//        } else {
//            val message = "Notifications disabled"
//            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun logout() {
//        lifecycleScope.launch {
//            userPreference.deleteUserData()
//            Snackbar.make(requireView(), "Logged out successfully", Snackbar.LENGTH_SHORT).show()
//            // Navigate to login or close the app
//            requireActivity().finish()
//        }
//    }
//
//    // Function to check if notification permission is granted
//    private fun isNotificationPermissionGranted(): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true // No permission required for versions below Android 13
//        }
//    }
//
//    // Function to show a notification when notifications are enabled
//    private fun showNotification() {
//        val channelId = "mindcraft_notifications"
//        val notificationId = 1
//
//        // Create NotificationChannel for Android O or above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "General Notifications",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                description = "MindCraft notification channel"
//            }
//            val notificationManager: NotificationManager =
//                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Create the notification using NotificationCompat
//        val notification = NotificationCompat.Builder(requireContext(), channelId)
//            .setSmallIcon(R.drawable.ic_launcher_mindcraft_foreground) // Replace with your icon
//            .setContentTitle("Notifications Enabled")
//            .setContentText("You have successfully enabled notifications.")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .build()
//
//        // Show the notification
//        try {
//            // Existing notification creation and display code
//            with(NotificationManagerCompat.from(requireContext())) {
//                notify(notificationId, notification)
//            }
//        } catch (e: SecurityException) {
//            // Handle potential permission-related exceptions
//            Snackbar.make(requireView(), "Unable to show notification. Permission denied.", Snackbar.LENGTH_SHORT).show()
//        }
//    }
//
//    // Check and request notification permission for Android 13 and above
//    private fun checkAndRequestNotificationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            when {
//                ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) == PackageManager.PERMISSION_GRANTED -> {
//                    // Permission already granted
//                }
//                else -> {
//                    // Request permission
//                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
//                }
//            }
//        }
//    }

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
        const val USER_LOGGED_IN = "user_logged_in"

        const val APP_THEME = "app_theme"
        const val DARK_THEME = "Dark"
        const val LIGHT_THEME = "Light"
        const val SYSTEM_DEFAULT_THEME = "System Default"
    }
}
