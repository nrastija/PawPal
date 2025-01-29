package com.example.pawpal.ui

import androidx.fragment.app.Fragment
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase

class UpravljanjeDetaljimaPsaFragment: Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase


}