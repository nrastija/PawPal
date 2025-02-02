package com.example.pawpal.helper

import android.content.Context
import android.content.SharedPreferences

class AppPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    private val firstLaunchKey = "first_launch"

    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(firstLaunchKey, true)
    }

    fun setFirstLaunchDone() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(firstLaunchKey, false)
        editor.apply()
    }
}