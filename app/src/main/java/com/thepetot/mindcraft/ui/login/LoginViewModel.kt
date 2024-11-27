package com.thepetot.mindcraft.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.remote.response.login.LoginBody
import com.thepetot.mindcraft.data.remote.response.login.LoginResponse
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginResult = MediatorLiveData<Result<UserModel>>()
    val loginResult: LiveData<Result<UserModel>> get() = _loginResult

    fun login(email: String, password: String, token: String? = null) {
        val loginBody = LoginBody(password, email, token)
        val source = userRepository.login(loginBody)
        _loginResult.addSource(source) { result ->
            _loginResult.value = result

            // Remove the source when the operation is complete
            if (result is Result.Success || result is Result.Error) {
                _loginResult.removeSource(source)
            }
        }
    }

    fun saveUserData(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveUserData(user)
        }
    }

    fun getUserData() = userRepository.getUserData().asLiveData()
}
