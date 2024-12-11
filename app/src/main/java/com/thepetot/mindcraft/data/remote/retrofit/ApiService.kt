package com.thepetot.mindcraft.data.remote.retrofit

//import com.thepetot.mindcraft.data.remote.response.challenges.ChallengeData
//import com.thepetot.mindcraft.data.remote.response.challenges.ChallengesResponse
import com.thepetot.mindcraft.data.remote.body.auth.TokenRefreshBody
import com.thepetot.mindcraft.data.remote.body.users.CreateParticipationBody
import com.thepetot.mindcraft.data.remote.response.auth.refresh_token.RefreshTokenResponse
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeBody
import com.thepetot.mindcraft.data.remote.response.challenges.CreateChallengeResponse
import com.thepetot.mindcraft.data.remote.response.challenges.questions.QuestionsResponse
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.data.remote.response.challenges.test.GetChallengesResponse
//import com.thepetot.mindcraft.data.remote.response.challenges.ParticipantsResponse
//import com.thepetot.mindcraft.data.remote.response.challenges.QuestionsResponse
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
import com.thepetot.mindcraft.data.remote.response.user.Data
import com.thepetot.mindcraft.data.remote.response.user.UserResponse
import com.thepetot.mindcraft.data.remote.response.user.all_users.GetAllUsersResponse
import com.thepetot.mindcraft.data.remote.response.user.update.UpdateUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @Multipart
    @PUT("users/{userId}")
    suspend fun updateUserData(
        @Path("userId") userId: Int,
        @Part("firstName") firstName: RequestBody?,
        @Part("lastName") lastName: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part profileImage: MultipartBody.Part?
    ): Response<UpdateUserResponse>

    @POST("users/{userId}/challenges")
    suspend fun createQuiz(
        @Path("userId") userId: Int,
        @Body createQuizBody: CreateChallengeBody
    ): Response<CreateChallengeResponse>

    @GET("challenges")
    suspend fun getChallengesWithPage(
        @Query("offset") page: Int,
        @Query("limit") size: Int,
        @Query("search") query: String
    ): GetChallengesResponse

    @GET("challenges/{challengeId}/questions")
    suspend fun getQuestionsByChallengeId(
        @Path("challengeId") challengeId: Int
    ): Response<QuestionsResponse>

    @POST("users/{userId}/participations")
    suspend fun createParticipations(
        @Path("userId") userId: Int,
        @Body createParticipationBody: CreateParticipationBody
    ): Response<CreateParticipationsResponse>

    @GET("users")
    suspend fun getAllUsers(): Response<GetAllUsersResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body tokenRefreshBody: TokenRefreshBody
    ): Response<RefreshTokenResponse>

//
//    @GET("/challenges/{challengeId}")
//    suspend fun getChallengeById(
//        @Path("challengeId") challengeId: Int
//    ): Response<ChallengesResponse>
//
//    @GET("/api/v1/challenges/{challengeId}/participants")
//    suspend fun fetchParticipants(
//        @Path("challengeId") challengeId: Int
//    ): Response<ParticipantsResponse>
//
//    @GET("/api/v1/challenges/{challengeId}/questions")
//    suspend fun fetchQuestions(
//        @Path("challengeId") challengeId: Int
//    ): Response<QuestionsResponse>
//
//    @GET("/api/v1/participations/")
//    suspend fun fetchAllParticipations(): Response<AllParticipationsResponse>
//
//    @GET("/api/v1/participations/{participationId}")
//    suspend fun fetchParticipationById(
//        @Path("participationId") participationId: Int
//    ): Response<ParticipationResponse>

}