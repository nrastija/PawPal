package com.example.pawpal.main

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
import com.example.pawpal.ui.PregledWishlisteFragment
import com.example.pawpal.ui.ProfilKorisnikaFragment
import com.example.pawpal.ui.OdabirPrijaveIliPregledaPsaFragment
import com.example.pawpal.ui.OdabirVeterinaraFragment
import com.example.pawpal.ui.PromoPonudaFragment
import com.example.pawpal.ui.SPAFragment
import com.example.pawpal.ui.ShopFragment
import com.example.pawpal.ui.SkolaFragment
import com.example.pawpal.ui.UdomljavanjeFragment
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

        database = (application as PawPalApplication).database
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

        resetShopData()
        resetSkolaData()
        resetAdoptionData()
        //resetSPAData()
        //resetPromoData()
        resetVeterinarianData()
        resetUslugaData()
    }

    private fun setupHamburgerMenu(drawerLayout: DrawerLayout, toolbar: Toolbar, navView: NavigationView) {
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    setImagesVisibility(View.VISIBLE)
                    drawerLayout.closeDrawers()
                }
                R.id.nav_profile -> navigateToFragment(ProfilKorisnikaFragment())
                R.id.nav_spa -> navigateToFragment(SPAFragment())
                R.id.nav_promo ->navigateToFragment(PromoPonudaFragment())
                R.id.nav_shop -> navigateToFragment(ShopFragment())
                R.id.nav_school -> navigateToFragment(SkolaFragment())
                R.id.nav_adoption -> navigateToFragment(UdomljavanjeFragment())
                R.id.nav_lost_dogs -> navigateToFragment(OdabirPrijaveIliPregledaPsaFragment())
                R.id.nav_veterinar -> navigateToFragment(OdabirVeterinaraFragment())
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

            }
            drawerLayout.closeDrawers()
            true
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
        val kategorijaQueries = database.kategorijaQueries

        proizvodQueries.transaction {
            proizvodQueries.deleteAllProizvods()
        }
        kategorijaQueries.transaction {
            kategorijaQueries.deleteAllKategorijas()
        }


        kategorijaQueries.transaction {
            kategorijaQueries.insertKategorija(1, "Zdravlje")
            kategorijaQueries.insertKategorija(2, "Hrana")
            kategorijaQueries.insertKategorija(3, "Higijena")
            kategorijaQueries.insertKategorija(4, "Ostalo")
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

    private fun resetAdoptionData(){
        val queriesPasUdomljavanje = database.pasUdomljavanjeQueries
        val queriesZahtjevUdomljavanje = database.zahtjevUdomljavanjeQueries

        queriesZahtjevUdomljavanje.transaction {
            queriesZahtjevUdomljavanje.deleteAllZahtjevUdomljavanje()
        }

        queriesPasUdomljavanje.transaction {
            queriesPasUdomljavanje.deleteAllPasUdomljavanje() }

        queriesPasUdomljavanje.transaction {
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Nara",
                9,
                "Ženka",
                "Ja sam Nara, pas neodoljivog šarma i zaigrane naravi. Obožavam provoditi vrijeme s ljudima i uvijek sam spremna za maženje, u čemu istinski uživam. Moja najveća strast je pokazivanje ljubavi kroz veselo i neumorno lizanje, čime osvajam srca svih oko sebe. Osim toga, prava sam gurmanica, uvijek u potrazi za ukusnim zalogajem i nikada ne propuštam priliku za svoju omiljenu poslasticu. Moja energija i privrženost čine me savršenim prijateljem koji unosi radost u svaki trenutak.",
                "25.03.2015.",
                8.8,
                "Shitzu-Maltezer",
                "Ne zahtijeva puno šetnji",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "nara2",
                "nara3",
                "nara"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Zumi",
                1,
                "Ženka",
                "Ja sam Zumi, pas vrlo druželjubive naravi. Obožavam ljude, ljudsku prisutnost i ne mogu bez njih! Također sam jako znatiželjna, volim šnjofati svakakve skrivene kutke livada i dvorišta te nikada ne propuštam igru s drugim psima. Volim djecu kao i ostale pse, a lako se prilagodim na nove okoline. Jako sam hrabra i obožavam vodu, što je možda i pomalo čudno za psa. Nakon dugog dana punog igre, trčanja i istraživanja, najdraže mi je odmoriti na kauču uz doticaj svojih najmilijih. Umiljata sam i dobra, volim se maziti, a ponekad se malo previše zaigram, ali ne možete mi zamjeriti jer sam još mala! ",
                "10.05.2024.",
                2.8,
                "Mini Pudla",
                "Energična i slatka, uvijek zaigrana",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "zumi3", "zumi2", "zumi"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Kira",
                12,
                "Ženka",
                "Ja sam Kira, labradorica čije mirne i mudre oči odražavaju godine ispunjene ljubavlju i odanošću. Iako sam u zrelim godinama, i dalje zadržavam nježnu i prijateljsku narav tipičnu za labradore. Volim mirne šetnje i opušteno vrijeme provedeno u društvu svoje obitelji. Kroz godine sam postala vjeran i pouzdan pratitelj, uvijek spremna pružiti utjehu i toplinu svojim voljenima. Moja ljubav prema ljudima i umirujuća prisutnost čine me nezamjenjivim članom vaše obitelji.",
                "17.05.2012.",
                35.00,
                "Labrador",
                "Uživa u pažnji djece, alergična na kikiriki",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "kira3", "kira", "kira2"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Striček Viršl",
                5,
                "Mužjak",
                "Ja sam Striček Viršl, ponosni jazavčar sa srcem većim od mene! Moj izduženi izgled i kratke šapice osvajaju svakoga ko me sretne. Iako sam malen, nosim u sebi ogromnu energiju i ljubav prema ljudima. Volim istraživati svaki kutak tokom naših šetnji, a moj nos je uvijek u potrazi za najzanimljivijim mirisima. Nakon aktivnog dana, uživam u uvijanju u mekanu dekicu dok sanjam o novim avanturama. Uvek sam spreman za igru, ali i za nježne trenutke sa svojim ljudima. Ako tražiš vernog, veselog i neodoljivog prijatelja, tu sam – Striček Viršl!",
                "02.03.2018.",
                5.80,
                "Dugodlaki jazavčar",
                "Obožava igranje i istraživanje",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "wirsl1", "wirsl2", "wirsl3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Molly",
                3,
                "Ženka",
                "Ja sam Molly, preslatka maltezerica s mekanim, bijelim krznom koje podsjeća na oblak! Iako sam mala rastom, u meni se krije ogromno srce puno ljubavi i veselja. Obožavam provoditi vreme sa svojom ljudskom porodicom, bilo da se mazimo na kauču ili uživamo u kratkim šetnjama. Moje velike, sjajne oči uvijek prate svaki tvoj pokret, a moj veseli repić neumorno pokazuje koliko te volim. Ako tražiš nježnog i odanog prijatelja koji će ti uljepšati svaki dan, ja sam spremna da postanem dio tvoje obitelji!",
                "10.07.2020.",
                4.50,
                "Maltezer",
                "Uživanje u maženju i društvu, ne slaže se s drugim psima",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "molly3", "molly2", "molly"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Rex",
                10,
                "Mužjak",
                "Ja sam Rex, njemački ovčar poznat po svojoj odanosti i inteligenciji. Iako sam u zlatnim godinama, moja energija i stražarski instinkt još uvijek su snažni. Sa svojim prepoznatljivim crno-smeđim krznom i izraženim mišićima, zračim snagom i ponosom. Iako sam mirniji nego u mladosti, i dalje uživam u aktivnim igrama i šetnjama, a posebno volim pažnju svoje obitelji. Moja zaštitnička priroda i odanost prema onima koje volim nikada se nisu promijenile, a sa svakim danom postajem sve mudriji i nježniji.",
                "23.07.2014.",
                40.10,
                "Njemački ovčar",
                "Problemi s kukovima",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "reks2", "reks", "reks3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Nala",
                3,
                "Ženka",
                "Ja sam Nala, energična i vesela terijerka stara 3 godine. Iako sam mala, moja osobnost je sve samo ne to! Obožavam pažnju i uživam u igri, bilo da sam u dvorištu ili u udobnosti svog doma. Moja vesela narav i stalna želja za ljubavlju čine me savršenim prijateljem za obitelj. Kao pravi Jorkširski terijer, hrabra sam i inteligentna, brzo učim i volim biti u centru pažnje. Moj luksuzni zlatno-smeđi kaput traži malo pažnje, ali uz to dolazi nevjerojatan izgled koji će vas oduševiti svaki put kada me pogledate. Idealna sam za obitelj koja traži malog, ali energičnog ljubimca koji će im pružiti puno ljubavi i radosti.",
                "15.06.2021.",
                6.5,
                "Jorkširski terijer",
                "Ne slaže se najbolje s drugim psima",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "nala2", "nala", "nala3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Maša",
                1,
                "Ženka",
                "Ja sam Maša, mješanac njemačkog špica i pomeranca, mala sam i živahna kuglica energije. S mojim gustim krznom izgledam poput pravog malog medvjedića. Vesela sam, znatiželjna i uvijek spremna za igru, a moja razigrana osobnost čini me omiljenom u obitelji. Iako sam mala, moja hrabrost i glasno lajanje često me čine vrlo odvažnom u društvu većih pasa. Zbog svoje inteligencije brzo učim nove trikove i volim biti u centru pažnje, uživajući u svakom trenutku provedenom s vlasnicima. Moja privrženost i veselo ponašanje čine me izvrsnim pratiteljem za aktivan i sretan život.",
                "26.11.2023.",
                4.10,
                "Njemački špic-pomeranac",
                "Uživa u društvu djece, puno energije",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "masa2", "masa", "masa3"
            )
            queriesPasUdomljavanje.dodajPasUdomljavanje(
                "Ref",
                4,
                "Mužjak",
                "Ja sam Ref, njemački oštrodlaki ptičar, lovački pas izuzetnih sposobnosti. S mojim gustim, oštrim krznom prilagođenim za rad u zahtjevnim uvjetima, pravi sam profesionalac u lovu, s izvrsnim instinktom za praćenje mirisa. Iako sam najviše usmjeren na lovačke zadatke, moja energična i vesela priroda također me čini sjajnim članom obitelji. Uz visoku inteligenciju, vrlo sam poslušan i voljan raditi sa svojim vlasnicima, bilo da sam u lovu ili uživam u obiteljskim trenucima. S godinama, stekao sam i mudrost, ali i dalje zračim snagom i entuzijazmom za nove izazove.",
                "06.05.2020.",
                27.50,
                "Njemački oštrodlaki ptičar",
                "Zahtijeva duge šetnje, ima puno energije",
                "Bjesnoća, Štenećak, Parvovirus, Hepatitis, Parainfluenza, Leptospiroza",
                "ref3", "ref", "ref2"
            )
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            setImagesVisibility(View.VISIBLE)
        } else {
            super.onBackPressed()
        }
    }

}
