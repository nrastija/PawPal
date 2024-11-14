package com.example.pawpal.f04_veterinar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R

class odabirVeterinaraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_odabir_veterinara)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gumobovi = listOf(
            R.id.gumb1 to "Dr. Niko Rastija",
            R.id.gumb2 to "Dr. Nensi Vugrinec",
            R.id.gumb3 to "Dr. Mirta Vuković",
            R.id.gumb4 to "Dr. Petra Skoko",
            R.id.gumb5 to "Dr. Nara Narić"
        )


        gumobovi.forEach{ (id, naziv_vet)->
            findViewById<Button>(id).setOnClickListener{
                val intent = Intent(this, RezervacijaVeterinaraActivity::class.java)
                intent.putExtra("ime_veterinara",naziv_vet )
            startActivity(intent)
        }

        }
    }
}