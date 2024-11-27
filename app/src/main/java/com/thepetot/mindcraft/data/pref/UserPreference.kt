package com.thepetot.mindcraft.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.internal.userAgent

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveUserData(dataUser: UserModel) {
        dataStore.edit {preferences ->
            preferences[USERID_KEY] = dataUser.userId
            preferences[FIRST_NAME_KEY] = dataUser.firstName
            preferences[LAST_NAME_KEY] = dataUser.lastName
            preferences[EMAIL_KEY] = dataUser.email
            preferences[PASSWORD_KEY] = dataUser.password
            preferences[PROFILE_PICTURE_KEY] = dataUser.profilePicture
            preferences[IS_LOGIN_KEY] = dataUser.isLogin
            preferences[IS_2FA_KEY] = dataUser.is2FA
            preferences[ACCESS_TOKEN_KEY] = dataUser.accessToken
            preferences[REFRESH_TOKEN_KEY] = dataUser.refreshToken
            preferences[SESSION_ID_KEY] = dataUser.sessionId
        }
    }

    fun getUserData(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USERID_KEY] ?: 0,
                preferences[FIRST_NAME_KEY] ?: "",
                preferences[LAST_NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[PROFILE_PICTURE_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
                preferences[IS_2FA_KEY] ?: false,
                preferences[ACCESS_TOKEN_KEY] ?: "",
                preferences[REFRESH_TOKEN_KEY] ?: "",
                preferences[SESSION_ID_KEY] ?: ""
            )
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USERID_KEY = intPreferencesKey("userId")
        private val FIRST_NAME_KEY = stringPreferencesKey("firstName")
        private val LAST_NAME_KEY = stringPreferencesKey("lastName")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val PROFILE_PICTURE_KEY = stringPreferencesKey("profilePicture")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val IS_2FA_KEY = booleanPreferencesKey("is2FA")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refreshToken")
        private val SESSION_ID_KEY = stringPreferencesKey("sessionId")


        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}