package com.example.pawpal.ui

import NarudzbePrikazAdapter
import SpaPrikazRezervacijaAdapter
import VetPrikazRezervacijaAdapter
import ZahtjevUdomljavanjeAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    private lateinit var labelSPA: TextView
    private lateinit var labelShop: TextView
    private lateinit var labelAdoption: TextView
    private lateinit var labelVet: TextView

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

        labelSPA = view.findViewById(R.id.spaLabel)
        labelShop = view.findViewById(R.id.shopLabel)
        labelAdoption = view.findViewById(R.id.adoptionLabel)
        labelVet = view.findViewById(R.id.vetLabel)

        recyclerViewSPA.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewVet.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewShop.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAdoption.layoutManager = LinearLayoutManager(requireContext())

        clientId = getCurrentUserId()

        fetchData()
        return view
    }

    private fun fetchData() {
        lifecycleScope.launch {
            try {
                val rezervacijeSPA = getSpaReservations()
                val rezervacijeVet = getVetReservations()
                val narudzbeShop = getShopOrders()
                val zahtjeviUdomljavanje = getAdoptionRequests()

                if (rezervacijeSPA.isNotEmpty()) {
                    labelSPA.visibility = View.VISIBLE
                    recyclerViewSPA.visibility = View.VISIBLE
                    val adapterSPA = SpaPrikazRezervacijaAdapter(rezervacijeSPA, onCancelClick = { cancelReservation(it) }, database, lifecycleScope = lifecycleScope)
                    recyclerViewSPA.adapter = adapterSPA
                } else {
                    recyclerViewSPA.visibility = View.GONE
                    labelSPA.visibility = View.GONE
                }

                if (rezervacijeVet.isNotEmpty()) {
                    labelVet.visibility = View.VISIBLE
                    recyclerViewVet.visibility = View.VISIBLE
                    val adapterVet = VetPrikazRezervacijaAdapter(rezervacijeVet, onCancelClick = { cancelReservation(it) })
                    recyclerViewVet.adapter = adapterVet
                } else {
                    recyclerViewVet.visibility = View.GONE
                    labelVet.visibility = View.GONE
                }

                if (narudzbeShop.isNotEmpty()) {
                    labelShop.visibility = View.VISIBLE
                    recyclerViewShop.visibility = View.VISIBLE
                    val adapterShop = NarudzbePrikazAdapter(narudzbeShop, onInfoClick = { showReservationInfo(it) })
                    recyclerViewShop.adapter = adapterShop
                } else {
                    recyclerViewShop.visibility = View.GONE
                    labelShop.visibility = View.GONE
                }

                if (zahtjeviUdomljavanje.isNotEmpty()) {
                    labelAdoption.visibility = View.VISIBLE
                    recyclerViewAdoption.visibility = View.VISIBLE
                    val adapterAdoption = ZahtjevUdomljavanjeAdapter(zahtjeviUdomljavanje, onCancelClick = { cancelReservation(it) })
                    recyclerViewAdoption.adapter = adapterAdoption
                } else {
                    recyclerViewAdoption.visibility = View.GONE
                    labelAdoption.visibility = View.GONE
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
