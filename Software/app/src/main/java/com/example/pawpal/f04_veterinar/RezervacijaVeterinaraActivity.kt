package com.example.pawpal.f04_veterinar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R
import java.text.SimpleDateFormat
import java.util.Locale

class RezervacijaVeterinaraActivity : AppCompatActivity() {
private lateinit var datumTekst : TextView
private lateinit var datumGumb : Button
private lateinit var vrijemeTekst :  TextView
private lateinit var vrijemeGumb : Button
private lateinit var spiner : Spinner
private lateinit var dodatniOpis : EditText
private lateinit var potvrdi : Button
private lateinit var ponisti : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_rezervacija_veterinara)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(0, 0, 0, imeInsets.bottom)
            insets
        }

        val imeVeterinara = intent.getStringExtra("ime_veterinara")

        val textView = findViewById<TextView>(R.id.textImeVeterinara)
        textView.text = imeVeterinara



        datumTekst = findViewById(R.id.datumTekst)
        datumGumb = findViewById(R.id.datumGumb)

        vrijemeTekst = findViewById(R.id.vrijemeTekst)
        vrijemeGumb = findViewById(R.id.vrijemeGumb)

        spiner = findViewById(R.id.spiner)

        dodatniOpis = findViewById(R.id.dodatniOpis)
        potvrdi = findViewById(R.id.potvrdi)
        ponisti = findViewById(R.id.ponisti)


        ponisti.setOnClickListener {
            datumTekst.text = ""
            vrijemeTekst.text = ""
            spiner.setSelection(0)
            dodatniOpis.text.clear()
            Toast.makeText(this, "Podaci su poništeni", Toast.LENGTH_SHORT).show()
        }

        potvrdi.setOnClickListener {
            val opis = dodatniOpis.text.toString()
            val datum = datumTekst.text.toString()
            val vrijeme = vrijemeTekst.text.toString()
            val usluga = spiner.selectedItem.toString()
            val imeVeterinara = intent.getStringExtra("ime_veterinara")

            val ValidanDatum =
                !datum.contains("Nedjeljom ne radimo!") && datum.contains("Odabrani datum")
            val ValidnoVrijeme = vrijeme.contains("Odabrano vrijeme")

            if (opis.isNotEmpty() && datum.isNotEmpty() && vrijeme.isNotEmpty() && usluga != "Odaberite uslugu" && ValidanDatum && ValidnoVrijeme) {
                val intent = Intent(this, PotvrdaRezervacije::class.java)
                intent.putExtra("odabrani_datum", datum)
                intent.putExtra("odabrano_vrijeme", vrijeme)
                intent.putExtra("odabrana_usluga", usluga)
                intent.putExtra("uneseni_opis", opis)
                intent.putExtra("ime_veterinara", imeVeterinara)

                startActivity(intent)
            } else {
                Toast.makeText(this, "Molimo ispunite sve podatke ispravno.", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        val usluge = listOf(
            "Odaberite uslugu",
            "Prvi pregled - 30,00€",
            "Kontrola - 35,00€",
            " Cijepljenje - 60,00€",
            " Laboratorijska dijagnostika - 120,00€",
            " Dermatologija - 70,00€",
            " Kirurgija - 170,00€",
            " Neurologija - 200,00€",
            " Oftamologija - 80,00€",
            " Stomatologija - 60,00€"
        )
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usluge)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spiner.adapter = adapter

        spiner.setSelection(0)
        spiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {}


            override fun onNothingSelected(parent: AdapterView<*>?) {
                spiner.setSelection(0)
            }
        }

        datumGumb.setOnClickListener {
            val kalendar = Calendar.getInstance()
            kalendar.add(Calendar.DAY_OF_YEAR, 1)
            val minDate = kalendar.timeInMillis

            val rezerviraniDatumi = listOf(
                Calendar.getInstance().apply { set(2024, 10, 23, 0, 0, 0); clear(Calendar.MILLISECOND) }.timeInMillis,
                Calendar.getInstance().apply { set(2024, 10, 28, 0, 0, 0); clear(Calendar.MILLISECOND) }.timeInMillis,
                Calendar.getInstance().apply { set(2024, 10, 30, 0, 0, 0); clear(Calendar.MILLISECOND) }.timeInMillis
            )

            val biracDatuma = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDateCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth, 0, 0, 0)
                        clear(Calendar.MILLISECOND)
                    }
                    val selectedDate = selectedDateCalendar.timeInMillis

                    when {
                        rezerviraniDatumi.contains(selectedDate) -> {
                            Toast.makeText(
                                this,
                                "Odabrani termin je zauzet. Molimo odaberite drugi datum.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        selectedDateCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY -> {
                            datumTekst.text = "Nedjeljom ne radimo! Molimo odaberite drugi dan"
                            datumTekst.setTextColor(ContextCompat.getColor(this, R.color.warningColor))
                            datumTekst.setTypeface(null, android.graphics.Typeface.BOLD)
                        }
                        else -> {
                            val selectedDateText =
                                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(selectedDate)
                            datumTekst.text = "Odabrani datum: $selectedDateText"
                            datumTekst.setTextColor(ContextCompat.getColor(this, R.color.textColorPrimary))
                            datumTekst.setTypeface(null, android.graphics.Typeface.ITALIC)
                        }
                    }
                },
                kalendar.get(Calendar.YEAR),
                kalendar.get(Calendar.MONTH),
                kalendar.get(Calendar.DAY_OF_MONTH)
            )

            biracDatuma.datePicker.minDate = minDate

            biracDatuma.datePicker.init(
                kalendar.get(Calendar.YEAR),
                kalendar.get(Calendar.MONTH),
                kalendar.get(Calendar.DAY_OF_MONTH)
            ) { _, year, monthOfYear, dayOfMonth ->
                val selectedDateCalendar = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                    clear(Calendar.MILLISECOND)
                }
                val selectedDate = selectedDateCalendar.timeInMillis

                if (rezerviraniDatumi.contains(selectedDate)) {
                    biracDatuma.datePicker.updateDate(
                        kalendar.get(Calendar.YEAR),
                        kalendar.get(Calendar.MONTH),
                        kalendar.get(Calendar.DAY_OF_MONTH)
                    )
                    Toast.makeText(
                        this,
                        "Odabrani termin je zauzet. Molimo odaberite drugi datum.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            biracDatuma.show()
        }





        vrijemeGumb.setOnClickListener {
            val kalendar = Calendar.getInstance()
            val trenutniSat = kalendar.get(Calendar.HOUR_OF_DAY)
            val trenutnaMinuta = kalendar.get(Calendar.MINUTE)

            val biracVremena = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    if (selectedHour in 8..18) {
                        val formatiranoVrijeme =
                            String.format("%02d:%02d", selectedHour, selectedMinute)
                        vrijemeTekst.text = "Odabrano vrijeme: $formatiranoVrijeme"
                        vrijemeTekst.setTextColor(ContextCompat.getColor(this, R.color.textColorPrimary))
                        vrijemeTekst.setTypeface(null, android.graphics.Typeface.ITALIC)
                    } else {
                        vrijemeTekst.text = "Molimo odaberite vrijeme između 08:00 i 19:00."
                        vrijemeTekst.setTextColor(ContextCompat.getColor(this, R.color.warningColor))
                        vrijemeTekst.setTypeface(null, android.graphics.Typeface.BOLD)
                    }
                },
                trenutniSat, trenutnaMinuta, true
            )
            biracVremena.show()
        }
    }
}