package com.example.pawpal.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.impl.WishlistDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.helper.AppPreferencesHelper
import com.example.pawpal.ui.PregledWishlisteFragment
import com.example.pawpal.ui.ProfilKorisnikaFragment
import com.example.pawpal.ui.OdabirPrijaveIliPregledaPsaFragment
import com.example.pawpal.ui.OdabirVeterinaraFragment
import com.example.pawpal.ui.PregledAktivnostiFragment
import com.example.pawpal.ui.PregledSvihAktivnostiFragment
import com.example.pawpal.ui.PregledZahtjevaFragment
import com.example.pawpal.ui.PregledRezervacijaFragment
import com.example.pawpal.ui.PrijavaActivity
import com.example.pawpal.ui.PromoPonudaFragment
import com.example.pawpal.ui.SPAFragment
import com.example.pawpal.ui.ShopFragment
import com.example.pawpal.ui.SkolaFragment
import com.example.pawpal.ui.SviTroskoviFragment
import com.example.pawpal.ui.TroskoviFragment
import com.example.pawpal.ui.UdomljavanjeFragment
import com.example.pawpal.ui.UpravljanjeUdomljavanjemPasaFragment
import com.example.pawpal.ui.WishlistFragment
import com.google.android.material.navigation.NavigationView
import com.pawpal.appdatabase.AppDatabase

