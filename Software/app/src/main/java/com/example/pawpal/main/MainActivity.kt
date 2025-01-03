package com.example.pawpal.main

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
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.f04_veterinar.odabirVeterinaraActivity
import com.example.pawpal.f11_profil.ProfilKorisnikaActivity
import com.example.pawpal.data.impl.WishlistDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.ui.PregledWishlisteFragment
import com.example.pawpal.ui.ProfilKorisnikaFragment
import com.example.pawpal.ui.ShopFragment
import com.example.pawpal.ui.UdomljavanjeFragment
import com.example.pawpal.ui.SkolaFragment
import com.example.pawpal.ui.WishlistFragment
import com.google.android.material.navigation.NavigationView
import com.pawpal.appdatabase.AppDatabase

import kotlinx.coroutines.launch

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

        resetShopData()
        resetSkolaData()
    }

    private fun setupHamburgerMenu(drawerLayout: DrawerLayout, toolbar: Toolbar, navView: NavigationView) {
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> drawerLayout.closeDrawers()
                R.id.nav_profile -> navigateToFragment(ProfilKorisnikaFragment())
                R.id.nav_shop -> navigateToFragment(ShopFragment())
                R.id.nav_school -> navigateToFragment(SkolaFragment())
                R.id.nav_wishlist -> {
                    val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
                    if (korisnikID == null) {
                        Toast.makeText(this, "Korisnik nije prijavljen!", Toast.LENGTH_SHORT).show()
                    } else {
                        lifecycleScope.launch {
                            val wishlistDataSource = WishlistDataSourceImpl(database)
                            val status = wishlistDataSource.getWishlistStatus(korisnikID)
                            val fragment = if (status == 1L) PregledWishlisteFragment() else WishlistFragment()
                            if (fragment is DatabaseConsumer) {
                                fragment.database = database
                            }

                            navigateToFragment(fragment)
                        }
                    }
                }
                else -> Toast.makeText(this, "Feature not implemented yet", Toast.LENGTH_SHORT).show()

                R.id.nav_adoption -> navigateToFragment(UdomljavanjeFragment())

                else -> Toast.makeText(this, "Feature not implemented yet", Toast.LENGTH_SHORT)
                    .show()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun resetShopData() {
        val proizvodQueries = database.proizvodQueries
        val kategorijaQueries = database.kategorijaQueries

        proizvodQueries.transaction {
            proizvodQueries.deleteAllProizvods()
        }
        kategorijaQueries.transaction {
            kategorijaQueries.deleteAllKategorijas()
        }

        proizvodQueries.transaction {
            proizvodQueries.insertProizvod("Paramol 250ML", 14.99, "Lijek za pse protiv virusa", "proizvod_1", 1)
            proizvodQueries.insertProizvod("Reid Fills 400G", 11.98, "Hrana za pse u granulama", "proizvod_2", 2)
            proizvodQueries.insertProizvod("Pupino 3000x", 79.99, "Aparat za brijanje pasa", "proizvod_3", 3)
            proizvodQueries.insertProizvod("Groomer Elite Set", 49.99, "Set četki za održavanje higijene vašeg psa", "proizvod_4", 3)
            proizvodQueries.insertProizvod("Healthy Paws 2KG", 32.00, "Healthy paws zdrava hrana sa povrćem za pse", "proizvod_5", 2)
            proizvodQueries.insertProizvod("Healthy Paws Multivitamal", 32.00, "Multivitamin smjesa za zdravlje pasa, 90 kapsula", "proizvod_6", 1)
            proizvodQueries.insertProizvod("CozyPaw SleepPad", 74.50, "Udoban ergonomski krevet za pse, namijenjen za pse male do srednje veličine", "proizvod_7", 4)
        }

        kategorijaQueries.transaction {
            kategorijaQueries.insertKategorija(1, "Zdravlje")
            kategorijaQueries.insertKategorija(2, "Hrana")
            kategorijaQueries.insertKategorija(3, "Higijena")
            kategorijaQueries.insertKategorija(4, "Ostalo")
        }
    }

    private fun resetSkolaData() {
        val skolaQueries = database.skolaQueries
        val voditeljQueries = database.voditeljQueries
        val skolaVoditeljQueries = database.skolaVoditeljQueries


        skolaQueries.transaction {
            skolaQueries.deleteAllSkole()
        }

        skolaVoditeljQueries.transaction {
            skolaVoditeljQueries.deleteAllSkolaVoditelj()
        }
        voditeljQueries.transaction {
            voditeljQueries.deleteAllVoditelji()
        }

        skolaQueries.transaction {
            skolaQueries.insertSkola(1, "Osnovni trening", "Učenje osnovnih naredbi i poslušnosti za pse.", 150.00, "Ponedjeljak 10:00 - 12:00")
            skolaQueries.insertSkola(2, "Napredni trening", "Napredne tehnike poslušnosti i socijalizacije.", 250.00, "Srijeda 14:00 - 16:00")
            skolaQueries.insertSkola(3, "Specijalizacija", "Specijalni treninzi za radne ili sportske pse.", 350.00, "Petak 09:00 - 11:00")
        }


        voditeljQueries.transaction {
            voditeljQueries.insertVoditelj(1, "Ivan", "Horvat", "ivan@example.com", "0912345678")
            voditeljQueries.insertVoditelj(2, "Ana", "Kovač", "ana@example.com", "0987654321")
            voditeljQueries.insertVoditelj(3, "Marko", "Novak", "marko@example.com", "0919876543")
        }


        skolaVoditeljQueries.transaction {
            skolaVoditeljQueries.insertSkolaVoditelj(1, 1)
            skolaVoditeljQueries.insertSkolaVoditelj(1, 2)
            skolaVoditeljQueries.insertSkolaVoditelj(2, 3)
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
}
