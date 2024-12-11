package com.thepetot.mindcraft.data.repository

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.thepetot.mindcraft.data.pref.UserModel
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.remote.body.auth.TokenRefreshBody
import com.thepetot.mindcraft.data.remote.body.users.CreateParticipationBody
import com.thepetot.mindcraft.data.remote.response.auth.refresh_token.RefreshTokenResponse
//import com.thepetot.mindcraft.data.remote.response.challenges.ChallengeData
//import com.thepetot.mindcraft.data.remote.response.challenges.ChallengesResponse
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeBody
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeResponse
import com.thepetot.mindcraft.data.remote.response.error.ErrorResponse
//import com.thepetot.mindcraft.data.remote.response.challenges.ParticipantData
//import com.thepetot.mindcraft.data.remote.response.challenges.QuestionData
import com.thepetot.mindcraft.data.remote.response.login.LoginBody
import com.thepetot.mindcraft.data.remote.response.login.LoginResponse
import com.thepetot.mindcraft.data.remote.response.login.OAuthLoginBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutResponse
import com.thepetot.mindcraft.data.remote.response.participations.CreateParticipationsResponse
import com.thepetot.mindcraft.data.remote.response.signup.SignupBody
import com.thepetot.mindcraft.data.remote.response.signup.SignupResponse
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorBody
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorResponse
import com.thepetot.mindcraft.data.remote.response.user.UserResponse
import com.thepetot.mindcraft.data.remote.response.user.all_users.GetAllUsersResponse
import com.thepetot.mindcraft.data.remote.response.user.update.UpdateUserResponse
import com.thepetot.mindcraft.data.remote.retrofit.ApiService
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.logMessage
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

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
                    emit(Result.Error(errorBody!!))
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
                    emit(Result.Error(errorBody!!))
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

    fun setTwoFactor(enable: Boolean, twoFactorBody: TwoFactorBody): LiveData<Result<TwoFactorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.setTwoFactor(enable, twoFactorBody)
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

    fun getTwoFactor(): LiveData<Result<TwoFactorResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getTwoFactor()
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
                    val parsedError = gson.fromJson(errorBody, ErrorResponse::class.java)
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

    fun createQuiz(userId: Int, createChallengeBody: CreateChallengeBody): LiveData<Result<CreateChallengeResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.createQuiz(userId, createChallengeBody)
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

    fun createParticipations(userId: Int, createParticipationBody: CreateParticipationBody): LiveData<Result<CreateParticipationsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.createParticipations(userId, createParticipationBody)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, ErrorResponse::class.java)
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(
                        "ChallengesRepository",
                        "Error parsing HTTP exception response: ${parseException.message}"
                    )
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage("ChallengesRepository", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllUsers(): LiveData<Result<GetAllUsersResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, ErrorResponse::class.java)
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(
                        "ChallengesRepository",
                        "Error parsing HTTP exception response: ${parseException.message}"
                    )
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage("ChallengesRepository", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun refreshToken(userId: Int) {
        try {
            val tokenRefreshBody = TokenRefreshBody(userId)
            val response = apiService.refreshToken(tokenRefreshBody)

            if (response.isSuccessful) {
                val header = response.headers()
                val accessToken = header["Authorization"]!!
                val refreshToken = header["X-Refresh-Token"]!!
                val sessionId = header["X-Session-Id"]!!
                userPreference.setPreferenceSettings(UserPreference.ACCESS_TOKEN_KEY, accessToken)
                userPreference.setPreferenceSettings(UserPreference.REFRESH_TOKEN_KEY, refreshToken)
                userPreference.setPreferenceSettings(UserPreference.SESSION_ID_KEY, sessionId)
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                } catch (parseException: Exception) {
                    logMessage(TAG, "Error parsing HTTP exception response: ${parseException.message}")
                }
            }
        } catch (e: Exception) {
            logMessage(TAG, "Error: ${e.message}")
        }
    }

    fun updateUser(
        userId: Int,
        image: File?,
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?
    ): LiveData<Result<UpdateUserResponse>> = liveData {
        emit(Result.Loading)

        val firstNameRequestBody = firstName?.toRequestBody("text/plain".toMediaType())
        val lastNameRequestBody = lastName?.toRequestBody("text/plain".toMediaType())
        val emailRequestBody = email?.toRequestBody("text/plain".toMediaType())
        val passwordRequestBody = password?.toRequestBody("text/plain".toMediaType())
        val requestBody = image?.asRequestBody("image/*".toMediaType())?.let {
            MultipartBody.Part.createFormData(
                "profileImage",
                image.name,
                it
            )
        }

        try {
            val response = apiService.updateUserData(
                userId,
                firstNameRequestBody,
                lastNameRequestBody,
                passwordRequestBody,
                emailRequestBody,
                requestBody
            )
            if (response.isSuccessful) {
                emit(Result.Success(response.body()!!))
            } else {
                try {
                    val errorBody = response.errorBody()?.string()
                    val gson = Gson()
                    val parsedError = gson.fromJson(errorBody, ErrorResponse::class.java)
                    val message = parsedError.errors.first().messages.first()
                    emit(Result.Error(message))
                } catch (parseException: Exception) {
                    logMessage(
                        "ChallengesRepository",
                        "Error parsing HTTP exception response: ${parseException.message}"
                    )
                    emit(Result.Error("Error parsing HTTP exception response"))
                }
            }
        } catch (e: Exception) {
            logMessage("ChallengesRepository", "Error: ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveUserData(user: UserModel) = userPreference.saveUserData(user)
    fun getUserData(): Flow<UserModel> = userPreference.getUserData()
    suspend fun deleteUserData() = userPreference.deleteUserData()

    suspend fun <T> setPreferenceSettings(pref: Preferences.Key<T>, value: T) = userPreference.setPreferenceSettings(pref, value)
    fun <T> getPreferenceSettings(pref: Preferences.Key<T>, defaultValue: T): Flow<T> = userPreference.getPreferenceSettings(pref, defaultValue)
    suspend fun <T> deletePreferenceSettings(pref: Preferences.Key<T>) = userPreference.deletePreferenceSettings(pref)

//    fun fetchAllChallenges(): LiveData<Result<List<ChallengeData>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.fetchAllChallenges()
//            if (response.isSuccessful) {
//                emit(Result.Success(response.body() ?: emptyList()))
//            } else {
//                emit(Result.Error("Failed to fetch challenges: ${response.message()}"))
//            }
//        } catch (e: Exception) {
//            emit(Result.Error("An error occurred: ${e.message}"))
//        }
//    }
//
//    fun getChallengeById(challengeId: Int): LiveData<Result<ChallengesResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getChallengeById(challengeId)
//            if (response.isSuccessful) {
//                emit(Result.Success(response.body()!!))
//            } else {
//                try {
//                    val errorBody = response.errorBody()?.string()
//                    val gson = Gson()
//                    val parsedError = gson.fromJson(errorBody, ChallengesResponse::class.java)
//                    val message = parsedError.message
//                    emit(Result.Error(message ?: "Unknown error"))
//                } catch (parseException: Exception) {
//                    logMessage(
//                        TAG,
//                        "Error parsing HTTP exception response: ${parseException.message}"
//                    )
//                    emit(Result.Error("Error parsing HTTP exception response"))
//                }
//            }
//        } catch (e: Exception) {
//            logMessage(TAG, "Error: ${e.message}")
//            emit(Result.Error(e.message.toString()))
//        }
//    }
//
//    fun fetchParticipants(challengeId: Int): LiveData<Result<List<ParticipantData>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.fetchParticipants(challengeId)
//            if (response.isSuccessful && response.body()?.success == true) {
//                emit(Result.Success(response.body()!!.data))
//            } else {
//                val errorMessage = response.body()?.message ?: "Failed to fetch participants"
//                emit(Result.Error(errorMessage))
//            }
//        } catch (e: Exception) {
//            emit(Result.Error("An error occurred: ${e.message}"))
//        }
//    }
//
//    fun fetchQuestions(challengeId: Int): LiveData<Result<List<QuestionData>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.fetchQuestions(challengeId)
//            if (response.isSuccessful && response.body()?.success == true) {
//                emit(Result.Success(response.body()!!.data))
//            } else {
//                val errorMessage = response.body()?.message ?: "Failed to fetch questions"
//                emit(Result.Error(errorMessage))
//            }
//        } catch (e: Exception) {
//            emit(Result.Error("An error occurred: ${e.message}"))
//        }
//    }
//
//    fun fetchAllParticipations(): LiveData<Result<List<ParticipationData>>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.fetchAllParticipations()
//            if (response.isSuccessful && response.body()?.success == true) {
//                emit(Result.Success(response.body()!!.data))
//            } else {
//                val errorMessage = response.body()?.message ?: "Failed to fetch participations"
//                emit(Result.Error(errorMessage))
//            }
//        } catch (e: Exception) {
//            emit(Result.Error("An error occurred: ${e.message}"))
//        }
//    }
//
//    fun fetchParticipationById(participationId: Int): LiveData<Result<ParticipationData>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.fetchParticipationById(participationId)
//            if (response.isSuccessful && response.body()?.success == true) {
//                emit(Result.Success(response.body()!!.data))
//            } else {
//                val errorMessage = response.body()?.message ?: "Failed to fetch participation by ID"
//                emit(Result.Error(errorMessage))
//            }
//        } catch (e: Exception) {
//            emit(Result.Error("An error occurred: ${e.message}"))
//        }
//    }

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