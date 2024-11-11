package com.example.pawpal.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.ActionBarDrawerToggle

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var isRegistrationLayoutActive = false
    private val korisnici = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otvoriPrijavu() // Start with login screen
    }

    private fun otvoriPrijavu() {
        setContentView(R.layout.f01_loginlayout)

        val loginButton: Button = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            ulogirajse()
        }

        val btnProziranPri: Button = findViewById(R.id.btnProziranPri)
        btnProziranPri.setOnClickListener {
            toggleLayout() // Switch to registration layout
        }
    }

    private fun ulogirajse() {
        val korimeUnos = findViewById<EditText>(R.id.editKorime2).text.toString()
        val lozinkaUnos = findViewById<EditText>(R.id.editLozinka2).text.toString()

        if (korisnici.any { it.first == korimeUnos && it.second == lozinkaUnos }) {
            Toast.makeText(this, "Uspješna prijava", Toast.LENGTH_SHORT).show()
            setContentView(R.layout.activity_main)
            initializeDrawer()
        } else {
            Toast.makeText(this, "Netočni podaci", Toast.LENGTH_SHORT).show()
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

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_finance -> Toast.makeText(this, "Finance clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_reservations -> Toast.makeText(this, "Reservations clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_veterinar -> Toast.makeText(this, "Veterinar clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_spa -> Toast.makeText(this, "Spa clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_school -> Toast.makeText(this, "School clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_adoption -> Toast.makeText(this, "Adoption clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_lost_dogs -> Toast.makeText(this, "Lost dogs clicked", Toast.LENGTH_SHORT).show()
                R.id.nav_shop -> Toast.makeText(this, "Shop clicked", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawers()
            true
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

    private fun korisnikRegistracija() {
        val korime = findViewById<EditText>(R.id.editKorime).text.toString()
        val lozinka = findViewById<EditText>(R.id.editLozinka).text.toString()

        if (korime.isNotEmpty() && lozinka.isNotEmpty()) {
            korisnici.add(Pair(korime, lozinka))
            Toast.makeText(this, "Uspješna registracija!", Toast.LENGTH_SHORT).show()
            otvoriPrijavu()
        } else {
            Toast.makeText(this, "Molim popunite sve podatke.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::toggle.isInitialized && toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
