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
import com.thepetot.mindcraft.utils.setCurrentItem
import kotlin.math.abs

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        sharedPref = this.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        if (isOnboardingFinished()) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

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
            // Apply translation
            page.translationX = -position * page.width

            // Adjust alpha for fade effect
            page.alpha = 1 - abs(position)

            // Custom Bezier curve for slower transition
            val interpolator = PathInterpolator(0.2f, 0f, 0.8f, 1f) // Adjusted for a flatter curve

            // Apply interpolation
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
                setOnboardingFinished()
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

    private fun setOnboardingFinished() {
        sharedPref.edit().putBoolean(FINISHED_KEY, true).apply()
    }

    private fun isOnboardingFinished(): Boolean {
        return sharedPref.getBoolean(FINISHED_KEY, false)
    }

    private fun updateButtonVisibility(position: Int) {
        binding.btnBack.visibility = if (position == 0) View.GONE else View.VISIBLE
    }

    companion object {
        const val PREF_NAME = "onboarding"
        const val FINISHED_KEY = "finished"
    }
}