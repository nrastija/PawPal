package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.ProizvodKosaricaAdapter
import com.example.pawpal.entities.Proizvod
import com.example.pawpal.main.BaseActivity
import com.example.pawpal.services.KosaricaManager

class KosaricaActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProizvodKosaricaAdapter
    private lateinit var ukupnaCijenaLabel: TextView
    private val proizvodList = mutableListOf<Proizvod>() // Mutable list za dinamicka azuriranja

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_kosarica)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val btnNarudzba: Button = findViewById(R.id.btnNarudzba)
        btnNarudzba.setOnClickListener{
            val intent = Intent(this, CheckoutActivity::class.java);
            startActivity(intent);
        }

        ukupnaCijenaLabel = findViewById(R.id.ukupnaCijena)
        recyclerView = findViewById(R.id.recycler_view_kosarica) //Instanciranje recyclerviewa u kojem ce se prikazati podaci
        recyclerView.layoutManager = LinearLayoutManager(this)

        proizvodList.addAll(dohvatiProizvodeKosarice())

        adapter = ProizvodKosaricaAdapter(
            proizvodList,
            obrisiProizvod = { proizvod -> obrisiProizvod(proizvod) },
            povecajKolicinu = { proizvod -> povecajKolicinu(proizvod) },
            smanjiKolicinu = { proizvod -> smanjiKolicinu(proizvod) }
        )
        recyclerView.adapter = adapter

        azurirajUkupnuCijenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // kreiranje return gumba
        menuInflater.inflate(R.menu.f12_menu_return, menu)
        return true
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
        Toast.makeText(this, "${proizvod.naziv} obrisan iz kosarice", Toast.LENGTH_SHORT).show()
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
        KosaricaManager.smanjiKolicinuList(proizvod);
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
