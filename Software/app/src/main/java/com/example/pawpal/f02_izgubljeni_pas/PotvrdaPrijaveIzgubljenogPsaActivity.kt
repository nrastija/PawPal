package com.example.pawpal.f02_izgubljeni_pas

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R
import org.w3c.dom.Text

class PotvrdaPrijaveIzgubljenogPsaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_potvrda_prijave_izgubljenog_psa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        opisPsa = findViewById(R.id.opisPsa)
        zadnjalokacija = findViewById(R.id.zadnjalokacija)
        slikapsa = findViewById(R.id.slikapsa)



        val opis = intent.getStringExtra("opis")
        val lokacija = intent.getStringExtra("lokacija")
        val slikaUriString = intent.getStringExtra("slika")

        opisPsa.text = opis ?: "Nije unesen opis"
        zadnjalokacija.text = lokacija ?: "Nije unesena zadnje viđena lokacija"

        slikaUriString.let {
            val slika = Uri.parse(it)
            slikapsa.setImageURI(slika)
        }


    }

    private lateinit var opisPsa: TextView
    private lateinit var zadnjalokacija: TextView
    private lateinit var slikapsa: ImageView

}