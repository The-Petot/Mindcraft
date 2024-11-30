package com.thepetot.mindcraft.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {
    var is2FASwitchChecked: Boolean = false
    var isNotificationSwitchChecked: Boolean = false

    private val _currentThemeSetting = MutableLiveData<String>()
    val currentThemeSetting: LiveData<String> get() = _currentThemeSetting

    fun getTheme(context: Context) {
        viewModelScope.launch {
            val theme = SharedPreferencesManager.get(context, SettingsFragment.APP_THEME, SettingsFragment.SYSTEM_DEFAULT_THEME)
            _currentThemeSetting.value = theme
        }
    }

    fun deleteDataUser() {
        viewModelScope.launch {
            userRepository.deleteUserData()
        }
    }
}