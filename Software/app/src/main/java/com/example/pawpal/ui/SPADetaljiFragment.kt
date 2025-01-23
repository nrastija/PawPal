
package com.example.pawpal.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.util.Calendar

class SPADetaljiFragment : Fragment() {
    private lateinit var database: AppDatabase
    private var uslugaID: Long = 0
    private lateinit var datumInput: EditText
    private lateinit var vrijemeInput: EditText
    private lateinit var napomeneInput: EditText

    companion object {
        const val ARG_USLUGA_ID = "uslugaID"
        fun newInstance(uslugaID: Long) = SPADetaljiFragment().apply {
            arguments = Bundle().apply { putLong(ARG_USLUGA_ID, uslugaID) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uslugaID = arguments?.getLong(ARG_USLUGA_ID) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.f03_usluga_detalji, container, false)
        database = (requireActivity().application as PawPalApplication).database
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        ucitajUslugaDetalje(view)

        lifecycleScope.launch {
            val trenutniKorisnikID = KorisnikManager.dajUlogiranogKorisnika()!!
            val trenutniKorisnik = database.korisnikQueries.dajKorisnikaPoID(trenutniKorisnikID).executeAsOne()

            if (trenutniKorisnik.tip_korisnika == 2L) {
                view.findViewById<FrameLayout>(R.id.RezervacijaFrame).visibility = View.GONE

                view.findViewById<Button>(R.id.btnAzurirajUslugu).apply {
                    visibility = View.VISIBLE
                    setOnClickListener { otvoriAzuriranjeUsluge() }
                }

                view.findViewById<Button>(R.id.btnObrisiUslugu).apply {
                    visibility = View.VISIBLE
                    setOnClickListener { obrisiUslugu() }
                }
            }
        }
    }

    private fun setupViews(view: View) {
        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        datumInput = view.findViewById(R.id.datumRezervacijaUsluga)
        vrijemeInput = view.findViewById(R.id.vrijemeRezervacijaUsluga)
        napomeneInput = view.findViewById(R.id.napomeneRezervacijaUsluga)
        datumInput.setOnClickListener { showDatePicker() }
        vrijemeInput.setOnClickListener { showTimePicker() }

        view.findViewById<Button>(R.id.btnRezervirajTermin).apply {
            setOnClickListener { spremiRezervaciju() }
        }
    }

    private fun ucitajUslugaDetalje(view: View) {
        lifecycleScope.launch {
            val usluga = database.uslugaQueries.dohvatiUsluguPoID(uslugaID).executeAsOne()
            view.findViewById<TextView>(R.id.UslugaNaziv).text = usluga.naziv
            view.findViewById<TextView>(R.id.OpisUsluge).text = usluga.opis ?: "Nema opisa"
            view.findViewById<TextView>(R.id.UslugaDetaljiCijena).text = "${usluga.cijena} €"
            view.findViewById<TextView>(R.id.TrajanjeUsluge).text = "${usluga.trajanje} min"
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day -> datumInput.setText("$day/${month + 1}/$year") },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hour, minute -> vrijemeInput.setText(String.format("%02d:%02d", hour, minute)) },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun spremiRezervaciju() {
        val datum: String = datumInput.text.toString()
        val vrijeme: String = vrijemeInput.text.toString()

        if (datum.isEmpty() || vrijeme.isEmpty()) {
            Toast.makeText(context, "Molimo unesite datum i vrijeme", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val trenutniKorisnikID: Long = KorisnikManager.dajUlogiranogKorisnika()!!
                val trenutniKorisnik = database.korisnikQueries.dajKorisnikaPoID(trenutniKorisnikID).executeAsOne()

                database.rezervacijaTerminaUslugeQueries.dodajRezervaciju(
                    korisnikID = trenutniKorisnikID,
                    uslugaID = uslugaID,
                    datum = datum,
                    vrijeme = vrijeme,
                    napomene = napomeneInput.text.toString()
                )
                Toast.makeText(context, "Rezervacija uspješno spremljena za korisnika: ${trenutniKorisnik.korime}", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Greška prilikom spremanja: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obrisiUslugu() {
        lifecycleScope.launch {
            try {
                database.uslugaQueries.obrisiUsluguPoID(uslugaID)
                Toast.makeText(context, "Usluga uspješno obrisana!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Greška prilikom brisanja: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun otvoriAzuriranjeUsluge() {
        val fragment = AzurirajUsluguFragment.newInstance(uslugaID)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
