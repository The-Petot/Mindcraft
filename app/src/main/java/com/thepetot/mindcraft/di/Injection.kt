package com.thepetot.mindcraft.di

import android.content.Context
import com.thepetot.mindcraft.data.local.database.ChallengesDatabase
import com.thepetot.mindcraft.data.pref.UserPreference
import com.thepetot.mindcraft.data.pref.dataStore
import com.thepetot.mindcraft.data.remote.retrofit.ApiConfig
import com.thepetot.mindcraft.data.repository.ChallengesRepository
import com.thepetot.mindcraft.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideChallengesRepository(context: Context): ChallengesRepository {
        val database = ChallengesDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return ChallengesRepository.getInstance(apiService, database)
    }
}