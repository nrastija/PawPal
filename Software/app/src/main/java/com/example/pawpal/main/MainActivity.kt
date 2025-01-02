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
import com.example.pawpal.R
import com.example.pawpal.f04_veterinar.odabirVeterinaraActivity
import com.example.pawpal.f11_profil.ProfilKorisnikaActivity
import com.example.pawpal.ui.ShopFragment
import com.example.pawpal.ui.UdomljavanjeFragment
import com.google.android.material.navigation.NavigationView
import com.pawpal.appdatabase.AppDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setImagesVisibility(View.VISIBLE)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        database = (application as PawPalApplication).database

        resetShopData()
        resetAdoptionData()

    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setImagesVisibility(View.VISIBLE)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navView: NavigationView = findViewById(R.id.nav_view)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        // Instanciranje baze podataka
        database = (application as PawPalApplication).database

        // Dohvati sve korisnike
        val korisnici = database.korisnikQueries.dajSveKorisnike().executeAsList()

        // Brisanje stare baze
        deleteDatabase("appdatabase.db")

        // Kreiranje nove baze
        database = AppDatabase(
            AndroidSqliteDriver(
                AppDatabase.Schema,
                applicationContext,
                "appdatabase.db"
            )
        )

        // Vraćanje korisnika u bazu
        database.korisnikQueries.transaction {
            korisnici.forEach { korisnik ->
                database.korisnikQueries.dodajKorisnik(
                    korisnik.korime,
                    korisnik.ime,
                    korisnik.prezime,
                    korisnik.lozinka,
                    korisnik.email
                )
            }
        }

        // Popunjavanje baze podataka
        populateDatabase()
    }*/

    private fun setupHamburgerMenu(
        drawerLayout: DrawerLayout,
        toolbar: Toolbar,
        navView: NavigationView
    ) {
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
                R.id.nav_profile -> startActivity(
                    Intent(this, ProfilKorisnikaActivity::class.java)
                )
                R.id.nav_veterinar -> startActivity(
                    Intent(this, odabirVeterinaraActivity::class.java)
                )
                R.id.nav_shop -> navigateToFragment(ShopFragment())
                    /*{
                    val shopFragment = ShopFragment().apply {
                        database = (application as PawPalApplication).database
                    }
                    navigateToFragment(shopFragment)
                }*/

                R.id.nav_adoption -> navigateToFragment(UdomljavanjeFragment())
                    /*{
                val udomljavanjeFragment = UdomljavanjeFragment().apply {
                    database = (application as PawPalApplication).database
                }
                navigateToFragment(udomljavanjeFragment)
            }*/
                else -> Toast.makeText(this, "Feature not implemented yet", Toast.LENGTH_SHORT)
                    .show()
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun resetShopData(){
        val queriesProizvod = database.proizvodQueries
        val queriesKategorija = database.kategorijaQueries

        queriesProizvod.transaction {
            queriesProizvod.deleteAllProizvods()
        }
        queriesKategorija.transaction {
            queriesKategorija.deleteAllKategorijas()
        }


        queriesProizvod.transaction {
            queriesProizvod.insertProizvod("Paramol 250ML", 14.99, "Lijek za pse protiv virusa", "proizvod_1", 1)
            queriesProizvod.insertProizvod("Reid Fills 400G", 11.98, "Hrana za pse u granulama", "proizvod_2", 2)
            queriesProizvod.insertProizvod("Pupino 3000x", 79.99, "Aparat za brijanje pasa", "proizvod_3", 3)
            queriesProizvod.insertProizvod("Groomer Elite Set", 49.99, "Set četki za održavanje higijene vašeg psa", "proizvod_4", 3)
            queriesProizvod.insertProizvod("Healthy Paws 2KG", 32.00, "Healthy paws zdrava hrana sa povrćem za pse", "proizvod_5", 2)
        }

        queriesKategorija.transaction {
            queriesKategorija.insertKategorija(1, "Zdravlje")
            queriesKategorija.insertKategorija(2, "Hrana")
            queriesKategorija.insertKategorija(3, "Higijena")
            queriesKategorija.insertKategorija(4, "Ostalo")
        }


    }

    private fun resetAdoptionData(){
        val queriesPasUdomljavanje = database.pasUdomljavanjeQueries

        queriesPasUdomljavanje.transaction {
            queriesPasUdomljavanje.deleteAllPasUdomljavanje()
        }

        queriesPasUdomljavanje.transaction {
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Nara",
                9,
                "Ženka",
                "Nara je pas neodoljivog šarma i zaigrane naravi. Obožava provoditi vrijeme s ljudima i uvijek je spremna za maženje, u čemu istinski uživa. Njena najveća strast je pokazivanje ljubavi kroz veselo i neumorno lizanje, čime osvaja srca svih oko sebe. Osim toga, Nara je prava gurmanica – uvijek u potrazi za ukusnim zalogajem i nikada ne propušta priliku za omiljenu poslasticu. Njezina energija i privrženost čine je savršenim prijateljem koji unosi radost u svaki trenutak.",
                "25.03.2015.",
                8.8,
                "Shitzu-Maltezer",
                "Ne zahtijeva puno šetnji",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "nara", "nara2", "nara3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Zumi",
                1,
                "Ženka",
                "Nara je pas neodoljivog šarma i zaigrane naravi. Obožava provoditi vrijeme s ljudima i uvijek je spremna za maženje, u čemu istinski uživa. Njena najveća strast je pokazivanje ljubavi kroz veselo i neumorno lizanje, čime osvaja srca svih oko sebe. Osim toga, Nara je prava gurmanica – uvijek u potrazi za ukusnim zalogajem i nikada ne propušta priliku za omiljenu poslasticu. Njezina energija i privrženost čine je savršenim prijateljem koji unosi radost u svaki trenutak.",
                "25.03.2015.",
                8.80,
                "Shitzu-Maltezer",
                "Ne zahtijeva puno šetnji",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "zumi", "zumi", "zumi"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Kira",
                12,
                "Ženka",
                "Kira je labradorica čije mirne i mudre oči odražavaju godine ispunjene ljubavlju i odanošću. Iako je u zrelim godinama, i dalje zadržava nježnu i prijateljsku narav tipičnu za labradore. Voli mirne šetnje i opušteno vrijeme provedeno u društvu svoje obitelji. Kira je pravi primjer psa koji je kroz godine postao vjeran i pouzdan pratitelj, uvijek spreman pružiti utjehu i toplinu svojim voljenima. Njezina ljubav prema ljudima i umirujuća prisutnost čine je nezamjenjivim članom vaše obitelji.",
                "17.05.2012.",
                35.00,
                "Labrador",
                "Uživa u pažnji djece, alergična na kikiriki",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "kira", "kira", "kira"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Snupi",
                12,
                "Mužjak",
                "Snupi je nježan chow chow, poznat po svojoj impresivnoj, gustoj grivi i smirenoj prirodi. Iako je stariji, još uvijek zrači dostojanstvom i ljubaznošću. Njegova karakteristična smeđa dlaka daje mu izgled medvjedića, a na njegovom licu često možete primijetiti izraz tihe mudrosti. Snupi je vjerni prijatelj, koji uživa u mirnim šetnjama, udobnim trenucima odmora i svojoj omiljenoj hrskavici. Unatoč svojim godinama, on i dalje odražava tu posebnu, neponovljivu osobnost chow chow pasmine.",
                "15.12.2012.",
                41.60,
                "Chow chow",
                "Voli duge šetnje",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "snupi", "snupi", "snupi" )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Rex",
                10,
                "Mužjak",
                "Rex je 10-godišnji njemački ovčar, poznat po svojoj odanosti i inteligenciji. Iako je u zlatnim godinama, njegova energija i stražarska instinkta ostali su snažni. Sa svojim prepoznatljivim crno-smeđim krznom i izraženim mišićima, Rex zrači snagom i ponosom. Iako je mirniji nego u mladosti, još uvijek voli aktivne igre i šetnje, a naročito voli obiteljsku pažnju. Njegova zaštitnička priroda i odanost svojim vlasnicima ostali su nepromijenjeni, a sa svakim danom postaje sve mudriji i nježniji.",
                "23.07.2014.",
                40.10,
                "Njemački ovčar",
                "Problemi s kukovima",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "reks", "reks2", "reks3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Nala",
                3,
                "Ženka",
                "Nala je energična i vesela Jorkširski terijer, stara 3 godine. Iako je mala, njena osobnost je sve samo ne to! Ova preslatka djevojčica obožava pažnju i uživa u igri, bilo da je u dvorištu ili u udobnosti svog doma. Njena vesela narav i stalna želja za ljubavlju čine je savršenim prijateljem za obitelj. Jorkširski terijeri poznati su po svojoj hrabrosti i inteligenciji, a Nala nije iznimka - brzo se uči i voli biti u centru pažnje. Njen luksuzni zlatno-smeđi kaput zahtijeva malo pažnje, ali uz to dolazi nevjerojatan izgled koji će vas oduševiti svaki put kada je pogledate. Idealna je za obitelj koja traži malog, ali energičnog ljubimca koji će im pružiti puno ljubavi i radosti.",
                "15.06.2021.",
                6.5,
                "Jorkširski terijer",
                "Ne slaže se najbolje s drugim psima",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "nala","nala2","nala3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Maša",
                1,
                "Ženka",
                "Maša je mješanac njemačkog špica i pomeranca, mala je i živahna kuglica energije. S svojim gustim krznom izgleda poput pravog malog medvjedića. Maša je vesela, znatiželjna i uvijek spremna za igru, a njezina razigrana osobnost čini je omiljenom u obitelji. Iako je mala, njezina hrabrost i glasno lajanje često je čine vrlo odvažnom u društvu većih pasa. Zbog svoje inteligencije brzo uči nove trikove i voli biti u centru pažnje, uživajući u svakom trenutku provedenom s vlasnicima. Njezina privrženost i veselo ponašanje čine je izvrsnim pratiteljem za aktivan i sretan život.",
                "26.11.2023.",
                4.10,
                "Njemački špic-pomeranac",
                "Uživa u društvu djece, puno energije",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "masa","masa2", "masa"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Ref",
                4,
                "Mužjak",
                "Ref je 5-godišnji njemački oštrodlaki ptičar, lovački pas izuzetnih sposobnosti. S svojom gustim, oštrim krznom prilagođenim za rad u zahtjevnim uvjetima, Ref je pravi profesionalac u lovu, s izvrsnim instinktom za praćenje mirisa. Iako je najviše usmjeren na lovačke zadatke, njegova energična i vesela priroda također ga čini sjajnim članom obitelji. Uz visoku inteligenciju, Ref je vrlo poslušan i voljan raditi sa svojim vlasnicima, bilo da je u lovu ili uživa u obiteljskim trenucima. S godinama, stekao je i mudrost, ali i dalje zrači snagom i entuzijazmom za nove izazove.",
                "06.05.2020.",
                27.50,
                "Njemački oštrodlaki ptičar",
                "Zahtijeva duge šetnje, ima puno energije",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "ref","ref2","ref3"
            )
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
    /*private fun populateDatabase() {
        val proizvodDataSource = ProizvodDataSourceImpl(database)
        val queriesProizvod = database.proizvodQueries
        queriesProizvod.transaction {
            queriesProizvod.insertProizvod("Paramol 250ML", 14.99, "Lijek za pse protiv virusa", "proizvod_1", 1)
            queriesProizvod.insertProizvod("Reid Fills 400G", 11.98, "Hrana za pse u granulama", "proizvod_2", 2)
            queriesProizvod.insertProizvod("Pupino 3000x", 79.99, "Aparat za brijanje pasa", "proizvod_3", 3)
            queriesProizvod.insertProizvod("Groomer Elite Set", 49.99, "Set četki za održavanje higijene vašeg psa", "proizvod_4", 3)
            queriesProizvod.insertProizvod("Healthy Paws 2KG", 32.00, "Healthy paws zdrava hrana sa povrćem za pse", "proizvod_5", 2)
        }

        val kategorijaDataSource = KategorijaDataSourceImpl(database)
        val queriesKategorija = database.kategorijaQueries
        queriesKategorija.transaction {
            queriesKategorija.insertKategorija(1, "Zdravlje")
            queriesKategorija.insertKategorija(2, "Hrana")
            queriesKategorija.insertKategorija(3, "Higijena")
            queriesKategorija.insertKategorija(4, "Ostalo")
        }

        val PasUdomljavanjeDataSource = PasUdomljavanjeDataSourceImpl(database)
        val queriesPasUdomljavanje = database.pasUdomljavanjeQueries
        queriesKategorija.transaction {
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Nara",
                9,
                "Ženka",
                "Nara je pas neodoljivog šarma i zaigrane naravi. Obožava provoditi vrijeme s ljudima i uvijek je spremna za maženje, u čemu istinski uživa. Njena najveća strast je pokazivanje ljubavi kroz veselo i neumorno lizanje, čime osvaja srca svih oko sebe. Osim toga, Nara je prava gurmanica – uvijek u potrazi za ukusnim zalogajem i nikada ne propušta priliku za omiljenu poslasticu. Njezina energija i privrženost čine je savršenim prijateljem koji unosi radost u svaki trenutak.",
                "25.03.2015.",
                8.8,
                "Shitzu-Maltezer",
                "Ne zahtijeva puno šetnji",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "nara", "nara2", "nara3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Zumi",
                1,
                "Ženka",
                "Nara je pas neodoljivog šarma i zaigrane naravi. Obožava provoditi vrijeme s ljudima i uvijek je spremna za maženje, u čemu istinski uživa. Njena najveća strast je pokazivanje ljubavi kroz veselo i neumorno lizanje, čime osvaja srca svih oko sebe. Osim toga, Nara je prava gurmanica – uvijek u potrazi za ukusnim zalogajem i nikada ne propušta priliku za omiljenu poslasticu. Njezina energija i privrženost čine je savršenim prijateljem koji unosi radost u svaki trenutak.",
                "25.03.2015.",
                8.80,
                "Shitzu-Maltezer",
                "Ne zahtijeva puno šetnji",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "zumi", "zumi", "zumi")
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Kira",
                12,
                "Ženka",
                "Kira je labradorica čije mirne i mudre oči odražavaju godine ispunjene ljubavlju i odanošću. Iako je u zrelim godinama, i dalje zadržava nježnu i prijateljsku narav tipičnu za labradore. Voli mirne šetnje i opušteno vrijeme provedeno u društvu svoje obitelji. Kira je pravi primjer psa koji je kroz godine postao vjeran i pouzdan pratitelj, uvijek spreman pružiti utjehu i toplinu svojim voljenima. Njezina ljubav prema ljudima i umirujuća prisutnost čine je nezamjenjivim članom vaše obitelji.",
                "17.05.2012.",
                35.00,
                "Labrador",
                "Uživa u pažnji djece, alergična na kikiriki",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "kira", "kira", "kira")
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Snupi",
                12,
                "Mužjak",
                "Snupi je nježan chow chow, poznat po svojoj impresivnoj, gustoj grivi i smirenoj prirodi. Iako je stariji, još uvijek zrači dostojanstvom i ljubaznošću. Njegova karakteristična smeđa dlaka daje mu izgled medvjedića, a na njegovom licu često možete primijetiti izraz tihe mudrosti. Snupi je vjerni prijatelj, koji uživa u mirnim šetnjama, udobnim trenucima odmora i svojoj omiljenoj hrskavici. Unatoč svojim godinama, on i dalje odražava tu posebnu, neponovljivu osobnost chow chow pasmine.",
                "15.12.2012.",
                41.60,
                "Chow chow",
                "Voli duge šetnje",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "snupi", "snupi", "snupi" )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Rex",
                10,
                "Mužjak",
                "Rex je 10-godišnji njemački ovčar, poznat po svojoj odanosti i inteligenciji. Iako je u zlatnim godinama, njegova energija i stražarska instinkta ostali su snažni. Sa svojim prepoznatljivim crno-smeđim krznom i izraženim mišićima, Rex zrači snagom i ponosom. Iako je mirniji nego u mladosti, još uvijek voli aktivne igre i šetnje, a naročito voli obiteljsku pažnju. Njegova zaštitnička priroda i odanost svojim vlasnicima ostali su nepromijenjeni, a sa svakim danom postaje sve mudriji i nježniji.",
                "23.07.2014.",
                40.10,
                "Njemački ovčar",
                "Problemi s kukovima",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "reks", "reks2", "reks3")
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Nala",
                3,
                "Ženka",
                "Nala je energična i vesela Jorkširski terijer, stara 3 godine. Iako je mala, njena osobnost je sve samo ne to! Ova preslatka djevojčica obožava pažnju i uživa u igri, bilo da je u dvorištu ili u udobnosti svog doma. Njena vesela narav i stalna želja za ljubavlju čine je savršenim prijateljem za obitelj. Jorkširski terijeri poznati su po svojoj hrabrosti i inteligenciji, a Nala nije iznimka - brzo se uči i voli biti u centru pažnje. Njen luksuzni zlatno-smeđi kaput zahtijeva malo pažnje, ali uz to dolazi nevjerojatan izgled koji će vas oduševiti svaki put kada je pogledate. Idealna je za obitelj koja traži malog, ali energičnog ljubimca koji će im pružiti puno ljubavi i radosti.",
                "15.06.2021.",
                6.5,
                "Jorkširski terijer",
                "Ne slaže se najbolje s drugim psima",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "nala","nala2","nala3")
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Maša",
                1,
                "Ženka",
                "Maša je mješanac njemačkog špica i pomeranca, mala je i živahna kuglica energije. S svojim gustim krznom izgleda poput pravog malog medvjedića. Maša je vesela, znatiželjna i uvijek spremna za igru, a njezina razigrana osobnost čini je omiljenom u obitelji. Iako je mala, njezina hrabrost i glasno lajanje često je čine vrlo odvažnom u društvu većih pasa. Zbog svoje inteligencije brzo uči nove trikove i voli biti u centru pažnje, uživajući u svakom trenutku provedenom s vlasnicima. Njezina privrženost i veselo ponašanje čine je izvrsnim pratiteljem za aktivan i sretan život.",
                "26.11.2023.",
                4.10,
                "Njemački špic-pomeranac",
                "Uživa u društvu djece, puno energije",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "masa","masa2", "masa")
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Ref",
                4,
                "Mužjak",
                "Ref je 5-godišnji njemački oštrodlaki ptičar, lovački pas izuzetnih sposobnosti. S svojom gustim, oštrim krznom prilagođenim za rad u zahtjevnim uvjetima, Ref je pravi profesionalac u lovu, s izvrsnim instinktom za praćenje mirisa. Iako je najviše usmjeren na lovačke zadatke, njegova energična i vesela priroda također ga čini sjajnim članom obitelji. Uz visoku inteligenciju, Ref je vrlo poslušan i voljan raditi sa svojim vlasnicima, bilo da je u lovu ili uživa u obiteljskim trenucima. S godinama, stekao je i mudrost, ali i dalje zrači snagom i entuzijazmom za nove izazove.",
                "06.05.2020.",
                27.50,
                "Njemački oštrodlaki ptičar",
                "Zahtijeva duge šetnje, ima puno energije",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "ref","ref2","ref3")
        }
    }*/
}
