package com.example.pawpal.f01_autorizacija

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawpal.R
import com.example.pawpal.main.MainActivity

class PrijavaRegistracijaActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var UlogiranDaNe = true  // Ovisno o stanju, prikazujemo prijavu ili registraciju

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f01_loginlayout)  // Početni layout za prijavu

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        initializeLoginLayoutButtons()
    }

    // Prvo inicijaliziramo gumbe za login
    private fun initializeLoginLayoutButtons() {
        val loginButton: Button = findViewById(R.id.btnLogin)
        loginButton.setOnClickListener {
            if (UlogiranDaNe) {
                ulogirajse()
            } else {
                korisnikRegistracija()
            }
        }

        val btnProziranPri: Button = findViewById(R.id.btnProziranPri)
        btnProziranPri.setOnClickListener {
            if (UlogiranDaNe) {
                PrebaciNaRegistraciju()
            } else {
                PrebaciNaLogin()
            }
        }
    }

    // Inicijaliziramo gumbe za registraciju kad smo prebaceni u registracijski layout
    private fun initializeRegistrationLayoutButtons() {
        val btnRegistriraj: Button = findViewById(R.id.btnRegistriraj)
        btnRegistriraj.setOnClickListener {
            korisnikRegistracija()
        }

        val btnProziranReg: Button = findViewById(R.id.btnProziranReg)
        btnProziranReg.setOnClickListener {
            PrebaciNaLogin()
        }
    }

    // Prebacivanje na registracijski layout
    private fun PrebaciNaRegistraciju() {
        setContentView(R.layout.f01_registrationlayout)
        UlogiranDaNe = false  // Prebacivanje u stanje registracije
        initializeRegistrationLayoutButtons()
    }

    // Prebacivanje na login layout
    private fun PrebaciNaLogin() {
        setContentView(R.layout.f01_loginlayout)
        UlogiranDaNe = true  // Prebacivanje u stanje prijave
        initializeLoginLayoutButtons()
    }

    // Prijava korisnika
    private fun ulogirajse() {
        val korimeUnos = findViewById<EditText>(R.id.editKorime2).text.toString()
        val lozinkaUnos = findViewById<EditText>(R.id.editLozinka2).text.toString()

        if (korimeUnos.isEmpty() || lozinkaUnos.isEmpty()) {
            Toast.makeText(this, "Molim popunite sve podatke za prijavu.", Toast.LENGTH_SHORT).show()
            return
        }

        val savedKorime = sharedPreferences.getString("korisnikKorime", "")
        val savedLozinka = sharedPreferences.getString("korisnikLozinka", "")

        // Provjera korisničkih podataka
        if (korimeUnos == savedKorime && lozinkaUnos == savedLozinka) {
            Toast.makeText(this, "Uspješna prijava", Toast.LENGTH_SHORT).show()

            // Nakon uspješne prijave, otvorimo glavni ekran
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // Zatvori prijavu aktivnost
        } else {
            Toast.makeText(this, "Netočni podaci", Toast.LENGTH_SHORT).show()
        }
    }

    // Registracija korisnika
    private fun korisnikRegistracija() {
        val korime = findViewById<EditText>(R.id.editKorime).text.toString()
        val lozinka = findViewById<EditText>(R.id.editLozinka).text.toString()
        val ime = findViewById<EditText>(R.id.editIme).text.toString()
        val prezime = findViewById<EditText>(R.id.editPrezime).text.toString()
        val email = findViewById<EditText>(R.id.editEmail).text.toString()

        if (korime.isNotEmpty() && lozinka.isNotEmpty() && ime.isNotEmpty() && prezime.isNotEmpty() && email.isNotEmpty()) {
            val editor = sharedPreferences.edit()
            editor.putString("korisnikIme", ime)
            editor.putString("korisnikPrezime", prezime)
            editor.putString("korisnikEmail", email)
            editor.putString("korisnikKorime", korime)
            editor.putString("korisnikLozinka", lozinka)
            editor.apply()

            Toast.makeText(this, "Uspješna registracija!", Toast.LENGTH_SHORT).show()

            // Prebacivanje u login layout nakon registracije
            PrebaciNaLogin()
        } else {
            Toast.makeText(this, "Molim popunite sve podatke.", Toast.LENGTH_SHORT).show()
        }
    }
}
