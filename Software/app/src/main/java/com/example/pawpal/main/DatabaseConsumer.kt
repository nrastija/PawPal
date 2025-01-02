package com.example.pawpal.main

import com.pawpal.appdatabase.AppDatabase

interface DatabaseConsumer {
    var database: AppDatabase
}