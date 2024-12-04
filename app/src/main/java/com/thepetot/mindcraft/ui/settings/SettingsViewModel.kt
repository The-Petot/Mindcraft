package com.thepetot.mindcraft.ui.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.remote.response.logout.LogoutBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutResponse
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorBody
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorResponse
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.thepetot.mindcraft.utils.Result

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {
    var is2FASwitchChecked: Boolean = false
    var isNotificationSwitchChecked: Boolean = false
    var currentTheme: String? = null
    var secretCode: String? = null

    private val _logoutResult = MediatorLiveData<Result<LogoutResponse>?>()
    val logoutResult: LiveData<Result<LogoutResponse>?> get() = _logoutResult

    private val _twoFactorResult = MediatorLiveData<Result<TwoFactorResponse>?>()
    val twoFactorResult: LiveData<Result<TwoFactorResponse>?> get() = _twoFactorResult

    fun logout(userId: Int) {
        val logoutBody = LogoutBody(userId)
        val source = userRepository.logout(logoutBody)
        _logoutResult.addSource(source) { result ->
            _logoutResult.value = result

            // Remove the source when the operation is complete
            if (result is Result.Success || result is Result.Error) {
                _logoutResult.removeSource(source)
            }
        }
    }

    fun twoFactor(enable: Boolean, userId: Int, secret: String, token: String) {
        val twoFactorBody = TwoFactorBody(userId, secret, token)
        val source = userRepository.twoFactor(enable, twoFactorBody)
        _twoFactorResult.addSource(source) {
            _twoFactorResult.value = it
            if (it is Result.Success || it is Result.Error) {
                _twoFactorResult.removeSource(source)
            }
        }
    }

    fun clearTwoFactor() {
        _twoFactorResult.value = null
    }

    fun clearLogout() {
        _logoutResult.value = null
    }

    fun <T> setPreferenceSettings(pref: Preferences.Key<T>, value: T) {
        viewModelScope.launch {
            userRepository.setPreferenceSettings(pref, value)
        }
    }

    fun <T> deletePreferenceSettings(pref: Preferences.Key<T>) {
        viewModelScope.launch {
            userRepository.deletePreferenceSettings(pref)
        }
    }

    fun <T> getPreferenceSettings(pref: Preferences.Key<T>, defaultValue: T) = runBlocking {
        userRepository.getPreferenceSettings(pref, defaultValue).first()
    }

    fun deleteDataUser() {
        viewModelScope.launch {
            userRepository.deleteUserData()
        }
    }
}