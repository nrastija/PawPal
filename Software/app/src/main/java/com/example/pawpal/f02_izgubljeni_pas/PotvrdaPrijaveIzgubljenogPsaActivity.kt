package com.example.pawpal.f02_izgubljeni_pas

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.data.impl.IzgubljeniPsiImpl
import com.example.pawpal.main.MainActivity
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream

class PotvrdaPrijaveIzgubljenogPsaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_potvrda_prijave_izgubljenog_psa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        opisPsa = findViewById(R.id.opisPsa)
        zadnjalokacija = findViewById(R.id.zadnjalokacija)
        slikapsa = findViewById(R.id.slikapsa)
        odustani = findViewById(R.id.odustaniGumb)
        potvrdi = findViewById(R.id.potvrdi)

        val driver = AndroidSqliteDriver(AppDatabase.Schema, this, "database.db")
        val db = AppDatabase(driver)
        dataSource = IzgubljeniPsiImpl(db)


        val opis = intent.getStringExtra("opis")
        val lokacija = intent.getStringExtra("lokacija")
        val slikaBase64String = intent.getStringExtra("slika")

        opisPsa.text = opis ?: "Nije unesen opis"
        zadnjalokacija.text = lokacija ?: "Nije unesena zadnje viđena lokacija"

        slikaBase64String?.let {
            val bitmap = decodeBase64ToBitmap(it)
            if (bitmap != null) {
                slikapsa.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Greška pri učitavanju slike", Toast.LENGTH_SHORT).show()
            }
        }
        odustani.setOnClickListener{
            finish()
        }

        potvrdi.setOnClickListener{

            if (opis != null && lokacija != null && slikaBase64String != null) {



                lifecycleScope.launch {
                    saveLostDogs(opis, lokacija, slikaBase64String)
                }

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()

            }
            Toast.makeText(this, "Prijava psa potvrđena!", Toast.LENGTH_SHORT).show()

            }

        }


    private fun convertImageToByteArray(uri: Uri): ByteArray? {
        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        inputStream.close()
        return byteArrayOutputStream.toByteArray()
    }


    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }


    private suspend fun saveLostDogs(opis: String, lokacija: String, base64Image: String) {
        dataSource.dodajIzgubljenogPsa(opis, lokacija, base64Image)
    }


    private lateinit var opisPsa: TextView
    private lateinit var zadnjalokacija: TextView
    private lateinit var slikapsa: ImageView
    private lateinit var odustani: Button
    private lateinit var potvrdi: Button

    private lateinit var dataSource: IzgubljeniPsiImpl
}