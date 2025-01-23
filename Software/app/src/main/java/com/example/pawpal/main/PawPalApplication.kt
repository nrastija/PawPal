package com.example.pawpal.main

import android.app.Application
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.pawpal.appdatabase.AppDatabase
/*
class PawPalApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "appdatabase.db")
        database = AppDatabase(driver)

        val dbPath = this.getDatabasePath("appdatabase.db").absolutePath
        println("Path to database:$dbPath")
    }
}
*/

class PawPalApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "appdatabase.db")
        database = AppDatabase(driver)

        resetUserData()

        val dbPath = this.getDatabasePath("appdatabase.db").absolutePath
        println("Path to database:$dbPath")
    }

    private fun resetUserData() {
        database.korisnikQueries.transaction {
            database.korisnikQueries.dodajKorisnik(
                "admin",
                "Administrator",
                "Administrator",
                "admin",
                "Administrator@gmail.com",
                2
            )
        }
    }
}
