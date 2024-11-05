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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1: Set the login layout as the initial view
        setContentView(R.layout.f01_loginlayout)

        // Initialize login button
        val loginButton: Button = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    // Step 2: Perform the login check
    private fun performLogin() {
        // Example credentials (for testing, replace with real logic as needed)
        val usernameInput = findViewById<EditText>(R.id.txtKorime2).text.toString()
        val passwordInput = findViewById<EditText>(R.id.txtLozinka).text.toString()

        if (usernameInput == "nrastija22" && passwordInput == "doberman") {
            // On successful login, switch to the main activity layout with the navigation drawer
            setContentView(R.layout.activity_main)

            // Initialize toolbar, drawer, and navigation
            initializeDrawer()
        } else {
            // Show an error message if login fails
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }

    // Step 3: Initialize drawer and toolbar for activity_main layout
    private fun initializeDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up the navigation item click listeners
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
                // Add other navigation cases as needed
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::toggle.isInitialized && toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
