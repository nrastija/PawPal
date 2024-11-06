/*package com.example.pawpal.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        toggle.isDrawerIndicatorEnabled = true

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TODO -> Issue #15 - Hamburger menu, task 7
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
*/
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Početni layout - Login
        setContentView(R.layout.f01_loginlayout)

        // Inicijalizacija dugmadi
        val loginButton: Button = findViewById(R.id.btnLogin)
        val btnProziran: Button = findViewById(R.id.btnProziran)

        loginButton.setOnClickListener {
            performLogin()
        }

        // Otvaranje registracije
        btnProziran.setOnClickListener {
            openRegistrationLayout()
        }
    }

    private fun performLogin() {
        val usernameInput = findViewById<EditText>(R.id.txtKorime2).text.toString()
        val passwordInput = findViewById<EditText>(R.id.txtLozinka).text.toString()

        // Verifikacija korisničkog imena i lozinke
        if (usernameInput == "nrastija22" && passwordInput == "doberman") {
            // Ako je login uspešan, otvara glavni sadržaj
            setContentView(R.layout.activity_main)
            initializeDrawer()
        } else {
            Toast.makeText(this, "Netočan unos", Toast.LENGTH_SHORT).show()
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

        // Slušatelj za stavke menija
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
            openLoginLayout()
        } else {
            openRegistrationLayout()
        }
        isRegistrationLayoutActive = !isRegistrationLayoutActive
    }

    private fun openRegistrationLayout() {
        // Uključujemo layout registracije i omogućavamo povratak na login
        setContentView(R.layout.f01_registrationlayout)

        val btnProziran2: Button = findViewById(R.id.btnProziran2)
        btnProziran2.setOnClickListener {
            toggleLayout()
        }
    }

    private fun openLoginLayout() {
        // Vraćamo se na login ekran
        setContentView(R.layout.f01_loginlayout)

        val loginButton: Button = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            performLogin()
        }

        val btnProziran: Button = findViewById(R.id.btnProziran)
        btnProziran.setOnClickListener {
            toggleLayout()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::toggle.isInitialized && toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
