package com.thepetot.mindcraft.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.LoginResponse
import com.thepetot.mindcraft.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveUserData(userId: Int, token: String) {
        viewModelScope.launch {
            pref.saveUserData(userId, token)
        }
    }

    fun loginUser() {
        viewModelScope.launch {
            pref.login()
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccess = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val loginUser = apiService.login(email, password)
        loginUser.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "User logged in successfully") {
                        val userId = responseBody.data.userId
                        val token = response.headers()["Authorization"] ?: ""
                        saveUserData(userId, token)
                        _loginSuccess.postValue(true)
                        Log.d(TAG, "Login berhasil: userId=$userId, token=$token")
                    }
                } else {
                    Log.e(TAG, "Login gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Login error: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
