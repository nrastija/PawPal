package com.example.pawpal.f11_profil

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.example.pawpal.R
import com.google.android.material.color.utilities.SchemeTonalSpot

class profilactivity : AppCompatActivity(){

    private lateinit var imeEditText: EditText
    private lateinit var spolEditText: EditText
    private lateinit var kilažaEditText: EditText
    private lateinit var dobEditText: EditText
    private lateinit var pasminaEditText: EditText
    private lateinit var spremiButton: Button
    private lateinit var kreiranjeProfilaTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_profil_psa)

        imeEditText = findViewById(R.id.Ime)
        spolEditText = findViewById(R.id.Spol)
        kilažaEditText = findViewById(R.id.Kilaza)
        dobEditText = findViewById(R.id.Dob)
        pasminaEditText = findViewById(R.id.Pasmina)
        spremiButton = findViewById(R.id.btnSpremi)
        kreiranjeProfilaTextView = findViewById(R.id.KreiranjeProfila)


        spremiButton.setOnClickListener{
            spremiPodatke()
        }
    }

    private fun spremiPodatke() {
        val ime = imeEditText.text.toString()
        val spol = spolEditText.text.toString()
        val kilaža = kilažaEditText.text.toString()
        val dob = dobEditText.text.toString()
        val pasmina = pasminaEditText.text.toString()
    }

}