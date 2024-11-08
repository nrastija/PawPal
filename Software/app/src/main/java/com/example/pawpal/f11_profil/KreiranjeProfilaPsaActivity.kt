package com.example.pawpal.f11_profil


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class KreiranjeProfilaPsaActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f11_layout_kreiranje_profila_psa)

        val toolbar: Toolbar = findViewById(R.id.toolbarKreiranjePsa)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutKreiranjePsa)
        val navView: NavigationView = findViewById(R.id.navKreiranjePsa)

        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val btnKreirajProfil : Button = findViewById(R.id.btnKreirajProfil)
        btnKreirajProfil.setOnClickListener {
            val intent = Intent(this, PrikazProfilaPsaActivity::class.java)
            startActivity(intent)

        }
    }
}