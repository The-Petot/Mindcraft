package com.thepetot.mindcraft.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val PREFS_NAME = "app_preferences"

    private fun getInstance(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveBoolean(context: Context, key: String, value: Boolean) {
        getInstance(context).edit().putBoolean(key, value).apply()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        return getInstance(context).getBoolean(key, defaultValue)
    }

    fun saveString(context: Context, key: String, value: String) {
        getInstance(context).edit().putString(key, value).apply()
    }

    fun getString(context: Context, key: String, defaultValue: String? = null): String? {
        return getInstance(context).getString(key, defaultValue)
    }
}