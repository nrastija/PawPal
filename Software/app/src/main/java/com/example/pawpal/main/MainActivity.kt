package com.example.pawpal.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.data.impl.KategorijaDataSourceImpl
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.data.impl.ProizvodDataSourceImpl
import com.example.pawpal.f04_veterinar.odabirVeterinaraActivity
import com.example.pawpal.f11_profil.ProfilKorisnikaActivity
import com.example.pawpal.ui.ShopFragment
import com.google.android.material.navigation.NavigationView
import com.pawpal.appdatabase.AppDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Show main images
        setImagesVisibility(View.VISIBLE)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        //Resetiranje - ciscenje podataka u BP
        //resetDatabase(this)

        //Instanciranje - instanca nove BP
        database = (application as PawPalApplication).database
        //resetDatabase(this)

        populateDatabase()
    }

    private fun setupHamburgerMenu(drawerLayout: DrawerLayout, toolbar: Toolbar, navView: NavigationView) {
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> drawerLayout.closeDrawers()
                R.id.nav_profile -> startActivity(Intent(this, ProfilKorisnikaActivity::class.java))
                R.id.nav_veterinar -> startActivity(Intent(this, odabirVeterinaraActivity::class.java))
                R.id.nav_shop -> navigateToFragment(ShopFragment())
                else -> Toast.makeText(this, "Feature not implemented yet", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        if (fragment is DatabaseConsumer) {
            fragment.database = database
        }

        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        setImagesVisibility(View.GONE)

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setImagesVisibility(visibility: Int) {
        findViewById<ImageView>(R.id.imageView2).visibility = visibility
        findViewById<ImageView>(R.id.imageView7).visibility = visibility
    }

    private fun populateDatabase() {
        val proizvodDataSource = ProizvodDataSourceImpl(database)

        val queriesProizvod = database.proizvodQueries
        queriesProizvod.transaction {
            queriesProizvod.insertProizvod("Paramol 250ML", 14.99, "Lijek za pse protiv virusa", "proizvod_1", 1)
            queriesProizvod.insertProizvod("Reid Fills 400G", 11.98, "Hrana za pse u granulama", "proizvod_2", 2)
            queriesProizvod.insertProizvod("Pupino 3000x", 79.99, "Aparat za brijanje pasa", "proizvod_3", 3)
            queriesProizvod.insertProizvod("Groomer Elite Set", 49.99, "Set četki za održavanje higijene vašeg psa", "proizvod_4", 3)
            queriesProizvod.insertProizvod("Healthy Paws 2KG", 32.00, "Healthy paws zdrava hrana sa povrćem za pse", "proizvod_5", 2)
            queriesProizvod.insertProizvod("Healthy Paws Multivitamal", 32.00, "Multivitamin smjesa za zdravlje pasa, 90 kapsula", "proizvod_6", 1)
            queriesProizvod.insertProizvod("CozyPaw SleepPad", 74.50, "Udoban ergonomski krevet za pse, namijenjen za pse male do srednje veličine", "proizvod_7", 4)
        }

        val kategorijaDataSource = KategorijaDataSourceImpl(database)

        val queriesKategorija = database.kategorijaQueries
        queriesKategorija.transaction{
            queriesKategorija.insertKategorija(1, "Zdravlje")
            queriesKategorija.insertKategorija(2, "Hrana")
            queriesKategorija.insertKategorija(3, "Higijena")
            queriesKategorija.insertKategorija(4, "Ostalo")

        }
    }

    /*fun resetDatabase(context: Context) {
        context.deleteDatabase("appdatabase.db")
    }*/
}

