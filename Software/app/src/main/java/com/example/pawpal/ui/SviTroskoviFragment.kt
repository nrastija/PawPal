package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.TrosakAdapter
import com.example.pawpal.data.datasource.NarudzbaDataSource
import com.example.pawpal.data.datasource.RezervacijaTerminaUslugeDataSource
import com.example.pawpal.data.impl.NarudzbaDataSourceImpl
import com.example.pawpal.data.impl.RezervacijaTerminaUslugeImpl
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class SviTroskoviFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var narudzbaDataSource: NarudzbaDataSource
    private lateinit var rezervacijaDataSource: RezervacijaTerminaUslugeDataSource
    private lateinit var trosakWebshopAdapter: TrosakAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f05_pregled_troskova, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        narudzbaDataSource = NarudzbaDataSourceImpl(database)
        rezervacijaDataSource = RezervacijaTerminaUslugeImpl(database)

        val troskoviRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerTroskovi)
        troskoviRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val ukupniTroskoviTextView = view.findViewById<TextView>(R.id.totalCostsValue)

        view.findViewById<TextView>(R.id.ukupniTroskovi).text = "Ukupni troškovi centra"
        view.findViewById<TextView>(R.id.povijestTroskova).text = "Povijest troškova webshopa"

        fetchSviTroskovi(ukupniTroskoviTextView, troskoviRecyclerView)
    }

    private fun fetchSviTroskovi(ukupniTroskoviTextView: TextView, troskoviRecyclerView: RecyclerView) {
        lifecycleScope.launch {
            try {

                val ukupniTroskoviNarudzbi = narudzbaDataSource.dohvatiUkupneTroskoveSvihKorisnika()
                val ukupniTroskoviRezervacija = rezervacijaDataSource.dohvatiUkupneTroskoveRezervacijaSvihKorisnika()

                val ukupniTroskovi = ukupniTroskoviNarudzbi + ukupniTroskoviRezervacija
                ukupniTroskoviTextView.text = String.format("€%.2f", ukupniTroskovi)


                val webshopTroskovi = narudzbaDataSource.dohvatiSveNarudzbe()
                    .map { narudzba ->
                        "Webshop" to Triple(narudzba.ukupnaCijena, narudzba.datum, "Webshop")
                    }

                trosakWebshopAdapter = TrosakAdapter(webshopTroskovi) { naziv ->
                    prikaziPoruku("Kliknuli ste na: $naziv")
                }
                troskoviRecyclerView.adapter = trosakWebshopAdapter

            } catch (e: Exception) {
                prikaziPoruku("Greška pri dohvaćanju troškova: ${e.message}")
            }
        }
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show()
    }
}
