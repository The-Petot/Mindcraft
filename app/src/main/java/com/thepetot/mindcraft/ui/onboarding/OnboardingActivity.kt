package com.thepetot.mindcraft.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.thepetot.mindcraft.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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

        // Add page change callback to manage button visibility
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonVisibility(position)
            }
        })
    }

    private fun setupButtonListeners() {
        // Next button functionality
        binding.btnNext.setOnClickListener {
            val nextPage = viewPager.currentItem + 1
            if (nextPage < (viewPager.adapter?.itemCount ?: 0)) {
                viewPager.currentItem = nextPage
            }
        }

        // Back button functionality
        binding.btnBack.setOnClickListener {
            val previousPage = viewPager.currentItem - 1
            if (previousPage >= 0) {
                viewPager.currentItem = previousPage
            }
        }
    }

    private fun updateButtonVisibility(position: Int) {
        // Hide back button on the first page
        binding.btnBack.visibility = if (position == 0) View.GONE else View.VISIBLE
    }
}