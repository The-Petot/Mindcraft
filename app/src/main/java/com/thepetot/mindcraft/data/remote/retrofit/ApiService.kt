package com.thepetot.mindcraft.data.remote.retrofit

import com.thepetot.mindcraft.data.remote.response.login.LoginBody
import com.thepetot.mindcraft.data.remote.response.login.LoginResponse
import com.thepetot.mindcraft.data.remote.response.signup.SignupBody
import com.thepetot.mindcraft.data.remote.response.signup.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/register")
    suspend fun signup(@Body signupBody: SignupBody): Response<SignupResponse>

    @POST("/login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>
}