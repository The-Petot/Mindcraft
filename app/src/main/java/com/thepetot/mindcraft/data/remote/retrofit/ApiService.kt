package com.thepetot.mindcraft.data.remote.retrofit

import com.thepetot.mindcraft.data.remote.response.challenges.ChallengeData
import com.thepetot.mindcraft.data.remote.response.challenges.ChallengesResponse
import com.thepetot.mindcraft.data.remote.response.challenges.ParticipantsResponse
import com.thepetot.mindcraft.data.remote.response.challenges.QuestionsResponse
import com.thepetot.mindcraft.data.remote.response.login.LoginBody
import com.thepetot.mindcraft.data.remote.response.login.LoginResponse
import com.thepetot.mindcraft.data.remote.response.login.OAuthLoginBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutBody
import com.thepetot.mindcraft.data.remote.response.logout.LogoutResponse
import com.thepetot.mindcraft.data.remote.response.participations.AllParticipationsResponse
import com.thepetot.mindcraft.data.remote.response.participations.ParticipationResponse
import com.thepetot.mindcraft.data.remote.response.signup.SignupBody
import com.thepetot.mindcraft.data.remote.response.signup.SignupResponse
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorBody
import com.thepetot.mindcraft.data.remote.response.twofactor.TwoFactorResponse
import com.thepetot.mindcraft.data.remote.response.user.Data
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
    suspend fun setTwoFactor(
        @Query("enable") enable: Boolean,
        @Body twoFactorBody: TwoFactorBody
    ): Response<TwoFactorResponse>

    @GET("auth/two-factor")
    suspend fun getTwoFactor(): Response<TwoFactorResponse>

    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): Response<UserResponse>

    @GET("/api/v1/challenges/")
    suspend fun fetchAllChallenges(): Response<List<ChallengeData>>

    @GET("/challenges/{challengeId}")
    suspend fun getChallengeById(
        @Path("challengeId") challengeId: Int
    ): Response<ChallengesResponse>

    @GET("/api/v1/challenges/{challengeId}/participants")
    suspend fun fetchParticipants(
        @Path("challengeId") challengeId: Int
    ): Response<ParticipantsResponse>

    @GET("/api/v1/challenges/{challengeId}/questions")
    suspend fun fetchQuestions(
        @Path("challengeId") challengeId: Int
    ): Response<QuestionsResponse>

    @GET("/api/v1/participations/")
    suspend fun fetchAllParticipations(): Response<AllParticipationsResponse>

    @GET("/api/v1/participations/{participationId}")
    suspend fun fetchParticipationById(
        @Path("participationId") participationId: Int
    ): Response<ParticipationResponse>

}