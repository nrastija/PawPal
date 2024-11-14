package com.example.pawpal.main

import android.os.Bundle
import com.example.pawpal.R
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Povežite sa vašim layoutom activity_main.xml

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout) // Provjerite da li imate ovaj ID u XML-u
        val toolbar: Toolbar = findViewById(R.id.toolbar)  // Provjerite da li imate ovaj ID u XML-u
        val navView: NavigationView = findViewById(R.id.nav_view)  // Provjerite da li imate ovaj ID u XML-u

        setupHamburgerMenu(drawerLayout, toolbar, navView) // Pozivate metodu iz BaseActivity za konfiguraciju hamburger menija
    }
}
