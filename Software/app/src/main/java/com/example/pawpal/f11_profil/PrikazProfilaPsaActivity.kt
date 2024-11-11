package com.example.pawpal.f11_profil

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class PrikazProfilaPsaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_prikaz_profila_psa)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfilPsa)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutProfilPsa)
        val navView: NavigationView = findViewById(R.id.navProfilPsa)

        setupHamburgerMenu(drawerLayout, toolbar, navView)


        val ime = intent.getStringExtra("ime") ?: "N/A"
        val dob = intent.getStringExtra("dob") ?: "N/A"
        val pasmina = intent.getStringExtra("pasmina") ?: "N/A"
        val spol = intent.getStringExtra("spol") ?: "N/A"
        val kilaza = intent.getStringExtra("kilaza") ?: "N/A"


        findViewById<TextView>(R.id.textIme).text = "IME: $ime"
        findViewById<TextView>(R.id.textDob).text = "DOB: $dob"
        findViewById<TextView>(R.id.textPasmina).text = "PASMINA: $pasmina"
        findViewById<TextView>(R.id.textSpol).text = "SPOL: $spol"
        findViewById<TextView>(R.id.textKilaza).text = "KILAŽA: $kilaza"
    }
}
