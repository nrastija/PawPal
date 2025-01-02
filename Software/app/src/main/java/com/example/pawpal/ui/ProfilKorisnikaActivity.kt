package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.datasource.PasDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.data.impl.PasDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.BaseActivity
import com.example.pawpal.main.PawPalApplication
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfilKorisnikaActivity : BaseActivity() {

    private lateinit var korisnikDataSource: KorisnikDataSource
    private lateinit var pasDataSource: PasDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_profil_korisnika)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfil)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutProfil)
        val navView: NavigationView = findViewById(R.id.navProfil)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val database = (application as PawPalApplication).database
        korisnikDataSource = KorisnikDataSourceImpl(database)
        pasDataSource = PasDataSourceImpl(database)

        postaviPodatkeKorisnika()

        val btnMojLjubimac: Button = findViewById(R.id.btnMojLjubimac)
        btnMojLjubimac.setOnClickListener {
            val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
            if (korisnikID != null) {
                GlobalScope.launch(Dispatchers.IO) {
                    val pas = pasDataSource.dohvatiPasPoKorisnikID(korisnikID)
                    withContext(Dispatchers.Main) {
                        if (pas != null) {

                            startActivity(Intent(this@ProfilKorisnikaActivity, PrikazProfilaPsaActivity::class.java))
                        } else {

                            startActivity(Intent(this@ProfilKorisnikaActivity, KreiranjeProfilaPsaActivity::class.java))
                        }
                    }
                }
            } else {
                prikaziPoruku("Nijedan korisnik nije prijavljen.")
            }
        }

        val btnUrediPodatkeK: Button = findViewById(R.id.btnUrediPodatkeK)
        btnUrediPodatkeK.setOnClickListener {
            val intent = Intent(this, UredivanjeProfilaKorisnika::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        postaviPodatkeKorisnika()
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
                        prikaziPodatke(korisnik)
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

    private fun prikaziPodatke(korisnik: appdatabase.Korisnik) {
        val imeTextView: TextView = findViewById(R.id.podatakIme)
        val prezimeTextView: TextView = findViewById(R.id.podatakPrezime)
        val emailTextView: TextView = findViewById(R.id.podatakMail)
        val korisnickoImeTextView: TextView = findViewById(R.id.podatakKorIme)

        imeTextView.text = korisnik.ime
        prezimeTextView.text = korisnik.prezime
        emailTextView.text = korisnik.email
        korisnickoImeTextView.text = korisnik.korime
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
