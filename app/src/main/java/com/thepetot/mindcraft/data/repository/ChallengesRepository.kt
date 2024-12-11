package com.thepetot.mindcraft.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.thepetot.mindcraft.data.ChallengesRemoteMediator
import com.thepetot.mindcraft.data.local.database.ChallengesDatabase
import com.thepetot.mindcraft.data.local.database.SearchHistoryEntity
import com.thepetot.mindcraft.data.remote.response.challenges.questions.QuestionsResponse
import com.thepetot.mindcraft.data.remote.response.challenges.test.DataItem
import com.thepetot.mindcraft.data.remote.response.error.ErrorResponse
import com.thepetot.mindcraft.data.remote.retrofit.ApiService
import com.thepetot.mindcraft.utils.Result
import com.thepetot.mindcraft.utils.logMessage

class ChallengesRepository private constructor(
    private val apiService: ApiService,
    private val challengesDatabase: ChallengesDatabase
) {

    fun getChallenges(query: String): LiveData<PagingData<DataItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = ChallengesRemoteMediator(challengesDatabase, apiService, query),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
//                if (query != null) challengesDatabase.challengesDao().getChallengesByQuery(query)
//                else challengesDatabase.challengesDao().getAllChallenges()
//                challengesDatabase.challengesDao().getChallengesByQuery(query)
                challengesDatabase.challengesDao().getAllChallenges()
            }
        ).liveData
    }

    fun getQuestionsByChallengeId(challengeId: Int): LiveData<Result<QuestionsResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getQuestionsByChallengeId(challengeId)
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

    suspend fun insertSearchQuery(query: String) {
        val searchHistory = SearchHistoryEntity(query = query)
        challengesDatabase.searchHistoryDao().insert(searchHistory)
    }

    fun getSearchQueries(): LiveData<List<SearchHistoryEntity>> {
        return challengesDatabase.searchHistoryDao().getAll()
    }

    companion object {
        @Volatile
        private var instance: ChallengesRepository? = null
        fun getInstance(
            apiService: ApiService,
            challengesDatabase: ChallengesDatabase
        ): ChallengesRepository =
            instance ?: synchronized(this) {
                instance ?: ChallengesRepository(apiService, challengesDatabase)
            }.also { instance = it }
    }
}