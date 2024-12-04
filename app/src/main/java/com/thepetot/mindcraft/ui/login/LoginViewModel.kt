package com.thepetot.mindcraft.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.remote.response.login.LoginBody
import com.thepetot.mindcraft.data.remote.response.login.LoginResponse
import com.thepetot.mindcraft.data.remote.response.login.OAuthLoginBody
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginResult = MediatorLiveData<Result<LoginResponse>?>()
    val loginResult: LiveData<Result<LoginResponse>?> get() = _loginResult

    private val _loginOTPResult = MediatorLiveData<Result<LoginResponse>?>()
    val loginOTPResult: LiveData<Result<LoginResponse>?> get() = _loginOTPResult

    var email: String = ""
    var password: String = ""

    fun login(email: String, password: String) {
        val loginBody = LoginBody(password, email, null)
        val source = userRepository.login(loginBody)
        _loginResult.addSource(source) { result ->
            _loginResult.value = result

            // Remove the source when the operation is complete
            if (result is Result.Success || result is Result.Error) {
                _loginResult.removeSource(source)
            }
        }
    }

    fun loginOTP(email: String, password: String, token: String) {
        val loginBody = LoginBody(password, email, token)
        val source = userRepository.login(loginBody)
        _loginOTPResult.addSource(source) { result ->
            _loginOTPResult.value = result

            // Remove the source when the operation is complete
            if (result is Result.Success || result is Result.Error) {
                _loginOTPResult.removeSource(source)
            }
        }
    }

    fun clearLogin() {
        _loginResult.value = null
    }

    fun clearOTPLogin() {
        _loginOTPResult.value = null
    }

    fun oAuthLogin(token: String) {
        val oAuthLoginBody = OAuthLoginBody(token)
        val source = userRepository.oAuthLogin(oAuthLoginBody)
        _loginResult.addSource(source) { result ->
            _loginResult.value = result

            // Remove the source when the operation is complete
            if (result is Result.Success || result is Result.Error) {
                _loginResult.removeSource(source)
            }
        }
    }

    fun saveUserData(user: LoginResponse) {
        viewModelScope.launch {
            val currentUser = UserModel(
                user.data.id,
                user.data.firstName,
                user.data.lastName,
                user.data.email,
                user.data.totalScore,
                user.data.currentRank,
                user.data.profileImgUrl,
                user.data.twoFactorEnabled,
                user.data.twoFactorSecret,
                user.data.notificationEnabled,
                user.data.createdAt,
                user.data.updatedAt,
                user.data.accessToken,
                user.data.refreshToken,
                user.data.sessionId
            )
            userRepository.saveUserData(currentUser)
        }
    }
}
