package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class TroskoviFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var narudzbaDataSource: NarudzbaDataSource
    private lateinit var rezervacijaTerminaUslugeDataSource: RezervacijaTerminaUslugeDataSource
    private lateinit var trosakAdapter: TrosakAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f05_pregled_troskova, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        narudzbaDataSource = NarudzbaDataSourceImpl(database)
        rezervacijaTerminaUslugeDataSource = RezervacijaTerminaUslugeImpl(database)


        val troskoviRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerTroskovi)
        troskoviRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        val ukupniTroskoviTextView = view.findViewById<TextView>(R.id.totalCostsValue)



        fetchTroskovi(ukupniTroskoviTextView, troskoviRecyclerView)



    }

    private fun fetchTroskovi(ukupniTroskoviTextView: TextView, troskoviRecyclerView: RecyclerView) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()

        if (korisnikID == null) {
            prikaziPoruku("Korisnik nije prijavljen!")
            return
        }

        lifecycleScope.launch {
            try {

                val ukupniTroskoviNarudzbi = narudzbaDataSource.dohvatiUkupneTroskove(korisnikID)
                val ukupniTroskoviRezervacija =
                    rezervacijaTerminaUslugeDataSource.dohvatiUkupneTroskoveRezervacija(korisnikID)

                val ukupniTroskovi = ukupniTroskoviNarudzbi + ukupniTroskoviRezervacija
                ukupniTroskoviTextView.text = String.format("€%.2f", ukupniTroskovi)


                val narudzbe = narudzbaDataSource.dohvatiSveNarudzbeKorisnika(korisnikID)
                val rezervacije = rezervacijaTerminaUslugeDataSource.dohvatiRezervacijeKorisnikaSDetaljima(korisnikID)

                val troskoviList = mutableListOf<Pair<String, Triple<Double, String, String>>>()


                narudzbe.forEach { narudzba ->
                    troskoviList.add(
                        "Webshop" to Triple(narudzba.ukupnaCijena, narudzba.datum, "Webshop")
                    )
                }


                troskoviList.addAll(rezervacije)


                if (troskoviList.isEmpty()) {
                    prikaziPoruku("Nema evidentiranih troškova.")
                }


                trosakAdapter = TrosakAdapter(troskoviList) { naziv ->
                    prikaziPoruku("Kliknuli ste na: $naziv")
                }
                troskoviRecyclerView.adapter = trosakAdapter

            } catch (e: Exception) {
                prikaziPoruku("Greška pri dohvaćanju troškova: ${e.message}")
            }
        }
    }


    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show()
    }
}
