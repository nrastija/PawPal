package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class KreiranjeProfilaPsaActivity : BaseActivity() {

    private lateinit var pasDataSource: PasDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_kreiranje_profila_psa)

        val toolbar: Toolbar = findViewById(R.id.toolbarKreiranjePsa)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutKreiranjePsa)
        val navView: NavigationView = findViewById(R.id.navKreiranjePsa)
        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val database = (application as PawPalApplication).database
        pasDataSource = PasDataSourceImpl(database)

        val btnKreirajProfil: Button = findViewById(R.id.btnKreirajProfil)
        btnKreirajProfil.setOnClickListener {
            val ime = findViewById<EditText>(R.id.Ime).text.toString()
            val dob = findViewById<EditText>(R.id.Dob).text.toString()
            val pasmina = findViewById<EditText>(R.id.Pasmina).text.toString()
            val spol = findViewById<EditText>(R.id.Spol).text.toString()
            val kilaza = findViewById<EditText>(R.id.Kilaza).text.toString()

            if (ime.isNotEmpty() && dob.isNotEmpty() && pasmina.isNotEmpty() && spol.isNotEmpty() && kilaza.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val korisnikID = KorisnikManager.dajUlogiranogKorisnika() ?: return@launch
                    pasDataSource.insertPas(ime, dob, pasmina, spol, kilaza, korisnikID)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@KreiranjeProfilaPsaActivity, "Profil psa je uspješno kreiran!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@KreiranjeProfilaPsaActivity, PrikazProfilaPsaActivity::class.java))
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Molimo ispunite sve podatke.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
