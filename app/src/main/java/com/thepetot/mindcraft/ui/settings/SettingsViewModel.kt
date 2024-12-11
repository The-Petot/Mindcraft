package com.thepetot.mindcraft.ui.settings

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.remote.response.logout.LogoutBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutResponse
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorBody
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorResponse
import com.thepetot.mindcraft.data.remote.response.user.update.UpdateUserResponse
import com.thepetot.mindcraft.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.thepetot.mindcraft.utils.Result
import java.io.File

class SettingsViewModel(private val userRepository: UserRepository) : ViewModel() {
    var is2FASwitchChecked: Boolean = false
    var isNotificationSwitchChecked: Boolean = false
    var currentTheme: String? = null
    var secretCode: String? = null

    private val _logoutResult = MediatorLiveData<Result<LogoutResponse>?>()
    val logoutResult: LiveData<Result<LogoutResponse>?> get() = _logoutResult

    private val _setTwoFactorResult = MediatorLiveData<Result<TwoFactorResponse>?>()
    val setTwoFactorResult: LiveData<Result<TwoFactorResponse>?> get() = _setTwoFactorResult

    private val _getTwoFactorResult = MediatorLiveData<Result<TwoFactorResponse>?>()
    val getTwoFactorResult: LiveData<Result<TwoFactorResponse>?> get() = _getTwoFactorResult

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

    fun setTwoFactor(enable: Boolean, userId: Int, secret: String, token: String) {
        val twoFactorBody = TwoFactorBody(userId, secret, token)
        val source = userRepository.setTwoFactor(enable, twoFactorBody)
        _setTwoFactorResult.addSource(source) {
            _setTwoFactorResult.value = it
            if (it is Result.Success || it is Result.Error) {
                _setTwoFactorResult.removeSource(source)
            }
        }
    }

    fun getTwoFactor() {
        val source = userRepository.getTwoFactor()
        _getTwoFactorResult.addSource(source) {
            _getTwoFactorResult.value = it
            if (it is Result.Success || it is Result.Error) {
                _getTwoFactorResult.removeSource(source)
            }
        }
    }

    fun clearSetTwoFactor() {
        _setTwoFactorResult.value = null
    }

    fun clearGetTwoFactor() {
        _getTwoFactorResult.value = null
    }

    fun clearLogout() {
        _logoutResult.value = null
    }

    fun <T> setPreferenceSettings(pref: Preferences.Key<T>, value: T) {
        viewModelScope.launch {
            userRepository.setPreferenceSettings(pref, value)
        }
    }

    suspend fun <T> setPreferenceSettingsSuspend(pref: Preferences.Key<T>, value: T) {
        userRepository.setPreferenceSettings(pref, value)
    }

    fun <T> deletePreferenceSettings(pref: Preferences.Key<T>) {
        viewModelScope.launch {
            userRepository.deletePreferenceSettings(pref)
        }
    }

    fun <T> getPreferenceSettings(pref: Preferences.Key<T>, defaultValue: T) = runBlocking {
        userRepository.getPreferenceSettings(pref, defaultValue).first()
    }

    fun getUser() = userRepository.getUserData().asLiveData()

    fun updateUser(
        userId: Int,
        image: File? = null,
        firstName: String? = null,
        lastName: String? = null,
        email: String? = null,
        password: String? = null
    ): LiveData<Result<UpdateUserResponse>> = userRepository.updateUser(userId, image, firstName, lastName, email, password)

    fun deleteDataUser() {
        viewModelScope.launch {
            userRepository.deleteUserData()
        }
    }
}