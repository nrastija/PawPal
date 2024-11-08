package com.example.pawpal.f11_profil

import android.os.Bundle
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

    }
}