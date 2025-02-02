package com.example.pawpal.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.datasource.*
import com.example.pawpal.data.impl.*
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PregledAktivnostiFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var narudzbaDataSource: NarudzbaDataSource
    private lateinit var rezervacijaTerminaUslugeDataSource: RezervacijaTerminaUslugeDataSource
    private lateinit var rezervacijaVeterinaraDataSource: RezervacijaVeterinaraDataSource
    private lateinit var wishlistDataSource: WishlistDataSource
    private lateinit var zahtjevUdomljavanjeDataSource: ZahtjevUdomljavanjeDataSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f05_pregled_aktivnosti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        narudzbaDataSource = NarudzbaDataSourceImpl(database)
        rezervacijaTerminaUslugeDataSource = RezervacijaTerminaUslugeImpl(database)
        rezervacijaVeterinaraDataSource = RezervacijaVeterinaraImpl(database)
        wishlistDataSource = WishlistDataSourceImpl(database)
        zahtjevUdomljavanjeDataSource = ZahtjevUdomljavanjeDataSourceImpl(database)

        fetchPodaciOKorisniku(view)
    }

    private fun fetchPodaciOKorisniku(view: View) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()

        if (korisnikID == null) {
            prikaziPoruku("Korisnik nije prijavljen!")
            return
        }

        lifecycleScope.launch {
            try {
                val brojNarudzbi = narudzbaDataSource.dohvatiBrojNarudzbiKorisnika(korisnikID).toInt()
                val brojRezervacijaUsluga = rezervacijaTerminaUslugeDataSource.dohvatiBrojRezervacijaUslugaKorisnika(korisnikID).toInt()
                val brojRezervacijaVeterinara = rezervacijaVeterinaraDataSource.dohvatiBrojRezervacijaVeterinaraKorisnika(korisnikID).toInt()
                val brojWishlist = wishlistDataSource.dohvatiBrojWishlistKorisnika(korisnikID).toInt()
                val brojZahtjeva = zahtjevUdomljavanjeDataSource.dohvatiBrojZahtjevaKorisnika(korisnikID).toInt()

                view.findViewById<TextView>(R.id.brojNarudzbiText).text = "Broj narudžbi: $brojNarudzbi"
                view.findViewById<TextView>(R.id.brojRezervacijaUslugaText).text = "Broj rezervacija usluga: $brojRezervacijaUsluga"
                view.findViewById<TextView>(R.id.brojRezervacijaVeterinaraText).text = "Broj rezervacija veterinara: $brojRezervacijaVeterinara"
                view.findViewById<TextView>(R.id.brojWishlistText).text = "Broj upisa u škole: $brojWishlist"
                view.findViewById<TextView>(R.id.brojZahtjevaUdomljavanjeText).text = "Broj zahtjeva za udomljavanje: $brojZahtjeva"

                setupPieChart(view, brojNarudzbi, brojRezervacijaUsluga, brojRezervacijaVeterinara, brojWishlist, brojZahtjeva)

            } catch (e: Exception) {
                prikaziPoruku("Greška pri dohvaćanju podataka: ${e.message}")
            }
        }
    }



    private fun setupPieChart(view: View, narudzbe: Int, usluge: Int, veterinari: Int, wishlist: Int, zahtjevi: Int) {
        val pieChart = view.findViewById<PieChart>(R.id.grafikonAktivnosti)

        val podaci = mutableListOf<PieEntry>()
        val boje = mutableListOf<Int>()


        if (narudzbe > 0) {
            podaci.add(PieEntry(narudzbe.toFloat(), "Webshop"))
            boje.add(Color.parseColor("#A67B5B"))
        }

        if (usluge > 0) {
            podaci.add(PieEntry(usluge.toFloat(), "Pseći spa"))
            boje.add(Color.parseColor("#8B5E3B"))
        }

        if (veterinari > 0) {
            podaci.add(PieEntry(veterinari.toFloat(), "Veterinar"))
            boje.add(Color.parseColor("#deb46a"))
        }

        if (wishlist > 0) {
            podaci.add(PieEntry(wishlist.toFloat(), "Škola"))
            boje.add(Color.parseColor("#cba95a"))
        }

        if (zahtjevi > 0) {
            podaci.add(PieEntry(zahtjevi.toFloat(), "Udomljavanje"))
            boje.add(Color.parseColor("#C19A6B"))
        }

        val dataSet = PieDataSet(podaci, "").apply {
            colors = boje
            valueTextSize = 16f
            valueTextColor = Color.WHITE
        }

        val pieData = PieData(dataSet)

        pieChart.setUsePercentValues(true)
        pieData.setValueFormatter(PercentFormatter(pieChart))

        dataSet.setDrawValues(true)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.setUsePercentValues(true)
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.holeRadius = 40f
        pieChart.transparentCircleRadius = 50f
        pieChart.animateY(1000)


        pieChart.legend.isEnabled = true
        pieChart.invalidate()
    }


    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show()
    }
}
