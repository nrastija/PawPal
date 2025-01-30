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
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PregledRezervacijaFragment : Fragment(), DatabaseConsumer
{
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f10_rezervacije, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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

                // Postavi adapter za RecyclerView
                val adapter = RezervacijaAdapter(
                    rezervacijeSPA,
                    rezervacijeVet,
                    narudzbeShop,
                    zahtjeviUdomljavanje,
                    onCancelClick = { reservation ->
                        // Implementiraj otkazivanje rezervacije
                        cancelReservation(reservation)
                    },
                    onInfoClick = { reservation ->
                        // Implementiraj funkcionalnost za info o rezervaciji (ako je potrebno)
                        showReservationInfo(reservation)
                    }
                )

                recyclerView.adapter = adapter
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Greška pri dohvaćanju podataka", Toast.LENGTH_SHORT)
                        .show()
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
                database.rezervacijaTerminaUslugeQueries.dohvatiSveRezervacije().executeAsList()
            }
        }

        private suspend fun getVetReservations(): List<RezervacijaVeterinara> {
            return withContext(Dispatchers.IO) {
                database.rezervacijaVeterinaraQueries.dohvatiSveRezervacije().executeAsList()
            }
        }

        private suspend fun getShopOrders(): List<Narudzba> {
            return withContext(Dispatchers.IO) {
                database.narudzbaQueries.dohvatiNarudzbu(1).executeAsList()
            }
        }

        private suspend fun getAdoptionRequests(): List<Zahtjevudomljavanje> {
            return withContext(Dispatchers.IO) {
                database.zahtjevUdomljavanjeQueries.dohvatiSveZahtjeve().executeAsList()
            }
        }
    }
