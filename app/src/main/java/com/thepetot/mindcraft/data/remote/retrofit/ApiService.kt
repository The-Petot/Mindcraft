package com.thepetot.mindcraft.data.remote.retrofit

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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun signup(@Body signupBody: SignupBody): Response<SignupResponse>

    @POST("auth/login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>

    @POST("auth/oauth/google")
    suspend fun oAuthLogin(@Body oAuthLoginBody: OAuthLoginBody): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(@Body logoutBody: LogoutBody): Response<LogoutResponse>

    @PUT("auth/two-factor")
    suspend fun twoFactor(
        @Query("enable") enable: Boolean,
        @Body twoFactorBody: TwoFactorBody
    ): Response<TwoFactorResponse>

    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): Response<UserResponse>
}