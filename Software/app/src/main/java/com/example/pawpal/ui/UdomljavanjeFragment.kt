package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.PasUdomljavanjeAdapter
import com.example.pawpal.data.impl.PasUdomljavanjeDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class UdomljavanjeFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PasUdomljavanjeAdapter
    private val pasList = mutableListOf<appdatabase.Pasudomljavanje>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.novo_f09_udomljavanjelayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerPasUdomljavanje)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PasUdomljavanjeAdapter(pasList) { pas ->
            navigateToPasDetaljFragment(pas)
        }
        recyclerView.adapter = adapter
        fetchDogs() // Dohvat podataka o psima iz baze
    }

    private fun fetchDogs() {
        lifecycleScope.launch {
            // Zamijeniti s tvojim izvorom podataka
            val dogs = database.pasUdomljavanjeQueries.dohvatiSvePse().executeAsList() // Pretpostavljam da koristiš SQLite
            updateDogList(dogs)
        }
    }

    private fun updateDogList(dogs: List<appdatabase.Pasudomljavanje>) {
        pasList.clear()
        pasList.addAll(dogs)
        adapter.notifyDataSetChanged()
    }

    private fun navigateToPasDetaljFragment(pas: appdatabase.Pasudomljavanje) {
        val detaljFragment = PasDetaljFragment.newInstance(pas.pasudomljavanjeID)
        detaljFragment.database = database
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, detaljFragment)
            .addToBackStack(null)
            .commit()
    }
}
