package com.example.pawpal.ui

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase

class PregledRezervacijaFragment : Fragment(), DatabaseConsumer
{
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
}