package com.thepetot.mindcraft.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.thepetot.mindcraft.data.remote.response.signup.SignupBody
import com.thepetot.mindcraft.data.remote.response.signup.SignupResponse
import com.thepetot.mindcraft.data.repository.UserRepository
import com.thepetot.mindcraft.utils.Result


class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signupResult = MediatorLiveData<Result<SignupResponse>?>()
    val signupResult: LiveData<Result<SignupResponse>?> get() = _signupResult

    fun signup(firstName: String, lastName: String, email: String, password: String) {
        val signupBody = SignupBody(firstName, lastName, password, email)
        val source = userRepository.signup(signupBody)
        _signupResult.addSource(source) { result ->
            _signupResult.value = result

            // Remove the source when the operation is complete
            if (result is Result.Success || result is Result.Error) {
                _signupResult.removeSource(source)
            }
        }
    }

    fun clearSignup() {
        _signupResult.value = null
    }
}
