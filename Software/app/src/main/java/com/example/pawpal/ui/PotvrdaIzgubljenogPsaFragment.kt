package com.example.pawpal.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.impl.IzgubljeniPsiImpl
import com.example.pawpal.main.MainActivity
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PotvrdaIzgubljenogPsaFragment: AppCompatActivity() {


    private lateinit var opisPsa: TextView
    private lateinit var zadnjaLokacija: TextView
    private lateinit var slikaPsa: ImageView
    private lateinit var odustani: Button
    private lateinit var potvrdi: Button

    lateinit var database: AppDatabase
    private lateinit var izgubljeniPsiDataSource: IzgubljeniPsiImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_potvrda_prijave_izgubljenog_psa)

        opisPsa = findViewById(R.id.opisPsa)
        zadnjaLokacija = findViewById(R.id.zadnjalokacija)
        slikaPsa = findViewById(R.id.slikapsa)
        odustani = findViewById(R.id.odustaniGumb)
        potvrdi = findViewById(R.id.potvrdi)

        database = (application as PawPalApplication).database
        izgubljeniPsiDataSource = IzgubljeniPsiImpl(database)

        val opis = intent.getStringExtra("opis")
        val lokacija = intent.getStringExtra("lokacija")
        val slikaUriString = intent.getStringExtra("slika")

        opisPsa.text = opis ?: "Nema opisa"
        zadnjaLokacija.text = lokacija ?: "Nema zadnje lokacije"
        slikaUriString?.let {
            val slika = Uri.parse(it)
            slikaPsa.setImageURI(slika)
        }

        odustani.setOnClickListener {
            finish()
        }

        potvrdi.setOnClickListener {
            if (opis != null && lokacija != null && slikaUriString != null) {
                lifecycleScope.launch {
                try {
                    izgubljeniPsiDataSource.dodajIzgubljenogPsa(opis, lokacija, slikaUriString)
                    Toast.makeText(this@PotvrdaIzgubljenogPsaFragment, "Uspješna prijava izgubljenog psa!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PotvrdaIzgubljenogPsaFragment, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                catch (e:Exception){
                    Toast.makeText(this@PotvrdaIzgubljenogPsaFragment, "Greška pri spremanju: ${e.message}", Toast.LENGTH_SHORT).show()
                }
               }
            }
        }
    }
}