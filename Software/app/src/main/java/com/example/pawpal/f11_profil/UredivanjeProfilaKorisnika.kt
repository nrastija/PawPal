package com.example.pawpal.f11_profil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class UredivanjeProfilaKorisnika : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_uredivanje_profila_korisnika)

        val toolbar: Toolbar = findViewById(R.id.toolbarUredivanjeK)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutUredivanjeK)
        val navView: NavigationView = findViewById(R.id.navUredivanjeK)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val imeEditText = findViewById<EditText>(R.id.ImePsa)
        val prezimeEditText = findViewById<EditText>(R.id.Prezime)
        val emailEditText = findViewById<EditText>(R.id.Mail)
        val korimeEditText = findViewById<EditText>(R.id.KorIme)

        imeEditText.setText(sharedPreferences.getString("korisnikIme", ""))
        prezimeEditText.setText(sharedPreferences.getString("korisnikPrezime", ""))
        emailEditText.setText(sharedPreferences.getString("korisnikEmail", ""))
        korimeEditText.setText(sharedPreferences.getString("korisnikKorime", ""))

        val btnPromjeniLozinku: Button = findViewById(R.id.btnPromjeniLozinku)
        btnPromjeniLozinku.setOnClickListener {
            val intent = Intent(this, PromjenaLozinkeActivity::class.java)
            startActivity(intent)
        }

        val btnSpremiP: Button = findViewById(R.id.btnSpremiP)
        btnSpremiP.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putString("korisnikIme", imeEditText.text.toString())
            editor.putString("korisnikPrezime", prezimeEditText.text.toString())
            editor.putString("korisnikEmail", emailEditText.text.toString())
            editor.putString("korisnikKorime", korimeEditText.text.toString())
            editor.apply()

            val intent = Intent(this, ProfilKorisnikaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
