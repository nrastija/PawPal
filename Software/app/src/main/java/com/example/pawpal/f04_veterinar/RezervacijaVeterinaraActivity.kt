package com.example.pawpal.f04_veterinar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.min

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
        setContentView(R.layout.activity_rezervacija_veterinara)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        datumTekst = findViewById(R.id.datumTekst)
        datumGumb = findViewById(R.id.datumGumb)

        vrijemeTekst = findViewById(R.id.vrijemeTekst)
        vrijemeGumb = findViewById(R.id.vrijemeGumb)

        spiner = findViewById(R.id.spiner)

        dodatniOpis = findViewById(R.id.dodatniOpis)
        potvrdi = findViewById(R.id.potvrdi)
        ponisti = findViewById(R.id.ponisti)


        ponisti.setOnClickListener{
            datumTekst.text = ""
            vrijemeTekst.text = ""
            spiner.setSelection(0)
            dodatniOpis.text.clear()
            Toast.makeText(this, "Podaci su poništeni", Toast.LENGTH_SHORT).show()
        }

        potvrdi.setOnClickListener{
            val opis = dodatniOpis.text.toString()

            if(opis.isNotEmpty()){

            }
        }



        val usluge = listOf(
                    "Odaberite uslugu",
                    "Prvi pregled",
                    "Kontrola" ,
                    " Cijepljenje",
                    " Laboratorijska dijagnostika",
                    " Dermatologija",
                    " Kirurgija",
                    " Neurologija",
                    " Oftamologija",
                    " Stomatologija"
        )
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, usluge)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spiner.adapter=adapter

        spiner.setSelection(0)
        spiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val selectedService = parentView?.getItemAtPosition(position) as String

                if(selectedService == "Odaberite uslugu"){
                    Toast.makeText(this@RezervacijaVeterinaraActivity, "Molimo odaberite uslugu", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@RezervacijaVeterinaraActivity, "Odabrali ste uslugu: $selectedService", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                spiner.setSelection(0)
            }
        }

        datumGumb.setOnClickListener{

            val kalendar = Calendar.getInstance()
            kalendar.add(Calendar.DAY_OF_YEAR, 1)
            val minDate = kalendar.timeInMillis

            val biracDatuma = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    kalendar.set(year, month, dayOfMonth)

                    if(kalendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                        datumTekst.text = "Nedjelja nije dostupna. Molimo odaberite drugi dan"
                    } else{
                        val selectedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(kalendar.time)
                        datumTekst.text = "Odabrani datum: $selectedDate"
                    }
                },
                kalendar.get(Calendar.YEAR),
                kalendar.get(Calendar.MONTH),
                kalendar.get(Calendar.DAY_OF_MONTH)
            )
            biracDatuma.datePicker.minDate = minDate
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
                    } else {
                        vrijemeTekst.text = "Molimo odaberite vrijeme između 08:00 i 19:00."
                    }
                },
                trenutniSat, trenutnaMinuta, true
            )
            biracVremena.show()
        }
    }
}