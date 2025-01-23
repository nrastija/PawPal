
package com.example.pawpal.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.util.Calendar

class SPADetaljiFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private var uslugaID: Long = 0
    private lateinit var datumInput: EditText
    private lateinit var vrijemeInput: EditText
    private lateinit var napomeneInput: EditText

    companion object {
        const val ARG_USLUGA_ID = "uslugaID"
        fun newInstance(uslugaID: Long): SPADetaljiFragment {
            val fragment = SPADetaljiFragment()
            val args = Bundle()
            args.putLong(ARG_USLUGA_ID, uslugaID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uslugaID = it.getLong(ARG_USLUGA_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f03_usluga_detalji, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val uslugaNaziv: TextView = view.findViewById(R.id.UslugaNaziv)
        val opisUsluge: TextView = view.findViewById(R.id.OpisUsluge)
        val cijenaUsluge: TextView = view.findViewById(R.id.UslugaDetaljiCijena)
        val trajanjeUsluge: TextView = view.findViewById(R.id.TrajanjeUsluge)

        datumInput = view.findViewById(R.id.datumInput)
        vrijemeInput = view.findViewById(R.id.vrijemeInput)
        napomeneInput = view.findViewById(R.id.napomeneInput)

        datumInput.setOnClickListener { showDatePicker() }
        vrijemeInput.setOnClickListener { showTimePicker() }

        view.findViewById<Button>(R.id.UsvojiMe).apply {
            text = "Rezerviraj termin"
            setOnClickListener { spremiRezervaciju() }
        }

        lifecycleScope.launch {
            val usluga = database.uslugaQueries.dohvatiUsluguPoID(uslugaID).executeAsOne()
            uslugaNaziv.text = usluga.naziv
            opisUsluge.text = usluga.opis ?: "Nema opisa"
            cijenaUsluge.text = "${usluga.cijena} €"
            trajanjeUsluge.text = "${usluga.trajanje} min"
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                datumInput.setText("$day/${month + 1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                vrijemeInput.setText(String.format("%02d:%02d", hour, minute))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun spremiRezervaciju() {
        val datum = datumInput.text.toString()
        val vrijeme = vrijemeInput.text.toString()
        val napomene = napomeneInput.text.toString()
        val trenutniKorisnikID = KorisnikManager.dajUlogiranogKorisnika()

        if (datum.isEmpty() || vrijeme.isEmpty()) {
            Toast.makeText(context, "Molimo unesite datum i vrijeme", Toast.LENGTH_SHORT).show()
            return
        }

        if (trenutniKorisnikID == null) {
            Toast.makeText(context, "Niste prijavljeni", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val trenutniKorisnik = database.korisnikQueries.dajKorisnikaPoID(trenutniKorisnikID).executeAsOne()

                database.rezervacijaTerminaUslugeQueries.dodajRezervaciju(
                    korisnikID = trenutniKorisnikID,
                    uslugaID = uslugaID,
                    datum = datum,
                    vrijeme = vrijeme,
                    napomene = napomene
                )
                Toast.makeText(context, "Rezervacija uspješno spremljena za korisnika: ${trenutniKorisnik.korime}", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Greška prilikom spremanja: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}