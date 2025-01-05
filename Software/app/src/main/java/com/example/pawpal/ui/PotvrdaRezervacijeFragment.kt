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
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PotvrdaRezervacijeFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private var veterinarID: Long = 0
    private var uslugaID: Long = 0

    companion object{
        const val ARG_VETERINAR_ID = "veterinarID"
        const val ARG_USLUGA_ID = "uslugaID"

        fun newInstance(veterinarID: Long, uslugaID: Long): PotvrdaRezervacijeFragment{
            val fragment = PotvrdaRezervacijeFragment()
            val args = Bundle()
            args.putLong(ARG_VETERINAR_ID, veterinarID)
            args.putLong(ARG_USLUGA_ID, uslugaID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            veterinarID = it.getLong(ARG_VETERINAR_ID)
            uslugaID = it.getLong(ARG_USLUGA_ID)
        }


    }

    private lateinit var datumTextView: TextView
    private lateinit var vrijemeTextView: TextView
    private lateinit var uslugaTextView: TextView
    private lateinit var opisTextView: TextView
    private lateinit var imeVetPotvrda: TextView
    private lateinit var titulaVetPotvrda: TextView
    private lateinit var potvrdiButton: Button
    private lateinit var odustaniButton: Button

    private lateinit var rezervacijeDataSource: RezervacijaVeterinaraImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_potvrda_rezervacije, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val driver = AndroidSqliteDriver(AppDatabase.Schema, requireContext(), "appdatabase.db" )
        database = AppDatabase(driver)


        datumTextView = view.findViewById(R.id.datumTextView)
        vrijemeTextView = view.findViewById(R.id.vrijemeTextView)
        uslugaTextView = view.findViewById(R.id.uslugaTextView)
        opisTextView = view.findViewById(R.id.opisTextView)
        imeVetPotvrda = view.findViewById(R.id.imeVetPotvrda)
        titulaVetPotvrda = view.findViewById(R.id.titulaVetPotvrda)
        potvrdiButton = view.findViewById(R.id.potvrdiGumb)
        odustaniButton = view.findViewById(R.id.odustaniGumb)


        val datum = arguments?.getString("odabrani_datum") ?: "Nije odabran datum"
        val vrijeme = arguments?.getString("odabrano_vrijeme") ?: "Nije odabrano vrijeme"
        val opis = arguments?.getString("uneseni_opis") ?: "Nije unesen opis"

        val korisnikId = getCurrentUserId()

        if (veterinarID == 0L) {
            Log.e("PotvrdaRezervacije", "VeterinarID nije pronađen!")
            Toast.makeText(context, "Greška: Veterinar nije odabran", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            lifecycleScope.launch {
                val veterinar = database.veterinarQueries.dohvatiVeterinaraID(veterinarID).executeAsOne()
                imeVetPotvrda.text = veterinar.imePrezime
                titulaVetPotvrda.text = veterinar.specijalizacija

                val usluga = database.vrstaUslugeQueries.dohvatiuslugupoID(uslugaID).executeAsOne()
                uslugaTextView.text = "${usluga.nazivUsluge} - ${usluga.cijena}"

            }

            }
        if (veterinarID == null) {
            Log.e("PotvrdaRezervacije", "Veterinar nije pronađen za ID: $veterinarID")
            return
        }

        rezervacijeDataSource = RezervacijaVeterinaraImpl(database)



        datumTextView.text = datum
        vrijemeTextView.text = vrijeme
        opisTextView.text = opis

        potvrdiButton.setOnClickListener {
            lifecycleScope.launch {
                database.rezervacijaVeterinaraQueries.dodajRezervaciju(
                    korisnikID = korisnikId,
                    veterinarID = veterinarID,
                    uslugaID = uslugaID,
                    datum = datum,
                    vrijeme = vrijeme,
                    dodatniOpis = opis
                )
                Toast.makeText(context, "Zahtjev uspješno poslan!", Toast.LENGTH_SHORT).show()
                navigateToMainFragment()
            }
        }

        odustaniButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
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