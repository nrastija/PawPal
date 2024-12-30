package com.example.pawpal.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.data.datasource.PasDataSource
import com.example.pawpal.data.impl.PasDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.BaseActivity
import com.example.pawpal.main.PawPalApplication
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrikazProfilaPsaActivity : BaseActivity() {

    private lateinit var pasDataSource: PasDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_prikaz_profila_psa)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfilPsa)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutProfilPsa)
        val navView: NavigationView = findViewById(R.id.navProfilPsa)

        setupHamburgerMenu(drawerLayout, toolbar, navView)


        val database = (application as PawPalApplication).database
        pasDataSource = PasDataSourceImpl(database)


        prikaziPodatkePsa()
    }

    private fun prikaziPodatkePsa() {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID != null) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val pas = pasDataSource.dohvatiPasPoKorisnikID(korisnikID)
                    withContext(Dispatchers.Main) {
                        if (pas != null) {
                            prikaziPodatke(pas)
                        } else {
                            prikaziPoruku("Profil psa nije pronađen.")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        prikaziPoruku("Greška pri dohvaćanju podataka: ${e.message}")
                    }
                }
            }
        } else {
            prikaziPoruku("Korisnik nije prijavljen.")
        }
    }

    private fun prikaziPodatke(pas: appdatabase.Pas) {
        val imeTextView: TextView = findViewById(R.id.podatakIme)
        val dobTextView: TextView = findViewById(R.id.podatakDob)
        val pasminaTextView: TextView = findViewById(R.id.podatakPasmina)
        val spolTextView: TextView = findViewById(R.id.podatakSpol)
        val kilazaTextView: TextView = findViewById(R.id.podatakKilaza)

        imeTextView.text = pas.ime
        dobTextView.text = pas.dob
        pasminaTextView.text = pas.pasmina
        spolTextView.text = pas.spol
        kilazaTextView.text = pas.kilaza
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show()
    }
}
