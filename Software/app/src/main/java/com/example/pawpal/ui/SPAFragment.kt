package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.SPAAdapter
import com.example.pawpal.data.session.KorisnikManager
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import com.example.pawpal.main.MainActivity
import com.example.pawpal.main.PawPalApplication


class SPAFragment : Fragment() {
    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SPAAdapter
    private val uslugaList = mutableListOf<appdatabase.Usluga>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f03_sve_usluge, container, false)
        database = (requireActivity().application as PawPalApplication).database
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //novo
        val usluge = database.uslugaQueries.dohvatiSveUsluge().executeAsList()
        if (usluge.isEmpty()) {
            (activity as MainActivity).resetSPAData()
        }

        recyclerView = view.findViewById(R.id.recyclerSPA)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = SPAAdapter(uslugaList) { usluga ->
            navigateToUslugaDetaljiFragment(usluga)
        }
        recyclerView.adapter = adapter

        dajUsluge()

        lifecycleScope.launch {
            val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
            korisnikID?.let { id ->
                val korisnik = database.korisnikQueries.dajKorisnikaPoID(id).executeAsOneOrNull()
                if (korisnik?.tip_korisnika == 2L) {
                    view.findViewById<Button>(R.id.btnDodajUslugu)?.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            navigateToDodajUsluguFragment()
                        }
                    }
                    view.findViewById<TextView>(R.id.spacentar)?.visibility = View.GONE
                    view.findViewById<TextView>(R.id.zakazitetermin)?.visibility = View.GONE
                }
            }
        }
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
        val detaljFragment = SPADetaljiFragment.newInstance(usluga.uslugaID)
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, detaljFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToDodajUsluguFragment() {
        val dodajUsluguFragment = DodajUsluguFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, dodajUsluguFragment)
            .addToBackStack(null)
            .commit()
    }
}
