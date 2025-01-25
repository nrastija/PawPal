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
import appdatabase.Promoponuda
import com.example.pawpal.R
import com.example.pawpal.adapters.PromoPonudaAdapater
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.main.MainActivity
import com.example.pawpal.main.PawPalApplication
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PromoPonudaFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PromoPonudaAdapater
    private val promoList = mutableListOf<Promoponuda>()
    private val koristeniPromo = mutableSetOf<Long>()

    companion object {
        private val koristeniPromoGlobal = mutableSetOf<Long>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (requireActivity().application as PawPalApplication).database
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f03_sve_ponude, container, false)
        val ponude = database.promoPonudaQueries.dohvatiSvePonude().executeAsList()
        if (ponude.isEmpty()) {
            (activity as MainActivity).resetPromoData()
        }
        recyclerView = view.findViewById(R.id.promoOffersRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val trenutniKorisnikID = KorisnikManager.dajUlogiranogKorisnika()!!
            val trenutniKorisnik = database.korisnikQueries.dajKorisnikaPoID(trenutniKorisnikID).executeAsOne()

            if (trenutniKorisnik.tip_korisnika == 2L) {
                view.findViewById<TextView>(R.id.NapomenaZaKod).visibility = View.GONE
                recyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
                    override fun onChildViewAttachedToWindow(view: View) {
                        view.findViewById<Button>(R.id.btnGenerirajKod)?.visibility = View.GONE
                    }
                    override fun onChildViewDetachedFromWindow(view: View) {}
                })

                view.findViewById<Button>(R.id.btnDodajPonudu)?.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        val fragment = DodajPonuduFragment()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }
        setupRecyclerView()
        dohvatiPromoPonude()
    }

    private fun setupRecyclerView() {
        adapter = PromoPonudaAdapater(promoList, koristeniPromo) { promo ->
            generatePromoCode(promo)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun dohvatiPromoPonude() {
        lifecycleScope.launch {
            val ponude = database.promoPonudaQueries.dohvatiSvePonude().executeAsList()
            val filteredPonude = ponude.filterNot {
                koristeniPromoGlobal.contains(it.naziv.hashCode().toLong())
            }
            updatePromoList(filteredPonude)
        }
    }

    private fun updatePromoList(ponude: List<appdatabase.Promoponuda>) {
        promoList.clear()
        promoList.addAll(ponude)
        adapter.notifyDataSetChanged()
    }

    private fun generatePromoCode(promo: appdatabase.Promoponuda) {
        val generatedCode = generateRandomCode()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Vaš promotivni kod")
            .setMessage(generatedCode)
            .setIcon(R.drawable.promo_icon)
            .setPositiveButton("U redu") { _, _ ->
                koristeniPromoGlobal.add(promo.naziv.hashCode().toLong())
                promoList.remove(promo)
                adapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun generateRandomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..8)
            .map { chars.random() }
            .joinToString("")
    }
}
