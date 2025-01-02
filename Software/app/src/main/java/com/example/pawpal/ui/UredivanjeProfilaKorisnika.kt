package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.BaseActivity
import com.example.pawpal.main.PawPalApplication
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UredivanjeProfilaKorisnika : BaseActivity() {

    private lateinit var korisnikDataSource: KorisnikDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_uredivanje_profila_korisnika)

        val toolbar: Toolbar = findViewById(R.id.toolbarUredivanjeK)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutUredivanjeK)
        val navView: NavigationView = findViewById(R.id.navUredivanjeK)

        setupHamburgerMenu(drawerLayout, toolbar, navView)


        val database = (application as PawPalApplication).database
        korisnikDataSource = KorisnikDataSourceImpl(database)


        postaviPodatkeKorisnika()

        val btnSpremi: Button = findViewById(R.id.btnSpremiP)
        btnSpremi.setOnClickListener {
            spremiPromjene()
        }


        val btnPromjeniLozinku: Button = findViewById(R.id.btnPromjeniLozinku)
        btnPromjeniLozinku.setOnClickListener {
            val intent = Intent(this, PromjenaLozinkeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postaviPodatkeKorisnika() {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID != null) {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val korisnik = withContext(Dispatchers.IO) {
                        korisnikDataSource.dajKorisnikaPoID(korisnikID)
                    }
                    if (korisnik != null) {
                        val imeEditText = findViewById<EditText>(R.id.ImePsa)
                        val prezimeEditText = findViewById<EditText>(R.id.Prezime)
                        val emailEditText = findViewById<EditText>(R.id.Mail)
                        val korimeEditText = findViewById<EditText>(R.id.KorIme)

                        imeEditText.setText(korisnik.ime)
                        prezimeEditText.setText(korisnik.prezime)
                        emailEditText.setText(korisnik.email)
                        korimeEditText.setText(korisnik.korime)
                    } else {
                        prikaziPoruku("Korisnik nije pronađen.")
                    }
                } catch (e: Exception) {
                    prikaziPoruku("Greška pri dohvaćanju podataka: ${e.message}")
                }
            }
        } else {
            prikaziPoruku("Nijedan korisnik nije prijavljen.")
        }
    }

    private fun spremiPromjene() {
        val imeEditText = findViewById<EditText>(R.id.ImePsa)
        val prezimeEditText = findViewById<EditText>(R.id.Prezime)
        val emailEditText = findViewById<EditText>(R.id.Mail)
        val korimeEditText = findViewById<EditText>(R.id.KorIme)

        val novoIme = imeEditText.text.toString()
        val novoPrezime = prezimeEditText.text.toString()
        val noviEmail = emailEditText.text.toString()
        val novoKorime = korimeEditText.text.toString()

        if (novoIme.isEmpty() || novoPrezime.isEmpty() || noviEmail.isEmpty() || novoKorime.isEmpty()) {
            prikaziPoruku("Molimo ispunite sva polja.")
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val korisnikID = KorisnikManager.dajUlogiranogKorisnika() ?: return@launch
                korisnikDataSource.azurirajKorisnika(
                    korisnikID = korisnikID,
                    korime = novoKorime,
                    ime = novoIme,
                    prezime = novoPrezime,
                    email = noviEmail
                )
                withContext(Dispatchers.Main) {
                    prikaziPoruku("Promjene su uspješno spremljene.")
                    startActivity(Intent(this@UredivanjeProfilaKorisnika, ProfilKorisnikaActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    prikaziPoruku("Greška pri spremanju podataka: ${e.message}")
                }
            }
        }
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show()
    }
}
