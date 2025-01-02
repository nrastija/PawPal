package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appdatabase.DohvatiProizvodeZaKosaricu
import com.example.pawpal.R
import com.example.pawpal.adapters.ProizvodKosaricaAdapter
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class KosaricaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProizvodKosaricaAdapter
    private lateinit var ukupnaCijenaLabel: TextView
    private val proizvodList = mutableListOf<DohvatiProizvodeZaKosaricu>()

    companion object {
        private const val ARG_KOSARICA_ID = "kosarica_id"

        fun newInstance(kosaricaID: Long): KosaricaFragment {
            val fragment = KosaricaFragment()
            val args = Bundle()
            args.putLong(ARG_KOSARICA_ID, kosaricaID)
            fragment.arguments = args
            return fragment
        }
    }

    private var kosaricaID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            kosaricaID = it.getLong(ARG_KOSARICA_ID, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f12_kosarica, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ukupnaCijenaLabel = view.findViewById(R.id.ukupnaCijena)
        recyclerView = view.findViewById(R.id.recycler_view_kosarica)

        val btnNarudzba: Button = view.findViewById(R.id.btnNarudzba)
        btnNarudzba.setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProizvodKosaricaAdapter(
            proizvodList,
            obrisiProizvod = { proizvod -> obrisiProizvod(proizvod) },
            povecajKolicinu = { proizvod -> povecajKolicinu(proizvod) },
            smanjiKolicinu = { proizvod -> smanjiKolicinu(proizvod) }
        )
        recyclerView.adapter = adapter

        dohvatiProizvodeKosarice()
    }


    private fun dohvatiProizvodeKosarice(){
        lifecycleScope.launch {
            val proizvodi = database.kosaricaProizvodQueries.dohvatiProizvodeZaKosaricu(kosaricaID).executeAsList()
            updateProizvodList(proizvodi)
        }
    }

    private fun obrisiProizvod(proizvod: DohvatiProizvodeZaKosaricu) {
        lifecycleScope.launch {
            val brisanProizvod = database.proizvodQueries.dohvatiProizvodPoId(proizvod.proizvodID).executeAsOneOrNull()
            database.kosaricaProizvodQueries.brisanjeProizvodaKosarice(kosaricaID, proizvod.proizvodID)
            dohvatiProizvodeKosarice()
            Toast.makeText(requireContext(), "${brisanProizvod?.naziv} obrisan iz košarice", Toast.LENGTH_SHORT).show()
        }
    }

    private fun povecajKolicinu(proizvod: DohvatiProizvodeZaKosaricu) {
        lifecycleScope.launch {
            database.kosaricaProizvodQueries.azurirajKolicinu(
                kolicina = 1,
                kosaricaID = kosaricaID,
                proizvodID = proizvod.proizvodID
            )
            dohvatiProizvodeKosarice()
        }
    }

    private fun smanjiKolicinu(proizvod: DohvatiProizvodeZaKosaricu) {
        lifecycleScope.launch {
            if (proizvod.kolicina > 1) {
                database.kosaricaProizvodQueries.azurirajKolicinu(
                    kolicina = -1,
                    kosaricaID = kosaricaID,
                    proizvodID = proizvod.proizvodID
                )
            } else {
                obrisiProizvod(proizvod)
            }
            dohvatiProizvodeKosarice()
        }
    }

    private fun azurirajUkupnuCijenu() {
        val ukupnaCijena = database.kosaricaProizvodQueries.dohvatiUkupnuCijenuZaKosaricu(kosaricaID).executeAsOneOrNull()
        //val zaokruzenaCijena = BigDecimal(ukupnaCijena).setScale(2, RoundingMode.HALF_UP).toDouble()
        ukupnaCijenaLabel.text = "Ukupna cijena: $ukupnaCijena €"
    }

    private fun updateProizvodList(proizvodi: List<DohvatiProizvodeZaKosaricu>) {
        proizvodList.clear()
        proizvodList.addAll(proizvodi)
        adapter.notifyDataSetChanged()
        azurirajUkupnuCijenu()
    }

}