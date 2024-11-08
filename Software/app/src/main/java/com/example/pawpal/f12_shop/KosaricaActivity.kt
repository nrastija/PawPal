package com.example.pawpal.f12_shop

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.f12_shop.entiteti.Proizvod
import com.example.pawpal.main.BaseActivity

class KosaricaActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProizvodKosaricaAdapter
    private val proizvodList = mutableListOf<Proizvod>() // Mutable list za dinamicka azuriranja

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_kosarica)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // kreiranje return gumba
        menuInflater.inflate(R.menu.f12_menu_return, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // funkcija obrade klika na return gumb
        return when (item.itemId) {
            R.id.return_icon -> {
                val intent = Intent(this, ShopActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun dohvatiProizvodeKosarice(): List<Proizvod> {
        // Define products directly
        return listOf(
            Proizvod(1, "Darling", 6.31, "Test", 1, null),
            Proizvod(2, "Sok", 51.31, "Test2", 2, null),
            Proizvod(3, "Hrana", 0.31, "Test3", 1, null)
        )
    }


    private fun obrisiProizvod(proizvod: Proizvod) {
        proizvodList.remove(proizvod)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "${proizvod.naziv} removed", Toast.LENGTH_SHORT).show()
    }

    private fun povecajKolicinu(proizvod: Proizvod) {
        Toast.makeText(this, "Povecao kolicinu za ${proizvod.naziv}", Toast.LENGTH_SHORT).show()
    }

    private fun smanjiKolicinu(proizvod: Proizvod) {
        Toast.makeText(this, "Smanjio kolicinu za ${proizvod.naziv}", Toast.LENGTH_SHORT).show()
    }


}
