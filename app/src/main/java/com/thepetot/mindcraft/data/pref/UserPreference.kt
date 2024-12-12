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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveUserData(dataUser: UserModel) {
        dataStore.edit {preferences ->
            preferences[USERID_KEY] = dataUser.userId
            preferences[FIRST_NAME_KEY] = dataUser.firstName
            preferences[LAST_NAME_KEY] = dataUser.lastName
            preferences[EMAIL_KEY] = dataUser.email
            preferences[TOTAL_SCORE_KEY] = dataUser.totalScore
            preferences[CURRENT_RANK_KEY] = dataUser.currentRank
            preferences[PROFILE_PICTURE_KEY] = dataUser.profilePicture
            preferences[TWO_FACTOR_ENABLE_KEY] = dataUser.twoFactorEnable
            preferences[TWO_FACTOR_SECRET_KEY] = dataUser.twoFactorSecret ?: ""
            preferences[NOTIFICATION_ENABLED_KEY] = dataUser.notificationEnabled
            preferences[CREATED_AT_KEY] = dataUser.createdAt
            preferences[UPDATED_AT_KEY] = dataUser.updatedAt
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
                preferences[TOTAL_SCORE_KEY] ?: 0,
                preferences[CURRENT_RANK_KEY] ?: 0,
                preferences[PROFILE_PICTURE_KEY] ?: "",
                preferences[TWO_FACTOR_ENABLE_KEY] ?: false,
                preferences[TWO_FACTOR_SECRET_KEY] ?: "",
                preferences[NOTIFICATION_ENABLED_KEY] ?: false,
                preferences[CREATED_AT_KEY] ?: "",
                preferences[UPDATED_AT_KEY] ?: "",
                preferences[ACCESS_TOKEN_KEY] ?: "",
                preferences[REFRESH_TOKEN_KEY] ?: "",
                preferences[SESSION_ID_KEY] ?: ""
            )
        }
    }

    suspend fun deleteUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USERID_KEY)
            preferences.remove(FIRST_NAME_KEY)
            preferences.remove(LAST_NAME_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(TOTAL_SCORE_KEY)
            preferences.remove(CURRENT_RANK_KEY)
            preferences.remove(PROFILE_PICTURE_KEY)
            preferences.remove(TWO_FACTOR_ENABLE_KEY)
            preferences.remove(TWO_FACTOR_SECRET_KEY)
            preferences.remove(NOTIFICATION_ENABLED_KEY)
            preferences.remove(CREATED_AT_KEY)
            preferences.remove(UPDATED_AT_KEY)
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(SESSION_ID_KEY)
        }
    }

    suspend fun <T> setPreferenceSettings(pref: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[pref] = value
        }
    }

    fun <T> getPreferenceSettings(pref: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.map { preferences ->
            preferences[pref] ?: defaultValue
        }
    }

    suspend fun <T> deletePreferenceSettings(pref: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(pref)
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        val USERID_KEY = intPreferencesKey("userId")
        val FIRST_NAME_KEY = stringPreferencesKey("firstName")
        val LAST_NAME_KEY = stringPreferencesKey("lastName")
        val EMAIL_KEY = stringPreferencesKey("email")
        val TOTAL_SCORE_KEY = intPreferencesKey("totalScore")
        val CURRENT_RANK_KEY = intPreferencesKey("currentRank")
        val PROFILE_PICTURE_KEY = stringPreferencesKey("profilePicture")
        val TWO_FACTOR_ENABLE_KEY = booleanPreferencesKey("twoFactorEnable")
        val TWO_FACTOR_SECRET_KEY = stringPreferencesKey("twoFactorSecret")
        val NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("notificationEnabled")
        val CREATED_AT_KEY = stringPreferencesKey("createdAt")
        val UPDATED_AT_KEY = stringPreferencesKey("updatedAt")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refreshToken")
        val SESSION_ID_KEY = stringPreferencesKey("sessionId")

        val NOTIFICATION_ONE = booleanPreferencesKey("notificationOne")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}