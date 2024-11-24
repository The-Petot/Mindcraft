package com.thepetot.mindcraft.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>){

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[FIRSTNAME_KEY] ?: "",
                preferences[LASTNAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[STATUS_KEY] ?: false,
                preferences[USER_ID_KEY]?.toInt() // Baca userId sebagai Int
            )
        }
    }


    suspend fun saveUser(dataUser: UserModel) {
        dataStore.edit {preferences ->
            preferences[FIRSTNAME_KEY] = dataUser.firstName
            preferences[LASTNAME_KEY] = dataUser.lastName
            preferences[EMAIL_KEY] = dataUser.email
            preferences[PASSWORD_KEY] = dataUser.password
            preferences[STATUS_KEY] = dataUser.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit {preferences ->
            preferences[STATUS_KEY] = true
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun logout() {
        dataStore.edit {preferences ->
            preferences[STATUS_KEY] = false
            preferences.remove(TOKEN_KEY)
        }
    }

    suspend fun saveUserData(userId: Int, token: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId.toString()
            preferences[TOKEN_KEY] = token
        }
    }




    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val FIRSTNAME_KEY = stringPreferencesKey("firstName")
        private val LASTNAME_KEY = stringPreferencesKey("lastName")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val STATUS_KEY = booleanPreferencesKey("status")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID_KEY = stringPreferencesKey("userId")


        fun getInstance(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>): UserPreference {
            return INSTANCE?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}