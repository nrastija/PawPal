package com.example.pawpal.f02_izgubljeni_pas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import appdatabase.IzgubljeniPsi
import com.example.pawpal.R
import com.example.pawpal.adapters.PsiAdapter
import com.example.pawpal.data.datasource.IzgubljeniPsiDataSource
import com.example.pawpal.data.impl.IzgubljeniPsiImpl
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.math.log

class PregledIzgubljenihPasaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pregled_izgubljenih_pasa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "database.db")
        val db = AppDatabase(driver)
        dataSource = IzgubljeniPsiImpl(db)

        lifecycleScope.launch {
            dataSource.dohvatiSveIzgubljenePse().collect { psiList ->
                psiAdapter = PsiAdapter(psiList){ pasId ->
                    obrisiPrijavuIzgubljenogPsa(pasId)
                }
                recyclerView.adapter = psiAdapter
            }
        }

    }

    private fun obrisiPrijavuIzgubljenogPsa(pasId: Long) {
        lifecycleScope.launch {
            dataSource.obrisiIzgubljenogPsa(pasId)
            var updatedList = dataSource.dohvatiSveIzgubljenePse().first()

            psiAdapter=PsiAdapter(updatedList){ pasId->
                obrisiPrijavuIzgubljenogPsa(pasId)
            }
            recyclerView.adapter = psiAdapter
        }

    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var psiAdapter: PsiAdapter
    private lateinit var dataSource: IzgubljeniPsiImpl
}