package com.thepetot.mindcraft.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.ErrorResponse
import com.thepetot.mindcraft.data.remote.response.login.LoginBody
import com.thepetot.mindcraft.data.remote.response.login.LoginResponse
import com.thepetot.mindcraft.data.remote.response.signup.SignupBody
import com.thepetot.mindcraft.data.remote.response.signup.SignupResponse
import com.thepetot.mindcraft.data.remote.retrofit.ApiService
import com.thepetot.mindcraft.utils.Result
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    fun signup(signupBody: SignupBody): LiveData<Result<SignupResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.signup(signupBody)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, ErrorResponse::class.java)
                    emit(Result.Error(parsedError.error))
                } catch (parseException: Exception) {
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(loginBody: LoginBody): LiveData<Result<UserModel>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(loginBody)

            if (response.isSuccessful) {
                val header = response.headers()

                // TODO: Need coordination with CC
                val authorization = header["Authorization"]
                val refreshToken = header["X-Refresh-Token"]
                val sessionId = header["X-Session-Id"]
                val userId = response.body()?.data?.userId

                val user = UserModel(
                    userId!!,
                    "Admin",
                    "Test",
                    loginBody.email,
                    loginBody.password,
                    "image::/",
                    true,
                    false,
                    authorization!!,
                    refreshToken!!,
                    sessionId!!
                )

                emit(Result.Success(user))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, ErrorResponse::class.java)
                    emit(Result.Error(parsedError.error))
                } catch (parseException: Exception) {
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveUserData(user: UserModel) = userPreference.saveUserData(user)

    fun getUserData() = userPreference.getUserData()

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}