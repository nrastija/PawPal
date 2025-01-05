package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Veterinari
import com.example.pawpal.R
import com.example.pawpal.adapters.VeterinarAdapter
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class OdabirVeterinaraFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var vetAdapter: VeterinarAdapter
    private val veterinariList = mutableListOf<Veterinari>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_odabir_veterinara, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicijalizacija RecyclerView i adaptera
        recyclerView = view.findViewById(R.id.recyclerViewVet)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        vetAdapter = VeterinarAdapter(veterinariList) { veterinar ->
            navigateToRezervacijaVeterinara(veterinar)
        }

        recyclerView.adapter = vetAdapter

        // Dohvat veterinara iz baze podataka
        dajVeterinare()
    }

    // Funkcija za dohvat svih veterinara iz baze podataka
    private fun dajVeterinare() {
        lifecycleScope.launch {
            val veterinari = database.veterinarQueries.dohvatiSveVeterinare().executeAsList()
            updateVeterinariList(veterinari)
        }
    }

    // Ažuriranje liste veterinara u adapteru
    private fun updateVeterinariList(newVeterinariList: List<Veterinari>) {
        veterinariList.clear()
        veterinariList.addAll(newVeterinariList)
        vetAdapter.updateVeterinariList(veterinariList)
    }

    // Navigacija do fragmenta za rezervaciju veterinara
    private fun navigateToRezervacijaVeterinara(veterinar: Veterinari) {
        Log.d("usao u navigiraj", "")
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
