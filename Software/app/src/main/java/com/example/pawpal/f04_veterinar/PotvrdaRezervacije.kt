package com.example.pawpal.f04_veterinar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R
import com.example.pawpal.main.MainActivity

class PotvrdaRezervacije : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.fragment_potvrda_rezervacije)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val datumTextView = findViewById<TextView>(R.id.datumTextView)
        val vrijemeTextView = findViewById<TextView>(R.id.vrijemeTextView)
        val uslugaTextView = findViewById<TextView>(R.id.uslugaTextView)
        val opisTextView = findViewById<TextView>(R.id.opisTextView)
        val potvrdiGumb = findViewById<Button>(R.id.potvrdiGumb)
        val odustaniGumb = findViewById<Button>(R.id.odustaniGumb)

        val datum = intent.getStringExtra("odabrani_datum")
        val vrijeme = intent.getStringExtra("odabrano_vrijeme")
        val usluga = intent.getStringExtra("odabrana_usluga")
        val opis = intent.getStringExtra("uneseni_opis")
        val imeVeterinara = intent.getStringExtra("ime_veterinara")


        datumTextView.text = datum
        vrijemeTextView.text = vrijeme
        uslugaTextView.text = usluga
        opisTextView.text = opis

        potvrdiGumb.setOnClickListener{
            Toast.makeText(this, "Rezervacija potvrđena!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }

        odustaniGumb.setOnClickListener{
            finish()
        }

    }
}