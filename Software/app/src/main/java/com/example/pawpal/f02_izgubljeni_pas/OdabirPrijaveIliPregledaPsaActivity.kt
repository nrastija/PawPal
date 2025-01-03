package com.example.pawpal.f02_izgubljeni_pas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R

class OdabirPrijaveIliPregledaPsaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_odabir_prijave_ili_pregleda_psa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prijavipsagumb = findViewById(R.id.prijavapsagumb)
        pregledpsagumb = findViewById(R.id.pregledpsagumb)

        prijavipsagumb.setOnClickListener{
            val intent = Intent(this, PrijavaIzgubljenihPasaActivity::class.java)
            startActivity(intent)
        }

        pregledpsagumb.setOnClickListener{
            val intent = Intent(this, PregledIzgubljenihPasaActivity::class.java)
            startActivity(intent)
        }

    }

    private lateinit var prijavipsagumb: Button
    private lateinit var pregledpsagumb: Button
}