package com.example.pawpal.f12_shop

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.f12_shop.KosaricaManager.filtrirajProizvodePoKategoriji
import com.example.pawpal.f12_shop.entiteti.Proizvod
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class ShopActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProizvodShopAdapter
    private val proizvodList = mutableListOf<Proizvod>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_layout_shop)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        recyclerView = findViewById(R.id.recyclerShop) //Instanciranje recyclerviewa u kojem ce se prikazati podaci
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        adapter = ProizvodShopAdapter(proizvodList) { proizvod ->
            val intent = Intent(this, ProizvodDetaljActivity::class.java)
            intent.putExtra("nazivProizvoda", proizvod.naziv)
            intent.putExtra("cijenaProizvoda", proizvod.cijena)
            intent.putExtra("opisProizvoda", proizvod.opis)
            intent.putExtra("kategorijaProizvoda", proizvod.kategorijaID)
            intent.putExtra("imageUrl", proizvod.imageUrl)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        proizvodList.addAll(dohvatiProizvodeShop())
        adapter.notifyDataSetChanged()

        val btnZdravlje: Button = findViewById(R.id.filterZdravlje)
        val btnHrana: Button = findViewById(R.id.filterHrana)
        val btnHigijena: Button = findViewById(R.id.filterHigijena)
        val btnOstalo: Button = findViewById(R.id.filterOstalo)
        val btnReset: Button = findViewById(R.id.filterReset)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // kreiranje kosarice
        menuInflater.inflate(R.menu.f12_menu_kosarica, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // funkcija obrade klika na kosaricu
        return when (item.itemId) {
            R.id.shop_basket -> {
                val intent = Intent(this, KosaricaActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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