package com.example.pawpal.f12_shop

import android.os.Bundle
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawpal.R

class ProizvodDetaljActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_product_detail)

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

        nazivProizvoda.text = naziv
        cijenaProizvoda.text = "Cijena: $cijena €"
        opisProizvoda.text = opis
        kategorijaProizvoda.text = "Kategorija: $kategorija"
        slikaProizvoda.setImageResource(R.drawable.test_slika)


        gumbDodajUKosaricu.setOnClickListener {
            Toast.makeText(this, "Pritisnut gumb dodaj u košaricu", Toast.LENGTH_SHORT).show()
        }
    }

}