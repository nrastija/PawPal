package com.example.pawpal.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.PasUdomljavanjeAdapter
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class UpravljanjeUdomljavanjemPasaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PasUdomljavanjeAdapter
    private val pasList = mutableListOf<appdatabase.Pasudomljavanje>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f06_upravljanje_udomljavanjem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerPasUdomljavanje)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PasUdomljavanjeAdapter(pasList) { pas ->
            navigateToPasDetaljFragment(pas)
        }
        recyclerView.adapter = adapter

        val btnDodajPsa: Button = view.findViewById(R.id.btnDodajPsa)
        btnDodajPsa.setOnClickListener {
            navigateToDodajPsaFragment()
        }
        dajPeseke()
    }

    private fun dajPeseke() {
        lifecycleScope.launch {
            val peseki = database.pasUdomljavanjeQueries.dohvatiSvePse().executeAsList()
            updateDogList(peseki)
        }
    }

    private fun updateDogList(peseki: List<appdatabase.Pasudomljavanje>) {
        pasList.clear()
        pasList.addAll(peseki)
        adapter.notifyDataSetChanged()
    }

    private fun navigateToPasDetaljFragment(pas: appdatabase.Pasudomljavanje) {
        val detaljFragment = DodajPsaUdomljavanjeFragment.newInstance(pas.pasudomljavanjeID).apply {
            database = this@UpravljanjeUdomljavanjemPasaFragment.database
        }
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, detaljFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToDodajPsaFragment() {
        val dodajPsaFragment = DodajPsaUdomljavanjeFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, dodajPsaFragment)
            .addToBackStack(null)
            .commit()
    }
}