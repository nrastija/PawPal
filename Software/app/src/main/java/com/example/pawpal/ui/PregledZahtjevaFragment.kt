package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Zahtjevudomljavanje
import com.example.pawpal.R
import com.example.pawpal.adapters.ZahtjevAdapter
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PregledZahtjevaFragment: Fragment(), DatabaseConsumer{

    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private val zahtjeviList = mutableListOf<Zahtjevudomljavanje>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (requireActivity().application as PawPalApplication).database
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f06_odobravanje_odbijanje_zahtjeva, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerPasUdomljavanje)
        recyclerView.layoutManager = LinearLayoutManager(context)

    }


}