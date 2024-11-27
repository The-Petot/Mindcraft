package com.thepetot.mindcraft.ui.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.thepetot.mindcraft.R
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingFragment : Fragment() {

    private lateinit var userPreference: UserPreference
    private lateinit var profileHeader: LinearLayout
    private lateinit var firstNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutTextView: TextView
    private lateinit var twoStepSwitch: SwitchMaterial // Change to SwitchMaterial
    private lateinit var notificationsSwitch: SwitchMaterial // Change to SwitchMaterial

    // For requesting notification permission (Android 13+)
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Snackbar.make(requireView(), "Notification permission granted", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(requireView(), "Notification permission denied", Snackbar.LENGTH_SHORT).show()
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        userPreference = UserPreference.getInstance(context.dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind UI elements
        profileHeader = view.findViewById(R.id.profileHeader)
        firstNameTextView = view.findViewById(R.id.firstNameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        logoutTextView = view.findViewById(R.id.logoutTextView)
        twoStepSwitch = view.findViewById(R.id.twoStepSwitch)
        notificationsSwitch = view.findViewById(R.id.notificationsSwitch)

        // Load user data and setup UI
        loadUserData()

        // Check notification permission (for Android 13+)
        checkAndRequestNotificationPermission()

        // Handle profile header click
        profileHeader.setOnClickListener {
            navigateToProfileSettings()
        }

        // Handle logout
        logoutTextView.setOnClickListener {
            logout()
        }

        // Handle switch actions
        twoStepSwitch.setOnCheckedChangeListener { _, isChecked ->
            toggleTwoStepVerification(isChecked)
        }

        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleNotifications(isChecked)
        }
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val user = userPreference.getDataUser().first()
            firstNameTextView.text = "${user.firstName.ifEmpty { "First Name" }} ${user.lastName.ifEmpty { "Last Name" }}"
            emailTextView.text = user.email.ifEmpty { "email@example.com" }

            // Set initial state of switches
            twoStepSwitch.isChecked = user.is2FA
            notificationsSwitch.isChecked = false // Example: Assume notifications are enabled by default
        }
    }

    private fun navigateToProfileSettings() {
        // Buat Intent untuk menuju ke SettingProfileActivity
        val intent = Intent(requireContext(), SettingProfileActivity::class.java)
        startActivity(intent) // Memulai activity
    }


    private fun toggleTwoStepVerification(isChecked: Boolean) {
        lifecycleScope.launch {
            val user = userPreference.getDataUser().first()
            val updatedUser = user.copy(is2FA = isChecked)
            userPreference.saveDataUser(updatedUser)

            val message = if (isChecked) {
                "Two-step verification enabled"
            } else {
                "Two-step verification disabled"
            }
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun handleNotifications(isChecked: Boolean) {
        if (isChecked) {
            // Check if permission is granted before showing notifications
            if (isNotificationPermissionGranted()) {
                showNotification()
            } else {
                Snackbar.make(requireView(), "Notification permission required", Snackbar.LENGTH_SHORT).show()
                notificationsSwitch.isChecked = false // Reset the switch state
            }
        } else {
            val message = "Notifications disabled"
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            userPreference.clearDataUser()
            Snackbar.make(requireView(), "Logged out successfully", Snackbar.LENGTH_SHORT).show()
            // Navigate to login or close the app
            requireActivity().finish()
        }
    }

    // Function to check if notification permission is granted
    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No permission required for versions below Android 13
        }
    }

    // Function to show a notification when notifications are enabled
    private fun showNotification() {
        val channelId = "mindcraft_notifications"
        val notificationId = 1

        // Create NotificationChannel for Android O or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "MindCraft notification channel"
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification using NotificationCompat
        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_launcher_mindcraft_foreground) // Replace with your icon
            .setContentTitle("Notifications Enabled")
            .setContentText("You have successfully enabled notifications.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Show the notification
        try {
            // Existing notification creation and display code
            with(NotificationManagerCompat.from(requireContext())) {
                notify(notificationId, notification)
            }
        } catch (e: SecurityException) {
            // Handle potential permission-related exceptions
            Snackbar.make(requireView(), "Unable to show notification. Permission denied.", Snackbar.LENGTH_SHORT).show()
        }
    }

    // Check and request notification permission for Android 13 and above
    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                }
                else -> {
                    // Request permission
                    requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}
