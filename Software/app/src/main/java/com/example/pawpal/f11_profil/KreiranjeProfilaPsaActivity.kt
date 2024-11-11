package com.example.pawpal.f11_profil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.f11_profil.entiteti.ProfilPsa
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class KreiranjeProfilaPsaActivity : BaseActivity() {


    private val dogProfiles = mutableListOf<ProfilPsa>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_kreiranje_profila_psa)

        val toolbar: Toolbar = findViewById(R.id.toolbarKreiranjePsa)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutKreiranjePsa)
        val navView: NavigationView = findViewById(R.id.navKreiranjePsa)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val btnKreirajProfil: Button = findViewById(R.id.btnKreirajProfil)
        btnKreirajProfil.setOnClickListener {

            val ime = findViewById<EditText>(R.id.Ime).text.toString()
            val dob = findViewById<EditText>(R.id.Dob).text.toString()
            val pasmina = findViewById<EditText>(R.id.Pasmina).text.toString()
            val spol = findViewById<EditText>(R.id.Spol).text.toString()
            val kilaza = findViewById<EditText>(R.id.Kilaza).text.toString()


            if (ime.isNotEmpty() && dob.isNotEmpty() && pasmina.isNotEmpty() && spol.isNotEmpty() && kilaza.isNotEmpty()) {

                val noviProfilPsa = ProfilPsa(ime, dob, pasmina, spol, kilaza)


                val intent = Intent(this, PrikazProfilaPsaActivity::class.java).apply {
                    putExtra("ime", noviProfilPsa.ime)
                    putExtra("dob", noviProfilPsa.dob)
                    putExtra("pasmina", noviProfilPsa.pasmina)
                    putExtra("spol", noviProfilPsa.spol)
                    putExtra("kilaza", noviProfilPsa.kilaza)
                }


                Toast.makeText(this, "Profil psa je uspješno kreiran!", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            } else {

                Toast.makeText(this, "Molimo ispunite sve podatke.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
