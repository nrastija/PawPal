package com.example.pawpal.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RezervacijaVeterinaraFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private var veterinarID: Long = 0

    private lateinit var datumTekst: TextView
    private lateinit var datumGumb: Button
    private lateinit var vrijemeTekst: TextView
    private lateinit var vrijemeGumb: Button
    private lateinit var spiner: Spinner
    private lateinit var dodatniOpis: EditText
    private lateinit var gumbponisti: Button
    private lateinit var gumbpotvrdi: Button

    companion object {
        private const val ARG_VETERINAR_ID = "veterinarID"

        fun newInstance(veterinarID: Long): RezervacijaVeterinaraFragment {
            val fragment = RezervacijaVeterinaraFragment()
            val args = Bundle()
            args.putLong(ARG_VETERINAR_ID, veterinarID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            veterinarID = it.getLong(ARG_VETERINAR_ID)

        }
        Log.d("VeterinarID", "Veterinar ID: $veterinarID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_rezervacija_veterinara, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val imeVet: TextView = view.findViewById(R.id.textImeVeterinara)
        val titula: TextView = view.findViewById(R.id.titulaVetRez)
        gumbponisti = view.findViewById(R.id.ponistiVet)
        gumbpotvrdi = view.findViewById(R.id.potvrdiVet)


        datumTekst = view.findViewById(R.id.datumTekst)
        datumGumb = view.findViewById(R.id.datumGumb)
        vrijemeTekst = view.findViewById(R.id.vrijemeTekst)
        vrijemeGumb = view.findViewById(R.id.vrijemeGumb)
        spiner = view.findViewById(R.id.spiner)
        dodatniOpis = view.findViewById(R.id.dodatniOpis)

        lifecycleScope.launch {
            val veterinar = database.veterinarQueries.dohvatiVeterinaraID(veterinarID).executeAsOne()
            imeVet.text = veterinar.imePrezime
            titula.text = veterinar.specijalizacija
        }



        val usluge = listOf(
            "Odaberite uslugu",
            "Prvi pregled",
            "Kontrola",
            "Cijepljenje",
            "Laboratorijska dijagnostika",
            "Dermatologija",
            "Kirurgija",
            "Neurologija",
            "Oftamologija",
            "Stomatologija"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, usluge)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spiner.adapter = adapter

        spiner.setSelection(0)

        gumbponisti.setOnClickListener {
            datumTekst.text = ""
            vrijemeTekst.text = ""
            spiner.setSelection(0)
            dodatniOpis.text.clear()
            showToast("Podaci su poništeni")
        }

        gumbpotvrdi.setOnClickListener {

            val opis = dodatniOpis.text.toString()
            val datum = datumTekst.text.toString()
            val vrijeme = vrijemeTekst.text.toString()
            val usluga = spiner.selectedItem.toString()

            val ValidanDatum = !datum.contains("Nedjeljom ne radimo!") && datum.contains("Odabrani datum")
            val ValidnoVrijeme = vrijeme.contains("Odabrano vrijeme")

            if (opis.isNotEmpty() && datum.isNotEmpty() && vrijeme.isNotEmpty() && usluga != "Odaberite uslugu" && ValidanDatum && ValidnoVrijeme) {
                val fragment = PotvrdaRezervacijeFragment().apply {
                    arguments = Bundle().apply {
                        putString("odabrani_datum", datum)
                        putString("odabrano_vrijeme", vrijeme)
                        putString("odabrana_usluga", usluga)
                        putString("uneseni_opis", opis)
                        putLong("veterinarID", veterinarID)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                showToast("Molimo ispunite sve podatke ispravno.")
            }
        }

        datumGumb.setOnClickListener { openDatePicker() }
        vrijemeGumb.setOnClickListener { openTimePicker() }
    }

    private fun openDatePicker() {
        val kalendar = Calendar.getInstance()
        kalendar.add(Calendar.DAY_OF_YEAR, 1)
        val minDate = kalendar.timeInMillis

        val rezerviraniDatumi = listOf(
            Calendar.getInstance().apply { set(2024, 10, 23, 0, 0, 0); clear(Calendar.MILLISECOND) }.timeInMillis,
            Calendar.getInstance().apply { set(2024, 10, 28, 0, 0, 0); clear(Calendar.MILLISECOND) }.timeInMillis,
            Calendar.getInstance().apply { set(2024, 10, 30, 0, 0, 0); clear(Calendar.MILLISECOND) }.timeInMillis
        )

        val biracDatuma = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDateCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    clear(Calendar.MILLISECOND)
                }
                val selectedDate = selectedDateCalendar.timeInMillis

                when {
                    rezerviraniDatumi.contains(selectedDate) -> {
                        showToast("Odabrani termin je zauzet. Molimo odaberite drugi datum.")
                    }
                    selectedDateCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY -> {
                        datumTekst.text = "Nedjeljom ne radimo! Molimo odaberite drugi dan"
                        datumTekst.setTextColor(ContextCompat.getColor(requireContext(), R.color.warningColor))
                    }
                    else -> {
                        val selectedDateText =
                            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate)
                        datumTekst.text = "Odabrani datum: $selectedDateText"
                        datumTekst.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorPrimary))
                    }
                }
            },
            kalendar.get(Calendar.YEAR),
            kalendar.get(Calendar.MONTH),
            kalendar.get(Calendar.DAY_OF_MONTH)
        )

        biracDatuma.datePicker.minDate = minDate
        biracDatuma.show()
    }

    private fun openTimePicker() {
        val kalendar = Calendar.getInstance()
        val trenutniSat = kalendar.get(Calendar.HOUR_OF_DAY)
        val trenutnaMinuta = kalendar.get(Calendar.MINUTE)

        val biracVremena = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                if (selectedHour in 8..18) {
                    val formatiranoVrijeme = String.format("%02d:%02d", selectedHour, selectedMinute)
                    vrijemeTekst.text = "Odabrano vrijeme: $formatiranoVrijeme"
                } else {
                    vrijemeTekst.text = "Molimo odaberite vrijeme između 08:00 i 19:00."
                }
            },
            trenutniSat, trenutnaMinuta, true
        )
        biracVremena.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}