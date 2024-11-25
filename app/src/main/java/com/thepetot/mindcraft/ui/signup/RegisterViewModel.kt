package com.thepetot.mindcraft.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.RegisterResponse
import com.thepetot.mindcraft.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerSuccess = MutableLiveData(false)
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    fun register(email: String, password: String, firstName: String, lastName: String) {
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val registerUser = apiService.register(email, password, firstName, lastName)
        registerUser.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Simpan data pengguna ke UserPreference
                        saveUser(UserModel(firstName, lastName, email, password, false))
                        _registerSuccess.postValue(true)
                        Log.e(TAG, "Register berhasil: ${responseBody.message}")
                    }
                } else {
                    Log.e(TAG, "Register gagal: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Register gagal: ${t.message.toString()}")
            }
        })
    }

    private fun saveUser(dataUser: UserModel) {
        viewModelScope.launch {
            pref.saveUser(dataUser)
        }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}
