package com.thepetot.mindcraft.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.repository.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {
    var is2FASwitchChecked: Boolean = false
    var isNotificationSwitchChecked: Boolean = false
    var isThemeSwitchChecked: Boolean = false

    fun deleteDataUser() {
        viewModelScope.launch {
            userRepository.deleteUserData()
        }
    }
}