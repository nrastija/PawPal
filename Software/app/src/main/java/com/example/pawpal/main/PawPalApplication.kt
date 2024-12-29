package com.example.pawpal.main

import android.app.Application
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.pawpal.appdatabase.AppDatabase

class PawPalApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "appdatabase.db")
        database = AppDatabase(driver)
    }
}
