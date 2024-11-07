package com.example.pawpal.f04_veterinar

import android.icu.util.Calendar
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rezervacija_veterinara)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val calendar = findViewById<CalendarView>(R.id.calendar)
        val dateText = findViewById<TextView>(R.id.dateText)

        calendar.minDate = System.currentTimeMillis()
        calendar.setOnDateChangeListener{ _, year, month, dayOfMonth->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)


            val formatirajDatum = SimpleDateFormat("EEE, MMM, d, yyyy", Locale.getDefault())
            val formatiranDatum = formatirajDatum.format(selectedDate.time)

            dateText.text = "Odabran datum: $formatiranDatum"
        }
    }
}