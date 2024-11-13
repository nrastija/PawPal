package com.example.pawpal.f04_veterinar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale

class RezervacijaVeterinaraActivity : AppCompatActivity() {
private lateinit var datumTekst : TextView
private lateinit var datumGumb : Button
private lateinit var vrijemeTekst :  TextView
private lateinit var vrijemeGumb : Button

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

        datumGumb.setOnClickListener{

            val kalendar = Calendar.getInstance()
            val godina = kalendar.get(Calendar.YEAR)
            val mjesec = kalendar.get(Calendar.MONTH)
            val dan = kalendar.get(Calendar.DAY_OF_MONTH)

            val biracDatuma = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val odabranDatum = "$selectedDay/${selectedMonth+1}/$selectedYear"
                    datumTekst.text = "Odabrani datum: $odabranDatum"
                },
                godina, mjesec, dan
            )
            biracDatuma.show()
        }


        vrijemeGumb.setOnClickListener{
            val kalendar = Calendar.getInstance()
            val trenutniSat = kalendar.get(Calendar.HOUR_OF_DAY)
            val trenutnaMinuta = kalendar.get(Calendar.MINUTE)

            val biracVremena = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val formatiranoVrijeme = String.format("%02d:%02d", selectedHour, selectedMinute)
                    vrijemeTekst.text = "Odabrano vrijeme: $formatiranoVrijeme"
                },
                trenutniSat, trenutnaMinuta, true
            )
            biracVremena.show()
        }


    }
}