package com.example.pawpal.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pawpal.appdatabase.AppDatabase
import com.example.pawpal.R
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.main.PawPalApplication
import kotlinx.coroutines.launch


class RegistracijaActivity : AppCompatActivity() {
    private lateinit var korisnikDataSource: KorisnikDataSource

    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f01_registracija)

        database = (application as PawPalApplication).database
        korisnikDataSource = KorisnikDataSourceImpl(database)

        val btnRegistriraj: Button = findViewById(R.id.btnRegistriraj)
        val btnProziranReg: Button = findViewById(R.id.btnProziranReg)

        btnRegistriraj.setOnClickListener {
            handleRegistration()
        }

        btnProziranReg.setOnClickListener {
            startActivity(Intent(this, PrijavaActivity::class.java))
            finish()
        }
    }

    private fun handleRegistration() {
        val korime = findViewById<EditText>(R.id.editKorime).text.toString()
        val lozinka = findViewById<EditText>(R.id.editLozinka).text.toString()
        val ime = findViewById<EditText>(R.id.editIme).text.toString()
        val prezime = findViewById<EditText>(R.id.editPrezime).text.toString()
        val email = findViewById<EditText>(R.id.editEmail).text.toString()

        if (korime.isEmpty() || lozinka.isEmpty() || ime.isEmpty() ||
            prezime.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Molim popunite sve podatke.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                korisnikDataSource.dodajKorisnik(korime, ime, prezime, email, lozinka)
                Toast.makeText(this@RegistracijaActivity, "Uspješna registracija!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegistracijaActivity, PrijavaActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@RegistracijaActivity, "Greška pri registraciji: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


