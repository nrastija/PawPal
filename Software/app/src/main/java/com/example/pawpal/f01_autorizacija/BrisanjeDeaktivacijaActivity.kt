package com.example.pawpal.f01_autorizacija

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.example.pawpal.ui.PrijavaActivity
import com.example.pawpal.ui.UredivanjeProfilaKorisnikaFragment
import com.google.android.material.navigation.NavigationView

class BrisanjeDeaktivacijaActivity : BaseActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f01_brisanjelayout)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val toolbar: Toolbar = findViewById(R.id.toolbarBrisanje)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayoutBrisanje)
        val navView: NavigationView = findViewById(R.id.navBrisanje)
        setupHamburgerMenu(drawerLayout, toolbar, navView)

        val btnBrisanje: Button = findViewById(R.id.btnBrisanjeProfila)
        btnBrisanje.setOnClickListener {
            showBrisanjeDialog()
        }
        val btnOdjava: Button = findViewById(R.id.btnOdjava)
        btnOdjava.setOnClickListener {
            showOdjavaDialog()
        }

        val btnUredivanjeProfila: Button = findViewById(R.id.btnUredivanjeProfila)
        btnUredivanjeProfila.setOnClickListener {
            val intent = Intent(this, UredivanjeProfilaKorisnikaFragment::class.java)
            startActivity(intent)
        }
    }

    private fun showBrisanjeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Jeste li sigurni da želite deaktivirati račun?")

        builder.setPositiveButton("Da") { dialog, _ ->
            deaktivacija()
            dialog.dismiss()
        }
        builder.setNegativeButton("Ne") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showOdjavaDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Jeste li sigurni da se želite odjaviti?")

        builder.setPositiveButton("Da") { dialog, _ ->
            odjava()
            dialog.dismiss()
        }
        builder.setNegativeButton("Ne") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun deaktivacija() {
        val editor = sharedPreferences.edit()
        editor.remove("korisnikIme")
        editor.remove("korisnikPrezime")
        editor.remove("korisnikEmail")
        editor.remove("korisnikKorime")
        editor.remove("korisnikLozinka")
        editor.apply()

        Toast.makeText(this, "Uspješno ste obrisali račun.", Toast.LENGTH_SHORT).show()


        val intent = Intent(this, PrijavaActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun odjava(){

        Toast.makeText(this, "Odjavljeni ste.", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, PrijavaActivity::class.java)
        startActivity(intent)
        finish()

    }
}
