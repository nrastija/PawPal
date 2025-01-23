package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.SPAAdapter
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class SPAFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SPAAdapter
    private val uslugaList = mutableListOf<appdatabase.Usluga>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f03_termini, container, false)
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerSPA)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SPAAdapter(uslugaList) { usluga ->
            navigateToUslugaDetaljiFragment(usluga)
        }
        recyclerView.adapter = adapter
        dajUsluge()
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerSPA)

        // Ispravan LinearLayoutManager s kontekstom fragmenta
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        // Postavljanje paddinga za centriranje kartica
        recyclerView.setPadding(0, 0, 20, 0)
        recyclerView.clipToPadding = false

        // Postavljanje adaptera
        adapter = SPAAdapter(uslugaList) { usluga ->
            navigateToUslugaDetaljiFragment(usluga)
        }
        recyclerView.adapter = adapter

        // Dohvaćanje usluga
        dajUsluge()
    }


    private fun dajUsluge() {
        lifecycleScope.launch {
            val usluge = database.uslugaQueries.dohvatiSveUsluge().executeAsList()
            updateUslugaList(usluge)
        }
    }

    private fun updateUslugaList(usluge: List<appdatabase.Usluga>) {
        uslugaList.clear()
        uslugaList.addAll(usluge)
        adapter.notifyDataSetChanged()
    }

    private fun navigateToUslugaDetaljiFragment(usluga: appdatabase.Usluga) {
        val detaljFragment =  SPADetaljiFragment.newInstance(usluga.uslugaID).apply {
            database = this@SPAFragment.database
        }
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, detaljFragment)
            .addToBackStack(null)
            .commit()
    }

}