import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var database: AppDatabase
    private lateinit var appPreferences: AppPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = (application as PawPalApplication).database
        appPreferences = AppPreferencesHelper(this)

        setImagesVisibility(View.VISIBLE)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val headerView = navView.getHeaderView(0)

        val usernameTextView: TextView = headerView.findViewById(R.id.username_hamburger)
        val mailTextView: TextView = headerView.findViewById(R.id.mail_hamburger)

        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()

        if (korisnikID != null) {
            val logiraniKorisnikInfo = database.korisnikQueries.dajKorisnikaPoID(korisnikID).executeAsOneOrNull()
            val korisnickoIme = logiraniKorisnikInfo?.korime
            val korisnickiMail = logiraniKorisnikInfo?.email

            usernameTextView.text = korisnickoIme
            mailTextView.text = korisnickiMail
        } else {
            usernameTextView.text = "temp"
            mailTextView.text = "temp@temp.com"
        }

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        if (appPreferences.isFirstLaunch()) {
            resetShopData()
            resetSkolaData()
            resetVeterinarianData()
            resetUslugaData()

            appPreferences.setFirstLaunchDone()
        }
    }

    private fun setupHamburgerMenu(drawerLayout: DrawerLayout, toolbar: Toolbar, navView: NavigationView) {
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        lifecycleScope.launch {
            val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
            if (korisnikID != null) {
                val logiraniKorisnik = database.korisnikQueries.dajKorisnikaPoID(korisnikID).executeAsOne()

                navView.menu.clear()
                if (logiraniKorisnik.tip_korisnika == 2L) {
                    navView.inflateMenu(R.menu.hamburger_menu_navigacija_admin)
                } else {
                    navView.inflateMenu(R.menu.hamburger_menu_navigacija)
                }

                navView.setNavigationItemSelectedListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.nav_home -> {
                            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            setImagesVisibility(View.VISIBLE)
                        }
                        R.id.nav_reservations -> navigateToFragment(PregledRezervacijaFragment())
                        R.id.nav_profile -> navigateToFragment(ProfilKorisnikaFragment())
                        R.id.nav_spa -> navigateToFragment(SPAFragment())
                        R.id.nav_promo -> navigateToFragment(PromoPonudaFragment())
                        R.id.nav_shop -> navigateToFragment(ShopFragment())
                        R.id.nav_school -> navigateToFragment(SkolaFragment())
                        R.id.nav_adoption -> navigateToFragment(UdomljavanjeFragment())
                        R.id.nav_adoption_admin -> navigateToFragment(UpravljanjeUdomljavanjemPasaFragment())
                        R.id.nav_users_adpotion -> navigateToFragment(PregledZahtjevaFragment())
                        R.id.nav_lost_dogs -> navigateToFragment(OdabirPrijaveIliPregledaPsaFragment())
                        R.id.nav_veterinar -> navigateToFragment(OdabirVeterinaraFragment())
                        R.id.nav_finance -> navigateToFragment(TroskoviFragment())
                        R.id.nav_finance2 -> navigateToFragment(SviTroskoviFragment())
                        R.id.nav_activity -> navigateToFragment(PregledAktivnostiFragment())
                        R.id.nav_activity2 -> navigateToFragment(PregledSvihAktivnostiFragment())
                        R.id.nav_wishlist -> {
                            val currentKorisnikID = KorisnikManager.dajUlogiranogKorisnika()
                            if (currentKorisnikID == null) {
                                runOnUiThread {
                                    Toast.makeText(this@MainActivity, "Korisnik nije prijavljen!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                lifecycleScope.launch {
                                    val wishlistDataSource = WishlistDataSourceImpl(database)
                                    val status = wishlistDataSource.getWishlistStatus(currentKorisnikID)
                                    val fragment = if (status == 1L) PregledWishlisteFragment() else WishlistFragment()
                                    if (fragment is DatabaseConsumer) {
                                        fragment.database = database
                                    }
                                    navigateToFragment(fragment)
                                }
                            }
                        }
                        R.id.nav_odjava -> {
                            KorisnikManager.odjava()
                            val intent = Intent(this@MainActivity, PrijavaActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                            true
                        }
                        else -> {
                            runOnUiThread {
                                Toast.makeText(this@MainActivity, "Feature not implemented yet", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    drawerLayout.closeDrawers()
                    true
                }
            }
        }
    }

    public fun resetPromoData(){
        val promoQueries = database.promoPonudaQueries
        promoQueries.transaction {
            promoQueries.obrisiSvePromoponude()
        }

        promoQueries.transaction {
            promoQueries.insertPromoPonuda(
                "Senior njega",
                "Poseban tretman za pse starije od 7 godina uz 25% popusta",
                "27.01.2025.",
                "Potrebno donijeti veterinarsku dokumentaciju"
            )
            promoQueries.insertPromoPonuda(
                "Duo paket",
                "Dovedite dva psa i ostvarite 30% popusta na tretman za oba ljubimca",
                "19.05.2025.",
                "Potrebna prethodna rezervacija termina"
            )
            promoQueries.insertPromoPonuda(
                "Happy Hour",
                "Svaki dan od 14-16h sve usluge uz 15% popusta",
                "01.04.2025.",
                "Vrijedi za sve tretmane osim usluge općeninog uljepšavanja"
            )
            promoQueries.insertPromoPonuda(
                "Štene paket",
                "Poseban tretman za štence do 6 mjeseci starosti uz gratis igračku",
                "01.01.2025.",
                "Potrebno donijeti veterinarsku dokumentaciju"
            )
        }
    }

    private fun resetShopData() {
        val proizvodQueries = database.proizvodQueries

        proizvodQueries.transaction {
            proizvodQueries.deleteAllProizvods()
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
    }

    private fun resetVeterinarianData() {
        val vetQueries = database.veterinarQueries

        vetQueries.transaction {
            vetQueries.izbrisiSveVeterinare()
        }

        vetQueries.transaction {
            vetQueries.dodajVeterinara(
                "Luka Zorić",
                "Dr.spec",
                "0934567635",
            )
            vetQueries.dodajVeterinara(
                "Ana Anić",
                "Dr.vet.spec",
                "093454667",

                )
            vetQueries.dodajVeterinara(
                "Miro Mirić",
                "Dr.vet.spec",
                "094523445",

                )

            vetQueries.dodajVeterinara(
                "Ante Stanislav",
                "Dr.spec",
                "094523445",

                )

            vetQueries.dodajVeterinara(
                "Ankica Narić",
                "Dr.med.vet",
                "094523445",

                )
        }
    }

    private fun resetUslugaData() {
        val vetQueries = database.vrstaUslugeQueries

        vetQueries.transaction {
            vetQueries.izbrisiSveUsluge()
        }


        vetQueries.transaction {
            vetQueries.dodajVrstuUsluge(
                "Odaberite uslugu",
                "",
            )

            vetQueries.dodajVrstuUsluge(
                "Prvi Pregled",
                "50€",
            )
            vetQueries.dodajVrstuUsluge(
                "Kontrola",
                "50€",

                )
            vetQueries.dodajVrstuUsluge(
                "Cijepljenje",
                "100€",

                )
            vetQueries.dodajVrstuUsluge(
                "Laboratorijska dijagnostika",
                "150€",

                )
            vetQueries.dodajVrstuUsluge(
                "Dermatologija",
                "100€",

                )
            vetQueries.dodajVrstuUsluge(
                "Kirurgija",
                "200€",

                )
            vetQueries.dodajVrstuUsluge(
                "Neurologija",
                "190€",

                )
            vetQueries.dodajVrstuUsluge(
                "Oftamologija",
                "130€",

                )
            vetQueries.dodajVrstuUsluge(
                "Stomatologija",
                "120€",

                )
        }
    }

    private fun resetSkolaData() {
        val skolaQueries = database.skolaQueries
        val voditeljQueries = database.voditeljQueries
        val skolaVoditeljQueries = database.skolaVoditeljQueries

        skolaVoditeljQueries.transaction {
            skolaVoditeljQueries.deleteAllSkolaVoditelj()
        }

        skolaQueries.transaction {
            skolaQueries.deleteAllSkole()
        }

        voditeljQueries.transaction {
            voditeljQueries.deleteAllVoditelji()
        }

        voditeljQueries.transaction {
            voditeljQueries.insertVoditelj(1, "Ivan", "Horvat", "ivan.horvatHR92@gmail.com", "0912345678")
            voditeljQueries.insertVoditelj(2, "Ana", "Kovač", "ana.kovac4412@gmail.com", "0987654321")
            voditeljQueries.insertVoditelj(3, "Marko", "Novak", "markonovak84@gmail.com", "0919876543")
            voditeljQueries.insertVoditelj(4, "Elza", "Rakitić", "elrakitic998877@gmail.com", "0919876543")
        }

        skolaQueries.transaction {
            skolaQueries.insertSkola(1, "Osnovni trening", "Učenje osnovnih naredbi i poslušnosti za pse.", 150.00, "Ponedjeljak 10:00 - 12:00")
            skolaQueries.insertSkola(2, "Napredni trening", "Napredne tehnike poslušnosti i socijalizacije.", 250.00, "Srijeda 14:00 - 16:00")
            skolaQueries.insertSkola(3, "Specijalizacija", "Specijalni treninzi za radne ili sportske pse.", 350.00, "Petak 09:00 - 11:00")
        }

        skolaVoditeljQueries.transaction {
            skolaVoditeljQueries.insertSkolaVoditelj(1, 1)
            skolaVoditeljQueries.insertSkolaVoditelj(1, 2)
            skolaVoditeljQueries.insertSkolaVoditelj(2, 3)
            skolaVoditeljQueries.insertSkolaVoditelj(3, 4)
        }
    }

    public fun resetSPAData(){
        val queriesSPA = database.uslugaQueries
        queriesSPA.transaction {
            queriesSPA.deleteAllUsluga() }

        queriesSPA.transaction {
            queriesSPA.insertUsluga(
                "Općenito uljepšavanje",
                50.00,
                "Kompleksan tretman koji uključuje kupanje, šišanje, četkanje, čišćenje ušiju i rezanje noktiju. Savršeno rješenje za sveobuhvatnu njegu vašeg psa u jednom dolasku.",
                90,
                "spa1")
        }

        queriesSPA.transaction {
            queriesSPA.insertUsluga(
                "Šišanje",
                35.00,
                "U kombiniranom tretmanu, vaš pas dobiva profesionalno šišanje prilagođeno njegovoj pasmini, uz sigurno i nježno rezanje noktiju. Brinemo o udobnosti i sigurnosti vašeg ljubimca koristeći visokokvalitetne alate i pristup prilagođen svakom psu.",
                75,
                "spa2")
        }

        queriesSPA.transaction {
            queriesSPA.insertUsluga(
                "Kupanje i feniranje",
                25.00,
                "Vaš pas će uživati u profesionalnom kupanju s visokokvalitetnim šamponima prilagođenima njegovom tipu dlake i koži. Nakon toga slijedi nježno sušenje fenom i uređivanje dlake kako bi izgledao čisto, svježe i dotjerano.",
                45,
                "spa4")
        }

        queriesSPA.transaction {
            queriesSPA.insertUsluga(
                "Četkanje",
                15.00,
                "Temeljito četkanje za uklanjanje mrtve dlake, zapetljaja i poddlake. Ova usluga osigurava zdrav i sjajan izgled dlake te pomaže u sprječavanju nastanka čvorova.",
                30,
                "spa5")
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
