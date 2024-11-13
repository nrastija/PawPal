package com.example.pawpal.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.pawpal.f01_autorizacija.entiteti.Korisnik
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.pawpal.f11_profil.ProfilKorisnikaActivity

class MainActivity : BaseActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var isRegistrationLayoutActive = false
    private lateinit var sharedPreferences: SharedPreferences
    override lateinit var toggle: ActionBarDrawerToggle

    private val korisnici = mutableListOf<Korisnik>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("KorisnikPrefs", Context.MODE_PRIVATE)
        otvoriPrijavu()
    }

    private fun otvoriPrijavu() {
        setContentView(R.layout.f01_loginlayout)

        val loginButton: Button = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            ulogirajse()
        }

        val btnProziranPri: Button = findViewById(R.id.btnProziranPri)
        btnProziranPri.setOnClickListener {
            toggleLayout()
        }
    }

    private fun ulogirajse() {
        val korimeUnos = findViewById<EditText>(R.id.editKorime2).text.toString()
        val lozinkaUnos = findViewById<EditText>(R.id.editLozinka2).text.toString()

        // Make sure we're checking the right SharedPreferences name
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val savedKorime = sharedPreferences.getString("korisnikKorime", "")
        val savedLozinka = sharedPreferences.getString("korisnikLozinka", "")

        if (korimeUnos == savedKorime && lozinkaUnos == savedLozinka) {
            Toast.makeText(this, "Uspješna prijava", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.activity_main)

            val toolbar: Toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)

            val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
            val navView: NavigationView = findViewById(R.id.nav_view)

            setupHamburgerMenu(drawerLayout, toolbar, navView)
            initializeDrawer()
        } else {
            Toast.makeText(this, "Netočni podaci", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleLayout() {
        if (isRegistrationLayoutActive) {
            otvoriPrijavu()
        } else {
            otvoriRegistraciju()
        }
        isRegistrationLayoutActive = !isRegistrationLayoutActive
    }

    private fun otvoriRegistraciju() {
        setContentView(R.layout.f01_registrationlayout)

        val btnProziranReg: Button = findViewById(R.id.btnProziranReg)
        btnProziranReg.setOnClickListener {
            toggleLayout()
        }

        val btnRegistriraj: Button = findViewById(R.id.btnRegistriraj)
        btnRegistriraj.setOnClickListener {
            korisnikRegistracija()
        }
    }

    private fun initializeDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun korisnikRegistracija() {
        val korime = findViewById<EditText>(R.id.editKorime).text.toString()
        val lozinka = findViewById<EditText>(R.id.editLozinka).text.toString()
        val ime = findViewById<EditText>(R.id.editIme).text.toString()
        val prezime = findViewById<EditText>(R.id.editPrezime).text.toString()
        val email = findViewById<EditText>(R.id.editEmail).text.toString()

        if (korime.isNotEmpty() && lozinka.isNotEmpty() && ime.isNotEmpty() && prezime.isNotEmpty() && email.isNotEmpty()) {
            val korisnik = Korisnik(korime, lozinka, ime, prezime, email)
            korisnici.add(korisnik)

            // Save credentials in SharedPreferences under the same name
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("korisnikIme", ime)
            editor.putString("korisnikPrezime", prezime)
            editor.putString("korisnikEmail", email)
            editor.putString("korisnikKorime", korime)
            editor.putString("korisnikLozinka", lozinka)
            editor.apply()

            // Show a success message
            Toast.makeText(this, "Uspješna registracija!", Toast.LENGTH_SHORT).show()

            // Reset to the login screen
            otvoriPrijavu()
        } else {
            Toast.makeText(this, "Molim popunite sve podatke.", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
