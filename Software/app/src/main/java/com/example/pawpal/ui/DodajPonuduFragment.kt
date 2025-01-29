package com.example.pawpal.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.util.Calendar

class DodajPonuduFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var nazivUnos: EditText
    private lateinit var opisUnos: EditText
    private lateinit var datumUnos: EditText
    private lateinit var uvjetiUnos: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (requireActivity().application as PawPalApplication).database
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f03_dodaj_ponudu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    private fun setupViews(view: View) {
        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        nazivUnos = view.findViewById(R.id.nazivPonude)
        opisUnos = view.findViewById(R.id.opisPonude)
        datumUnos = view.findViewById(R.id.datumValjanostiPonude)
        uvjetiUnos = view.findViewById(R.id.uvjetiPonude)

        view.findViewById<Button>(R.id.btnSpremiPonudu).setOnClickListener {
            spremiPonudu()
        }
    }

    private fun spremiPonudu() {
        val naziv = nazivUnos.text.toString()
        val opis = opisUnos.text.toString()
        val datum = datumUnos.text.toString()
        val uvjeti = uvjetiUnos.text.toString()

        if (naziv.isEmpty() || opis.isEmpty() || datum.isEmpty() || uvjeti.isEmpty()) {
            Toast.makeText(context, "Molimo popunite sva polja", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidDateFormat(datum)) {
            Toast.makeText(context, "Datum mora biti u formatu dd.mm.yyyy", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                database.promoPonudaQueries.insertPromoPonuda(
                    naziv = naziv,
                    opis = opis,
                    datumValjanosti = datum,
                    uvjeti = uvjeti
                )
                Toast.makeText(context, "Ponuda uspješno spremljena!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Greška: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidDateFormat(date: String): Boolean {
        val regex = """^\d{2}\.\d{2}\.\d{4}$""".toRegex()
        return regex.matches(date)
    }
}
