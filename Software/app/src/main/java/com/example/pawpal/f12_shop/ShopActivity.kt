package com.example.pawpal.f12_shop

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.google.android.material.navigation.NavigationView

class ShopActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_layout_shop)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        setupHamburgerMenu(drawerLayout, toolbar, navView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // kreiranje kosarice
        menuInflater.inflate(R.menu.f12_menu_kosarica, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // funkcija obrade klika na kosaricu
        return when (item.itemId) {
            R.id.shop_basket -> {
                val intent = Intent(this, KosaricaActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}