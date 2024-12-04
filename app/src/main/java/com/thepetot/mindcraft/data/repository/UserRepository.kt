package com.thepetot.mindcraft.data.repository

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.response.ErrorResponse
import com.thepetot.mindcraft.data.remote.response.login.LoginBody
import com.thepetot.mindcraft.data.remote.response.login.LoginResponse
import com.thepetot.mindcraft.data.remote.response.login.OAuthLoginBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutResponse
import com.thepetot.mindcraft.data.remote.response.signup.SignupBody
import com.thepetot.mindcraft.data.remote.response.signup.SignupResponse
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorBody
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorResponse
import com.thepetot.mindcraft.data.remote.response.user.UserResponse
import com.thepetot.mindcraft.data.remote.retrofit.ApiService
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.logMessage
import kotlinx.coroutines.flow.Flow
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
                    val parsedError = gson.fromJson(errorBody, SignupResponse::class.java)
//                    val message = parsedError.errors[0].messages[0]
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(TAG, "Error parsing HTTP exception response: ${parseException.message}")
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage(TAG, "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(loginBody: LoginBody): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(loginBody)

            if (response.isSuccessful) {
                val header = response.headers()
                val body = response.body()
                body?.data?.accessToken = header["Authorization"]!!
                body?.data?.refreshToken = header["X-Refresh-Token"]!!
                body?.data?.sessionId = header["X-Session-Id"]!!

//                // TODO: Need coordination with CC
//                val authorization = header["Authorization"]
//                val refreshToken = header["X-Refresh-Token"]
//                val sessionId = header["X-Session-Id"]
//                val userId = response.body()?.data?.userId

//                val user = UserModel(
//                    userId!!,
//                    "Admin",
//                    "Test",
//                    loginBody.email,
//                    loginBody.password,
//                    "image::/",
//                    true,
//                    authorization!!,
//                    refreshToken!!,
//                    sessionId!!
//                )

                emit(Result.Success(body!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, LoginResponse::class.java)
//                    val message = parsedError.errors[0].messages[0]
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(TAG, "Error parsing HTTP exception response: ${parseException.message}")
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage(TAG, "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun oAuthLogin(oAuthLoginBody: OAuthLoginBody): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.oAuthLogin(oAuthLoginBody)

            if (response.isSuccessful) {
                val header = response.headers()
                val body = response.body()
                body?.data?.accessToken = header["Authorization"]!!
                body?.data?.refreshToken = header["X-Refresh-Token"]!!
                body?.data?.sessionId = header["X-Session-Id"]!!

//                // TODO: Need coordination with CC
//                val authorization = header["Authorization"]
//                val refreshToken = header["X-Refresh-Token"]
//                val sessionId = header["X-Session-Id"]
//                val userId = response.body()?.data?.userId

//                val user = UserModel(
//                    userId!!,
//                    "Admin",
//                    "Test",
//                    loginBody.email,
//                    loginBody.password,
//                    "image::/",
//                    true,
//                    authorization!!,
//                    refreshToken!!,
//                    sessionId!!
//                )

                emit(Result.Success(body!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, LoginResponse::class.java)
//                    val message = parsedError.errors[0].messages[0]
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(TAG, "Error parsing HTTP exception response: ${parseException.message}")
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage(TAG, "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun logout(logoutBody: LogoutBody): LiveData<Result<LogoutResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.logout(logoutBody)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, LogoutResponse::class.java)
                    val message = parsedError.errors[0].messages[0]
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(
                        TAG,
                        "Error parsing HTTP exception response: ${parseException.message}"
                    )
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage(TAG, "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun twoFactor(enable: Boolean, twoFactorBody: TwoFactorBody): LiveData<Result<TwoFactorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.twoFactor(enable, twoFactorBody)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, TwoFactorResponse::class.java)
//                    val message = parsedError.errors[0].messages[0]
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(
                        TAG,
                        "Error parsing HTTP exception response: ${parseException.message}"
                    )
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage(TAG, "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserById(userId: Int): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserById(userId)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, UserResponse::class.java)
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(
                        TAG,
                        "Error parsing HTTP exception response: ${parseException.message}"
                    )
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage(TAG, "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveUserData(user: UserModel) = userPreference.saveUserData(user)
    fun getUserData(): Flow<UserModel> = userPreference.getUserData()
    suspend fun deleteUserData() = userPreference.deleteUserData()

    suspend fun <T> setPreferenceSettings(pref: Preferences.Key<T>, value: T) = userPreference.setPreferenceSettings(pref, value)
    fun <T> getPreferenceSettings(pref: Preferences.Key<T>, defaultValue: T): Flow<T> = userPreference.getPreferenceSettings(pref, defaultValue)
    suspend fun <T> deletePreferenceSettings(pref: Preferences.Key<T>) = userPreference.deletePreferenceSettings(pref)

    companion object {
        const val TAG = "UserRepository"

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