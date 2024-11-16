package com.example.pawpal.f11_profil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class PromjenaLozinkeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_promjena_lozinke)

        val toolbar: Toolbar = findViewById(R.id.toolbarPromjenaLozinke)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutPromjenaLozinke)
        val navView: NavigationView = findViewById(R.id.navPromjenaLozinke)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val staraLozinkaEditText = findViewById<EditText>(R.id.StaraLozinka)
        val novaLozinkaEditText = findViewById<EditText>(R.id.novaLozinka)
        val potvrdiNovuLozinkuEditText = findViewById<EditText>(R.id.NovaLozinka)

        val btnSpremiP: Button = findViewById(R.id.btnSpremiP)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val sačuvanaLozinka = sharedPreferences.getString("korisnikLozinka", "")

        btnSpremiP.setOnClickListener {
            val staraLozinka = staraLozinkaEditText.text.toString()
            val novaLozinka = novaLozinkaEditText.text.toString()
            val potvrdiNovuLozinku = potvrdiNovuLozinkuEditText.text.toString()

            when {
                staraLozinka.isEmpty() || novaLozinka.isEmpty() || potvrdiNovuLozinku.isEmpty() -> {
                    Toast.makeText(this, "Sva polja moraju biti popunjena.", Toast.LENGTH_SHORT).show()
                }
                staraLozinka != sačuvanaLozinka -> {
                    Toast.makeText(this, "Trenutna lozinka nije tačna.", Toast.LENGTH_SHORT).show()
                }
                novaLozinka.length < 6 -> {
                    Toast.makeText(this, "Nova lozinka mora sadržavati najmanje 6 znakova.", Toast.LENGTH_SHORT).show()
                }


                novaLozinka != potvrdiNovuLozinku -> {
                    Toast.makeText(this, "Nova lozinka i potvrda lozinke se ne podudaraju.", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    editor.putString("korisnikLozinka", novaLozinka)
                    editor.apply()

                    Toast.makeText(this, "Lozinka uspešno promenjena!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, ProfilKorisnikaActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
