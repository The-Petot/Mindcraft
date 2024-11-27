package com.thepetot.mindcraft.ui.onboarding

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.PathInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.thepetot.mindcraft.databinding.ActivityOnboardingBinding
import com.thepetot.mindcraft.ui.adapter.OnboardingAdapter
import com.thepetot.mindcraft.ui.login.LoginActivity
import com.thepetot.mindcraft.ui.main.MainActivity
import com.thepetot.mindcraft.utils.SharedPreferencesManager
import com.thepetot.mindcraft.utils.setCurrentItem
import kotlin.math.abs

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (SharedPreferencesManager.getBoolean(applicationContext, USER_LOGGED_IN)) {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
            return
        }

        if (SharedPreferencesManager.getBoolean(applicationContext, ONBOARDING_FINISHED)) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        enableEdgeToEdge()

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViewPager()
        setupButtonListeners()
    }

    private fun initializeViewPager() {
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

    private fun setupButtonListeners() {
        binding.btnNext.setOnClickListener {
            val nextPage = viewPager.currentItem + 1
            if (nextPage < (viewPager.adapter?.itemCount ?: 0)) {
//                viewPager.currentItem = nextPage
                viewPager.setCurrentItem(nextPage, duration = 500)
            } else {
                SharedPreferencesManager.saveBoolean(applicationContext, ONBOARDING_FINISHED, true)
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }

        binding.btnBack.setOnClickListener {
            val previousPage = viewPager.currentItem - 1
            if (previousPage >= 0) {
                viewPager.setCurrentItem(previousPage, duration = 500)
            }
        }
    }

    private fun updateButtonVisibility(position: Int) {
        binding.btnBack.visibility = if (position == 0) View.GONE else View.VISIBLE
    }

    companion object {
        const val ONBOARDING_FINISHED = "onboarding_finished"
        const val USER_LOGGED_IN = "user_logged_in"
    }
}