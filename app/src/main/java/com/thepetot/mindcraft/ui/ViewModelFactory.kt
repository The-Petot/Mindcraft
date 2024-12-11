package com.thepetot.mindcraft.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.repository.ChallengesRepository
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.di.Injection
import com.thepetot.mindcraft.ui.home.HomeViewModel
import com.thepetot.mindcraft.ui.home.quiz.add.AddQuizViewModel
import com.thepetot.mindcraft.ui.home.quiz.detail.DetailQuizViewModel
import com.thepetot.mindcraft.ui.login.LoginViewModel
import com.thepetot.mindcraft.ui.settings.SettingsViewModel
import com.thepetot.mindcraft.ui.signup.SignupViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val challengesRepository: ChallengesRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AddQuizViewModel::class.java) -> {
                AddQuizViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(challengesRepository, userRepository) as T
            }
            modelClass.isAssignableFrom(DetailQuizViewModel::class.java) -> {
                DetailQuizViewModel(challengesRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideChallengesRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}