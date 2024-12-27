package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.ProizvodKosaricaAdapter
import com.example.pawpal.f12_shop.entiteti.Proizvod
import com.example.pawpal.main.MainActivity
import com.example.pawpal.services.KosaricaManager

class KosaricaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProizvodKosaricaAdapter
    private lateinit var ukupnaCijenaLabel: TextView
    private val proizvodList = mutableListOf<Proizvod>() // Mutable list za dinamicka azuriranja

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
        /*proizvodList.addAll(dohvatiProizvodeKosarice())

        adapter = ProizvodKosaricaAdapter(
            proizvodList,
            obrisiProizvod = { proizvod -> obrisiProizvod(proizvod) },
            povecajKolicinu = { proizvod -> povecajKolicinu(proizvod) },
            smanjiKolicinu = { proizvod -> smanjiKolicinu(proizvod) }
        )
        recyclerView.adapter = adapter

        azurirajUkupnuCijenu()*/
    }

    private fun dohvatiProizvodeKosarice(): List<Proizvod> {
        return KosaricaManager.dohvatiProizvodeLista()
    }

    private fun obrisiProizvod(proizvod: Proizvod) {
        KosaricaManager.obrisiProizvodLista(proizvod)
        val pozicija = proizvodList.indexOf(proizvod)

        if (pozicija >= 0) {
            proizvodList.removeAt(pozicija)
            adapter.notifyItemRemoved(pozicija)
        }

        azurirajUkupnuCijenu()
        Toast.makeText(requireContext(), "${proizvod.naziv} obrisan iz košarice", Toast.LENGTH_SHORT).show()
    }

    private fun povecajKolicinu(proizvod: Proizvod) {
        KosaricaManager.povecajKolicinuList(proizvod)
        val pozicija = proizvodList.indexOf(proizvod)

        if (pozicija >= 0) {
            adapter.notifyItemChanged(pozicija)
        }
        azurirajUkupnuCijenu()
    }

    private fun smanjiKolicinu(proizvod: Proizvod) {
        KosaricaManager.smanjiKolicinuList(proizvod)
        val pozicija = proizvodList.indexOf(proizvod)

        if (pozicija >= 0) {
            if (proizvod.kolicina > 0) {
                adapter.notifyItemChanged(pozicija)
            } else {
                proizvodList.removeAt(pozicija)
                adapter.notifyItemRemoved(pozicija)
            }
        }
        azurirajUkupnuCijenu()
    }

    private fun azurirajUkupnuCijenu() {
        val cijena = KosaricaManager.izracunajCijenuLista()
        ukupnaCijenaLabel.text = "Ukupna cijena: $cijena €"
    }
}