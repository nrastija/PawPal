package com.example.pawpal.ui


import android.os.Bundle
import android.util.Log
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
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class UpravljanjeUdomljavanjemPasaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PasUdomljavanjeAdapter
    private val pasList = mutableListOf<appdatabase.Pasudomljavanje>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (requireActivity().application as PawPalApplication).database
    }

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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerPasUdomljavanje)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val btnDodajPsa: Button? = view.findViewById(R.id.btnDodajPsaUdomljavanje)
        if (btnDodajPsa == null) {
            Log.e("DodajPsa", "Button is null!")
        } else {
            btnDodajPsa.setOnClickListener {
                Log.d("DodajPsa", "Button clicked")
                navigateToDodajPsaFragment()
                updateRecyclerView()
            }
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

    private fun updateRecyclerView() {
        lifecycleScope.launch {
            val listaPasa = database.pasUdomljavanjeQueries.dohvatiSvePse().executeAsList()
            adapter.updateList(listaPasa)
        }
    }


    private fun navigateToDodajPsaFragment() {
        val dodajPsaFragment = DodajPsaUdomljavanjeFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, dodajPsaFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToPasDetaljFragment(pas: appdatabase.Pasudomljavanje) {
        val detaljiPsaFragment = UpravljanjeDetaljimaPsaFragment()
        val args = Bundle().apply {
            putLong("pasId", pas.pasudomljavanjeID)
            putString("ime", pas.ime)
            putString("starost", pas.dob.toString())
            putString("kilaza", pas.kilaza.toString())
            putString("spol", pas.spol)
            putString("pasmina", pas.pasmina)
            putString("datumRodenja", pas.datumRodenja)
            putString("opis", pas.opis)
            putString("cjepiva", pas.cijepiva)
            putString("dodatneInfo", pas.dodatneinfo)
        }
        detaljiPsaFragment.arguments = args

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, detaljiPsaFragment)
            .addToBackStack(null)
            .commit()
    }
}