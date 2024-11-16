package com.example.pawpal.f12_shop

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.pawpal.R
import com.example.pawpal.f12_shop.entiteti.Proizvod

class ProizvodDetaljActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_product_detail)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Pronalazak view elemenata
        val slikaProizvoda: ImageView = findViewById(R.id.slikaProizvoda)
        val nazivProizvoda: TextView = findViewById(R.id.nazivProizvoda)
        val cijenaProizvoda: TextView = findViewById(R.id.cijenaProizvoda)
        val kategorijaProizvoda: TextView = findViewById(R.id.kategorijaProizvoda)
        val opisProizvoda: TextView = findViewById(R.id.opisProizvoda)
        val spinnerKolicina: Spinner = findViewById(R.id.odabirKolicineSpinner)
        val gumbDodajUKosaricu: Button = findViewById(R.id.dodajProizvodUKosaricu)

        val naziv = intent.getStringExtra("nazivProizvoda")
        val cijena = intent.getDoubleExtra("cijenaProizvoda", 0.0)
        val opis = intent.getStringExtra("opisProizvoda")
        val kategorija = intent.getIntExtra("kategorijaProizvoda", 0)
        val nazivSlike = intent.getStringExtra("imageUrl")

        nazivProizvoda.text = naziv
        cijenaProizvoda.text = "Cijena: $cijena €"
        opisProizvoda.text = opis
        kategorijaProizvoda.text = "Kategorija: $kategorija"
        val slikaID = resources.getIdentifier(nazivSlike, "drawable", packageName)
        if (slikaID != 0) {
            slikaProizvoda.setImageResource(slikaID)
        } else {
            slikaProizvoda.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        //adapter za spinner
        val kolicinaList = listOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kolicinaList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKolicina.adapter = adapter


        gumbDodajUKosaricu.setOnClickListener {
            val proizvod = Proizvod(
                proizvodID = intent.getIntExtra("sifraProizvoda", 0),
                naziv = intent.getStringExtra("nazivProizvoda") ?: "",
                cijena = intent.getDoubleExtra("cijenaProizvoda", 0.0),
                opis = intent.getStringExtra("opisProizvoda") ?: "",
                kategorijaID = intent.getIntExtra("kategorijaProizvoda", 0),
                kolicina = spinnerKolicina.selectedItem.toString().toInt(),
                imageUrl = nazivSlike
            )

            Toast.makeText(this, "Dodan ${naziv} u košaricu!", Toast.LENGTH_SHORT).show()
            KosaricaManager.dodajProizvodLista(proizvod)
        }
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

}