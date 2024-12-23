package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.ProizvodShopAdapter
import com.example.pawpal.entities.Kategorija
import com.example.pawpal.entities.Proizvod
import com.example.pawpal.services.KosaricaManager.filtrirajProizvodePoKategoriji
import com.google.android.material.navigation.NavigationView

class ShopFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProizvodShopAdapter
    private val proizvodList = mutableListOf<Proizvod>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f12_layout_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerShop)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        adapter = ProizvodShopAdapter(proizvodList) { proizvod ->
            // Navigacija na ProizvodDetaljFragment
            val detaljFragment = ProizvodDetaljFragment.newInstance(
                proizvod.proizvodID,
                proizvod.naziv,
                proizvod.cijena,
                proizvod.opis,
                proizvod.kategorijaID,
                proizvod.imageUrl
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detaljFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.adapter = adapter

        proizvodList.addAll(dohvatiProizvodeShop())
        adapter.notifyDataSetChanged()

        val btnZdravlje: Button = view.findViewById(R.id.filterZdravlje)
        val btnHrana: Button = view.findViewById(R.id.filterHrana)
        val btnHigijena: Button = view.findViewById(R.id.filterHigijena)
        val btnOstalo: Button = view.findViewById(R.id.filterOstalo)
        val btnReset: Button = view.findViewById(R.id.filterReset)

        btnZdravlje.setOnClickListener { filtrirajProizvode(Kategorija.ZDRAVLJE) }
        btnHrana.setOnClickListener { filtrirajProizvode(Kategorija.HRANA) }
        btnHigijena.setOnClickListener { filtrirajProizvode(Kategorija.HIGIJENA) }
        btnOstalo.setOnClickListener { filtrirajProizvode(Kategorija.OSTALO) }
        btnReset.setOnClickListener { filtrirajProizvode(Kategorija.RESET) }
    }

    private fun dohvatiProizvodeShop(): List<Proizvod> {
        return listOf(
            Proizvod(1, "Paramol 250ML", 14.99, "Lijek za pse protiv virusa", 1, 0, "proizvod_1"),
            Proizvod(2, "Reid Fills 400G", 11.98, "Hrana za pse u granulama", 2, 0, "proizvod_2"),
            Proizvod(3, "Pupino 3000x", 79.99, "Aparat za brijanje pasa", 1, 0, "proizvod_3")
        )
    }

    private fun filtrirajProizvode(kategorija: Kategorija) {
        val filtriraniProizvodi = if (kategorija == Kategorija.RESET) {
            dohvatiProizvodeShop()
        } else {
            filtrirajProizvodePoKategoriji(kategorija, dohvatiProizvodeShop())
        }

        proizvodList.clear()
        proizvodList.addAll(filtriraniProizvodi)

        recyclerView.animate()
            .alpha(0f)
            .setDuration(0)
            .withEndAction {
                adapter.notifyDataSetChanged()
                recyclerView.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .start()
            }
            .start()
    }
}