package com.thepetot.mindcraft.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.Intent
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.thepetot.mindcraft.R as appR
import com.thepetot.mindcraft.databinding.FragmentSettingsBinding
import com.thepetot.mindcraft.ui.ViewModelFactory
import com.thepetot.mindcraft.ui.login.LoginActivity
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

        val switch2fa = binding.switch2fa

        switch2fa.isChecked = viewModel.is2FASwitchChecked
        switch2fa.jumpDrawablesToCurrentState()

        switch2fa.setOnCheckedChangeListener { _, isChecked ->
            viewModel.is2FASwitchChecked = isChecked
        }

        binding.profile.setOnClickListener {
            val intent = Intent(context, SettingsProfileActivity::class.java)
            startActivity(intent)
        }

        binding.twoStepVerification.setOnClickListener {
            switch2fa.isChecked = !viewModel.is2FASwitchChecked
        }

        binding.btnLogout.setOnClickListener {
            SharedPreferencesManager.set(requireActivity().applicationContext, USER_LOGGED_IN, false)
            viewModel.deleteDataUser()
            navigateToLoginPage()
        }


        binding.theme.setOnClickListener {
            showMenu()
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
    }

    private fun showMenu() {
        val popup = PopupMenu(context, binding.menuTheme)
        popup.menuInflater.inflate(appR.menu.popup_menu, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                appR.id.system_default -> {

                    true
                }
                appR.id.light -> {

                    true
                }
                appR.id.dark -> {

                    true
                }
                else -> {
                    true
                }
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
        val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(loginIntent)
        requireActivity().finish()
    }
}
