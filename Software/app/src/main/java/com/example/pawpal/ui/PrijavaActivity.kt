package com.example.pawpal.ui
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pawpal.appdatabase.AppDatabase
import com.example.pawpal.R
import com.example.pawpal.main.MainActivity
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.PawPalApplication
import kotlinx.coroutines.launch

class PrijavaActivity : AppCompatActivity() {
    private lateinit var korisnikDataSource: KorisnikDataSource

    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f01_loginlayout)

        database = (application as PawPalApplication).database
        korisnikDataSource = KorisnikDataSourceImpl(database)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnProziranPri: Button = findViewById(R.id.btnProziranPri)

        btnLogin.setOnClickListener {
            handleLogin()
        }

        btnProziranPri.setOnClickListener {
            startActivity(Intent(this, RegistracijaActivity::class.java))
        }
    }

    private fun handleLogin() {
        val korime = findViewById<EditText>(R.id.editKorime2).text.toString()
        val lozinka = findViewById<EditText>(R.id.editLozinka2).text.toString()

        lifecycleScope.launch {
            val user = korisnikDataSource.dajKorisnikaPoKorime(korime)

            if (user != null && user.lozinka == lozinka) {
                KorisnikManager.ulogiranKorisnik(user.korisnikID)
                startActivity(Intent(this@PrijavaActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this@PrijavaActivity, "Netočni podaci", Toast.LENGTH_SHORT).show()
            }
        }
    }

    }

