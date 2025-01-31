package com.example.pawpal.ui

import RezervacijaAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Narudzba
import appdatabase.RezervacijaTermina
import appdatabase.RezervacijaVeterinara
import appdatabase.Zahtjevudomljavanje
import com.example.pawpal.R
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PregledRezervacijaFragment : Fragment(), DatabaseConsumer
{
    override lateinit var database: AppDatabase
    private lateinit var recyclerViewSPA: RecyclerView
    private lateinit var recyclerViewShop : RecyclerView
    private lateinit var recyclerViewAdoption: RecyclerView
    private lateinit var recyclerViewVet : RecyclerView

    private var clientId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f10_rezervacije, container, false)

        recyclerViewSPA = view.findViewById(R.id.recyclerViewSPA)
        recyclerViewVet = view.findViewById(R.id.recyclerViewVet)
        recyclerViewShop = view.findViewById(R.id.recyclerViewShop)
        recyclerViewAdoption = view.findViewById(R.id.recyclerViewAdoption)

        recyclerViewSPA.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewSPA.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewSPA.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewSPA.layoutManager = LinearLayoutManager(requireContext())

        clientId = getCurrentUserId()

        fetchData()
        return view
    }

    private fun fetchData() {
        lifecycleScope.launch {
            try {
                // Dohvati rezervacije
                val rezervacijeSPA = getSpaReservations()
                val rezervacijeVet = getVetReservations()
                val narudzbeShop = getShopOrders()
                val zahtjeviUdomljavanje = getAdoptionRequests()

                // Reervacije SPA
                if (rezervacijeSPA.isNotEmpty()) {
                    recyclerViewSPA.visibility = View.VISIBLE
                    val adapterSPA = RezervacijaAdapter(
                        rezervacijeSPA,
                        rezervacijeVet,
                        narudzbeShop,
                        zahtjeviUdomljavanje,
                        onCancelClick = { cancelReservation(it) },
                        onInfoClick = { showReservationInfo(it) }
                    )
                    recyclerViewSPA.adapter = adapterSPA
                } else {
                    recyclerViewSPA.visibility = View.GONE
                }

                // Rezervacije veterinar
                if (rezervacijeVet.isNotEmpty()) {
                    recyclerViewVet.visibility = View.VISIBLE
                    val adapterVet = RezervacijaAdapter(
                        rezervacijeSPA,
                        rezervacijeVet,
                        narudzbeShop,
                        zahtjeviUdomljavanje,
                        onCancelClick = { cancelReservation(it) },
                        onInfoClick = { showReservationInfo(it) }
                    )
                    recyclerViewVet.adapter = adapterVet
                } else {
                    recyclerViewVet.visibility = View.GONE
                }

                // Narudzbe
                if (narudzbeShop.isNotEmpty()) {
                    recyclerViewShop.visibility = View.VISIBLE
                    val adapterShop = RezervacijaAdapter(
                        rezervacijeSPA,
                        rezervacijeVet,
                        narudzbeShop,
                        zahtjeviUdomljavanje,
                        onCancelClick = { cancelReservation(it) },
                        onInfoClick = { showReservationInfo(it) }
                    )
                    recyclerViewShop.adapter = adapterShop
                } else {
                    recyclerViewShop.visibility = View.GONE
                }

                // Zahtjevi za udomljavanje
                if (zahtjeviUdomljavanje.isNotEmpty()) {
                    recyclerViewAdoption.visibility = View.VISIBLE
                    val adapterAdoption = RezervacijaAdapter(
                        rezervacijeSPA,
                        rezervacijeVet,
                        narudzbeShop,
                        zahtjeviUdomljavanje,
                        onCancelClick = { cancelReservation(it) },
                        onInfoClick = { showReservationInfo(it) }
                    )
                    recyclerViewAdoption.adapter = adapterAdoption
                } else {
                    recyclerViewAdoption.visibility = View.GONE
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Greška pri dohvaćanju podataka: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun cancelReservation(reservation: Any) {
            Toast.makeText(context, "Rezervacija otkazana", Toast.LENGTH_SHORT).show()
        }

        private fun showReservationInfo(reservation: Any) {
            Toast.makeText(context, "Detalji rezervacije", Toast.LENGTH_SHORT).show()
        }

        private suspend fun getSpaReservations(): List<RezervacijaTermina> {
            return withContext(Dispatchers.IO) {
                database.rezervacijaTerminaUslugeQueries.dohvatiRezervacijeKorisnika(clientId).executeAsList()
            }
        }

        private suspend fun getVetReservations(): List<RezervacijaVeterinara> {
            return withContext(Dispatchers.IO) {
                database.rezervacijaVeterinaraQueries.dohvatiRezervacijeKorisnika(clientId).executeAsList()
            }
        }

        private suspend fun getShopOrders(): List<Narudzba> {
            return withContext(Dispatchers.IO) {
                database.narudzbaQueries.dohvatiNarudzbuPoIdKorisnika(clientId).executeAsList()
            }
        }

        private suspend fun getAdoptionRequests(): List<Zahtjevudomljavanje> {
            return withContext(Dispatchers.IO) {
                database.zahtjevUdomljavanjeQueries.dohvatiZahtjevePoIdKlijenta(clientId).executeAsList()
            }
        }

        private fun getCurrentUserId(): Long {
            val korisnikId = KorisnikManager.dajUlogiranogKorisnika()
            if (korisnikId == null || korisnikId == -1L) {
                Toast.makeText(requireContext(), "Korisnik nije prijavljen", Toast.LENGTH_SHORT).show()
            }
            return korisnikId ?: -1L
        }
    }
