package com.example.pawpal.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.data.impl.RezervacijaVeterinaraImpl
import com.example.pawpal.data.impl.VeterinarImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PotvrdaRezervacijeFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var datumTextView: TextView
    private lateinit var vrijemeTextView: TextView
    private lateinit var uslugaTextView: TextView
    private lateinit var opisTextView: TextView
    private lateinit var imeVeterinaraTextView: TextView
    private lateinit var potvrdiButton: Button
    private lateinit var odustaniButton: Button

    private lateinit var rezervacijeDataSource: RezervacijaVeterinaraImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_potvrda_rezervacije, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val driver = AndroidSqliteDriver(AppDatabase.Schema, requireContext(), "appdatabase.db")
        database = AppDatabase(driver)

        val veterinarID = arguments?.getLong("veterinar_id")
        if (veterinarID == null) {
            Toast.makeText(requireContext(), "Neispravan ID veterinara!", Toast.LENGTH_SHORT).show()
            return
        }

        val veterinarImpl = VeterinarImpl(database)
        lifecycleScope.launch {
            try {
                val veterinar = veterinarImpl.dohvatiVeterinaraPoID(veterinarID)

                if (veterinar != null) {
                    imeVeterinaraTextView.text = veterinar.imePrezime
                } else {
                    imeVeterinaraTextView.text = "Veterinar nije pronađen"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Greška pri dohvaćanju imena veterinara", Toast.LENGTH_SHORT).show()
            }
        }

        rezervacijeDataSource = RezervacijaVeterinaraImpl(database)

        datumTextView = view.findViewById(R.id.datumTextView)
        vrijemeTextView = view.findViewById(R.id.vrijemeTextView)
        uslugaTextView = view.findViewById(R.id.uslugaTextView)
        opisTextView = view.findViewById(R.id.opisTextView)
        imeVeterinaraTextView = view.findViewById(R.id.imeVeterinaraTextView)
        potvrdiButton = view.findViewById(R.id.potvrdiGumb)
        odustaniButton = view.findViewById(R.id.odustaniGumb)

        val datum = arguments?.getString("odabrani_datum") ?: "Nije odabran datum"
        val vrijeme = arguments?.getString("odabrano_vrijeme") ?: "Nije odabrano vrijeme"
        val usluga = arguments?.getString("odabrana_usluga") ?: "Nije odabrana usluga"
        val opis = arguments?.getString("uneseni_opis") ?: "Nije unesen opis"
        val imeVeterinara = arguments?.getString("ime_veterinara") ?: "Nije odabrano ime veterinara"



        datumTextView.text = datum
        vrijemeTextView.text = vrijeme
        uslugaTextView.text = usluga
        opisTextView.text = opis
        imeVeterinaraTextView.text = imeVeterinara

        potvrdiButton.setOnClickListener {
            lifecycleScope.launch {
                saveReservationToDatabase(datum, vrijeme, usluga, opis, veterinarID)
            }
        }

        odustaniButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private suspend fun saveReservationToDatabase(
        datum: String,
        vrijeme: String,
        usluga: String,
        opis: String,
        veterinarID: Long
    ) {
        Log.d("PotvrdaRezervacije", "Datum: $datum, Vrijeme: $vrijeme, Usluga: $usluga, Opis: $opis, Veterinar ID: $veterinarID")

        try {
            val korisnikId = getCurrentUserId().toString()

            val extractedTime = vrijeme.replace("Odabrano vrijeme:", "").trim()
            val extractDate = datum.replace("Odabrani datum:", "").trim()
            val datumLong = convertDateToTimestamp(extractDate)
            val vrijemeLong = convertTimeToTimestamp(extractedTime)


            val veterinarId = veterinarID
            if (veterinarId == null) {
                throw IllegalArgumentException("Neispravan ID veterinara") // Handle invalid ID
            }

            rezervacijeDataSource.dodajRezervaciju(datumLong, vrijemeLong, usluga, opis, veterinarId, korisnikId)
            Toast.makeText(requireContext(), "Rezervacija potvrđena!", Toast.LENGTH_SHORT).show()
            navigateToMainFragment()
        } catch (e: Exception) {
            Log.e("RezervacijaError", "Greška pri spremanju rezervacije: ${e.message}", e)
            Toast.makeText(requireContext(), "Greška pri spremanju rezervacije!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun convertDateToTimestamp(datum: String): Long {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.parse(datum)?.time ?: throw IllegalArgumentException("Neispravan format datuma")
    }

    private fun convertTimeToTimestamp(vrijeme: String): Long {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.parse(vrijeme)?.time ?: throw IllegalArgumentException("Neispravan format vremena")
    }


    private fun getCurrentUserId(): Long {
        val korisnikId = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikId == null || korisnikId == -1L) {
            Toast.makeText(requireContext(), "Korisnik nije prijavljen", Toast.LENGTH_SHORT).show()
        }
        return korisnikId ?: -1L
    }

    private fun navigateToMainFragment() {
        val mainFragment = OdabirVeterinaraFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, mainFragment)
            .addToBackStack(null)
            .commit()
    }
}