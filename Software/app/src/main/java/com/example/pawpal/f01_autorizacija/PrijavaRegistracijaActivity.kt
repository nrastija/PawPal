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
    private var UlogiranDaNe = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f01_loginlayout)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        initializeLoginLayoutButtons()
    }

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

    private fun PrebaciNaRegistraciju() {
        setContentView(R.layout.f01_registrationlayout)
        UlogiranDaNe = false
        initializeRegistrationLayoutButtons()
    }

    private fun PrebaciNaLogin() {
        setContentView(R.layout.f01_loginlayout)
        UlogiranDaNe = true
        initializeLoginLayoutButtons()
    }

    private fun ulogirajse() {
        val korimeUnos = findViewById<EditText>(R.id.editKorime2).text.toString()
        val lozinkaUnos = findViewById<EditText>(R.id.editLozinka2).text.toString()

        if (korimeUnos.isEmpty() || lozinkaUnos.isEmpty()) {
            Toast.makeText(this, "Molim popunite sve podatke za prijavu.", Toast.LENGTH_SHORT).show()
            return
        }

        val savedKorime = sharedPreferences.getString("korisnikKorime", "")
        val savedLozinka = sharedPreferences.getString("korisnikLozinka", "")

        if (korimeUnos == "test" && lozinkaUnos == "test") {
            Toast.makeText(this, "Uspješna prijava kao 'test' korisnik", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        if (korimeUnos == savedKorime && lozinkaUnos == savedLozinka) {
            Toast.makeText(this, "Uspješna prijava", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Netočni podaci", Toast.LENGTH_SHORT).show()
        }
    }

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

            PrebaciNaLogin()
        } else {
            Toast.makeText(this, "Molim popunite sve podatke.", Toast.LENGTH_SHORT).show()
        }
    }
}
