package com.thepetot.mindcraft.ui.onboarding

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.PathInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.thepetot.mindcraft.databinding.ActivityOnboardingBinding
import com.thepetot.mindcraft.ui.adapter.OnboardingAdapter
import com.thepetot.mindcraft.ui.login.LoginActivity
import com.thepetot.mindcraft.ui.main.MainActivity
import com.thepetot.mindcraft.ui.settings.SettingsFragment.Companion.CHANNEL_DESC
import com.thepetot.mindcraft.ui.settings.SettingsFragment.Companion.CHANNEL_ID
import com.thepetot.mindcraft.ui.settings.SettingsFragment.Companion.CHANNEL_IMPORTANCE
import com.thepetot.mindcraft.ui.settings.SettingsFragment.Companion.CHANNEL_NAME
import com.thepetot.mindcraft.utils.SharedPreferencesManager
import com.thepetot.mindcraft.utils.logMessage
import com.thepetot.mindcraft.utils.setCurrentItem
import kotlin.math.abs

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupCheck()
        enableEdgeToEdge()

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(
                // Notice we're using systemBars, not statusBar
                WindowInsetsCompat.Type.systemBars()
                        // Notice we're also accounting for the display cutouts
                        or WindowInsetsCompat.Type.displayCutout()
                // If using EditText, also add
                // "or WindowInsetsCompat.Type.ime()"
                // to maintain focus when opening the IME
            )
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buildNotificationChannel()
        initViewPager()
        setupButtons()
    }

    private fun setupCheck() {
        when (SharedPreferencesManager.get(applicationContext, APP_THEME, SYSTEM_DEFAULT_THEME)) {
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

        if (SharedPreferencesManager.get(applicationContext, USER_LOGGED_IN, false)) {
            navigateToMainPage()
        } else if (SharedPreferencesManager.get(applicationContext, ONBOARDING_FINISHED, false)) {
            navigateToLoginPage()
        }
    }

    private fun initViewPager() {
        viewPager = binding.viewPager
        val dotsIndicator = binding.dotsIndicator
        val onboardingAdapter = OnboardingAdapter(this)

        viewPager.adapter = onboardingAdapter
        dotsIndicator.attachTo(viewPager)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonVisibility(position)
            }
        })

        viewPager.setPageTransformer { page, position ->
            page.translationX = -position * page.width
            page.alpha = 1 - abs(position)
            val interpolator = PathInterpolator(0.2f, 0f, 0.8f, 1f) // Adjusted for a flatter curve
            val interpolatedPosition = interpolator.getInterpolation(abs(position))
            page.translationX *= interpolatedPosition
        }
    }

    private fun setupButtons() {
        binding.btnNext.setOnClickListener { nextButton() }
        binding.btnBack.setOnClickListener { backButton() }
    }

    private fun nextButton() {
        val nextPage = viewPager.currentItem + 1
        if (nextPage < (viewPager.adapter?.itemCount ?: 0)) {
            viewPager.setCurrentItem(nextPage, duration = 500)
        } else {
            SharedPreferencesManager.set(applicationContext, ONBOARDING_FINISHED, true)
            navigateToLoginPage()
        }
    }

    private fun backButton() {
        val previousPage = viewPager.currentItem - 1
        if (previousPage >= 0) {
            viewPager.setCurrentItem(previousPage, duration = 500)
        }
    }

    private fun navigateToLoginPage() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun navigateToMainPage() {
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    private fun updateButtonVisibility(position: Int) {
        binding.btnBack.visibility = if (position == 0) View.GONE else View.VISIBLE
    }

    private fun buildNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel != null) {
                logMessage(Log::w, "Notification", "Notification channel already exists")
                return
            }

            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, CHANNEL_IMPORTANCE).apply {
                description = CHANNEL_DESC
                enableLights(true)
                lightColor = android.graphics.Color.GREEN // Light color
                enableVibration(true) // Enable vibration
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val ONBOARDING_FINISHED = "onboarding_finished"
        const val USER_LOGGED_IN = "user_logged_in"

        const val APP_THEME = "app_theme"
        const val DARK_THEME = "Dark"
        const val LIGHT_THEME = "Light"
        const val SYSTEM_DEFAULT_THEME = "System Default"
    }
}