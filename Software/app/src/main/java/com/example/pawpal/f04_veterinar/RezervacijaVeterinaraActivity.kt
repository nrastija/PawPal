package com.example.pawpal.f04_veterinar

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Locale

class RezervacijaVeterinaraActivity : AppCompatActivity() {
private lateinit var datumTekst : TextView
private lateinit var datumGumb : Button
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

    }
}