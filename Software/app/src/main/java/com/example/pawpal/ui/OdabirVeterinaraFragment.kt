package com.example.pawpal.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.adapters.VeterinarAdapter
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class OdabirVeterinaraFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var recyclerView: RecyclerView
    private lateinit var vetAdapter: VeterinarAdapter
    private val veterinariList = mutableListOf<appdatabase.Veterinari>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_odabir_veterinara, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                val driver = AndroidSqliteDriver(AppDatabase.Schema, requireContext(), "appdatabase.db")
                database = AppDatabase(driver)
                Log.d("OdabirVeterinaraFragment", "Baza podataka inicijalizirana.")



        recyclerView = view.findViewById(R.id.recyclerViewVet)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        vetAdapter = VeterinarAdapter(veterinariList) { veterinar ->
            navigateToRezervacijaVeterinara(veterinar)
        }

        recyclerView.adapter = vetAdapter


        dajVeterinare()
    }

    private fun dajVeterinare() {
        lifecycleScope.launch {
            val veterinari = database.veterinarQueries.dohvatiSveVeterinare().executeAsList()
            updateVeterinariList(veterinari)
        }
    }

    private fun updateVeterinariList(veterinari: List<appdatabase.Veterinari>) {
        veterinariList.clear()
        veterinariList.addAll(veterinari)
        vetAdapter.notifyDataSetChanged()
    }

    private fun navigateToRezervacijaVeterinara(veterinar: appdatabase.Veterinari) {
        Log.d("usao u navigiraj", "Veterinar ID: ${veterinar.veterinarID}")
        val rezervacijaFragment = RezervacijaVeterinaraFragment.newInstance(veterinar.veterinarID).apply {
            database = this@OdabirVeterinaraFragment.database
        }

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, rezervacijaFragment)
            .addToBackStack(null)
            .commit()
    }
}
