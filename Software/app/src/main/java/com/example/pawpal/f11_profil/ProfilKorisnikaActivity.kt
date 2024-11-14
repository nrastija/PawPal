/*package com.example.pawpal.f11_profil

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class ProfilKorisnikaActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_profil_korisnika)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfil)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutProfil)
        val navView: NavigationView = findViewById(R.id.navProfil)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val btnMojLjubimac : Button = findViewById(R.id.btnMojLjubimac)
        btnMojLjubimac.setOnClickListener{
            val intent = Intent(this, KreiranjeProfilaPsaActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

 */
package com.example.pawpal.f11_profil

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView
class ProfilKorisnikaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_profil_korisnika)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfil)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutProfil)
        val navView: NavigationView = findViewById(R.id.navProfil)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val ime = sharedPreferences.getString("korisnikIme", "Nema podataka") ?: "Nema podataka"
        val prezime = sharedPreferences.getString("korisnikPrezime", "Nema podataka") ?: "Nema podataka"
        val email = sharedPreferences.getString("korisnikEmail", "Nema podataka") ?: "Nema podataka"
        val korime = sharedPreferences.getString("korisnikKorime", "Nema podataka") ?: "Nema podataka"

        val korisnickoImeTextView: TextView = findViewById(R.id.KorIme)
        val imeTextView: TextView = findViewById(R.id.Ime)
        val prezimeTextView: TextView = findViewById(R.id.Prezime)
        val emailTextView: TextView = findViewById(R.id.Mail)

        korisnickoImeTextView.text = korime
        imeTextView.text = ime
        prezimeTextView.text = prezime
        emailTextView.text = email

        val btnMojLjubimac: Button = findViewById(R.id.btnMojLjubimac)
        btnMojLjubimac.setOnClickListener {
            val intent = Intent(this, KreiranjeProfilaPsaActivity::class.java)
            startActivity(intent)
        }
        val btnUrediPodatkeK: Button = findViewById(R.id.btnUrediPodatkeK)
        btnUrediPodatkeK.setOnClickListener {
            val intent = Intent(this, UredivanjeProfilaKorisnika::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

