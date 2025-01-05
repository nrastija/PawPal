package com.example.pawpal.ui


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.data.impl.IzgubljeniPsiImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PotvrdaPrijaveIzgubljenogPsaFragment: Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var imePsa: TextView
    private lateinit var opisPsa: TextView
    private lateinit var zadnjalokacija: TextView
    private lateinit var slikapsa: ImageView
    private lateinit var odustani: Button
    private lateinit var potvrdi: Button

    private lateinit var dataSource: IzgubljeniPsiImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_potvrda_prijave_izgubljenog_psa, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val driver = AndroidSqliteDriver(AppDatabase.Schema, requireContext(), "appdatabase.db" )
        database = AppDatabase(driver)

        imePsa = view.findViewById(R.id.imePsa)
        opisPsa = view.findViewById(R.id.opisPsa)
        zadnjalokacija = view.findViewById(R.id.zadnjalokacija)
        slikapsa = view.findViewById(R.id.slikapsa)
        odustani = view.findViewById(R.id.odustaniGumb)
        potvrdi = view.findViewById(R.id.potvrdi)

        dataSource = IzgubljeniPsiImpl(database)

        val ime = arguments?.getString("ime")
        val opis = arguments?.getString("opis")
        val lokacija = arguments?.getString("lokacija")
        val slikaBase64String = arguments?.getString("slika")

        imePsa.text = ime ?: "Nije uneseno ime psa"
        opisPsa.text = opis ?: "Nije unesen opis"
        zadnjalokacija.text = lokacija ?: "Nije unesena zadnje viđena lokacija"

        slikaBase64String?.let {
            val bitmap = decodeBase64ToBitmap(it)
            if (bitmap != null) {
                slikapsa.setImageBitmap(bitmap)
            } else {
                Toast.makeText(requireContext(), "Greška pri učitavanju slike", Toast.LENGTH_SHORT).show()
            }
        }

        odustani.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        potvrdi.setOnClickListener {
            if (ime != null && opis != null && lokacija != null && slikaBase64String != null) {
                lifecycleScope.launch {
                    saveLostDogs(ime, opis, lokacija, slikaBase64String)
                }
                navigateToMainFragment()
            }
            Toast.makeText(requireContext(), "Prijava psa potvrđena!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainFragment() {
        val mainFragment = OdabirPrijaveIliPregledaPsaFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, mainFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private suspend fun saveLostDogs(
        ime: String,
        opis: String,
        lokacija: String,
        base64Image: String
    ) {
        val korisnikId = getCurrentUserId()
        dataSource.dodajIzgubljenogPsa(ime, opis, lokacija, base64Image, korisnikId)
    }

    private fun getCurrentUserId(): Long {
        val korisnikId = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikId == null || korisnikId == -1L) {
            Toast.makeText(requireContext(), "Korisnik nije prijavljen", Toast.LENGTH_SHORT).show()
        }
        return korisnikId ?: -1L
    }
}