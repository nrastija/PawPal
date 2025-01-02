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

class PromjenaLozinkeActivity : BaseActivity() {

    private lateinit var korisnikDataSource: KorisnikDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_promjena_lozinke)

        val toolbar: Toolbar = findViewById(R.id.toolbarPromjenaLozinke)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutPromjenaLozinke)
        val navView: NavigationView = findViewById(R.id.navPromjenaLozinke)

        setupHamburgerMenu(drawerLayout, toolbar, navView)


        val database = (application as PawPalApplication).database
        korisnikDataSource = KorisnikDataSourceImpl(database)

        val staraLozinkaEditText = findViewById<EditText>(R.id.StaraLozinka)
        val novaLozinkaEditText = findViewById<EditText>(R.id.novaLozinka)
        val potvrdiNovuLozinkuEditText = findViewById<EditText>(R.id.NovaLozinka)

        val btnSpremiP: Button = findViewById(R.id.btnSpremiP)

        btnSpremiP.setOnClickListener {
            val staraLozinka = staraLozinkaEditText.text.toString()
            val novaLozinka = novaLozinkaEditText.text.toString()
            val potvrdiNovuLozinku = potvrdiNovuLozinkuEditText.text.toString()


            if (staraLozinka.isEmpty() || novaLozinka.isEmpty() || potvrdiNovuLozinku.isEmpty()) {
                prikaziPoruku("Sva polja moraju biti popunjena.")
                return@setOnClickListener
            }

            if (novaLozinka.length < 6) {
                prikaziPoruku("Nova lozinka mora sadržavati najmanje 6 znakova.")
                return@setOnClickListener
            }

            if (novaLozinka != potvrdiNovuLozinku) {
                prikaziPoruku("Nova lozinka i potvrda lozinke se ne podudaraju.")
                return@setOnClickListener
            }


            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val korisnikID = KorisnikManager.dajUlogiranogKorisnika() ?: return@launch


                    val trenutniKorisnik = korisnikDataSource.dajKorisnikaPoID(korisnikID)
                    if (trenutniKorisnik == null || trenutniKorisnik.lozinka != staraLozinka) {
                        withContext(Dispatchers.Main) {
                            prikaziPoruku("Trenutna lozinka nije tačna.")
                        }
                        return@launch
                    }


                    korisnikDataSource.azurirajLozinku(
                        korisnikID = korisnikID,
                        novaLozinka = novaLozinka
                    )

                    withContext(Dispatchers.Main) {
                        prikaziPoruku("Lozinka uspješno promenjena!")
                        startActivity(Intent(this@PromjenaLozinkeActivity, ProfilKorisnikaActivity::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        prikaziPoruku("Greška pri promijeni lozinke: ${e.message}")
                    }
                }
            }
        }
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(this, poruka, Toast.LENGTH_SHORT).show()
    }
}
