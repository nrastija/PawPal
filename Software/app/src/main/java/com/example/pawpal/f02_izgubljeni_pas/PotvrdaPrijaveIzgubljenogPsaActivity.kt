package com.example.pawpal.f02_izgubljeni_pas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.data.impl.IzgubljeniPsiImpl
import com.example.pawpal.main.MainActivity
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

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
        odustani = findViewById(R.id.odustaniGumb)
        potvrdi = findViewById(R.id.potvrdi)

        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "database.db")
        val db = AppDatabase(driver)
        dataSource = IzgubljeniPsiImpl(db)


        val opis = intent.getStringExtra("opis")
        val lokacija = intent.getStringExtra("lokacija")
        val slikaUriString = intent.getStringExtra("slika")

        opisPsa.text = opis ?: "Nije unesen opis"
        zadnjalokacija.text = lokacija ?: "Nije unesena zadnje viđena lokacija"

        slikaUriString.let {
            val slika = Uri.parse(it)
            slikapsa.setImageURI(slika)
        }

        odustani.setOnClickListener{
            finish()
        }

        potvrdi.setOnClickListener{
            if (opis != null && lokacija != null && slikaUriString != null) {

                lifecycleScope.launch {
                    saveLostDogs(opis, lokacija, slikaUriString)
                }

            }
            Toast.makeText(this, "Prijava psa potvrđena!", Toast.LENGTH_SHORT).show()

            }

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

    private suspend fun saveLostDogs(opis: String, lokacija: String, slikaUriString: String) {
        dataSource.dodajIzgubljenogPsa(opis, lokacija, slikaUriString)
    }


    private lateinit var opisPsa: TextView
    private lateinit var zadnjalokacija: TextView
    private lateinit var slikapsa: ImageView
    private lateinit var odustani: Button
    private lateinit var potvrdi: Button

    private lateinit var dataSource: IzgubljeniPsiImpl
}