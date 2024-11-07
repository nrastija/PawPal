package com.example.pawpal.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.f04_veterinar.odabirVeterinaraActivity
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO -> promijeniti ovo

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

        val intent = Intent(this, odabirVeterinaraActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
