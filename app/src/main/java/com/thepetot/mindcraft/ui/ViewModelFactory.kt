package com.thepetot.mindcraft.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.ui.login.LoginViewModel
import com.thepetot.mindcraft.ui.signup.RegisterViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            else -> throw java.lang.IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}