package com.thepetot.mindcraft.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thepetot.mindcraft.ui.onboarding.OnboardingFirst
import com.thepetot.mindcraft.ui.onboarding.OnboardingSecond
import com.thepetot.mindcraft.ui.onboarding.OnboardingThird

class OnboardingAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OnboardingFirst()
            1 -> fragment = OnboardingSecond()
            2 -> fragment = OnboardingThird()
        }
        return fragment as Fragment
    }
}