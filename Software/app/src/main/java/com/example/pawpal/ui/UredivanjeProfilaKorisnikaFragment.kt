package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class UredivanjeProfilaKorisnikaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var korisnikDataSource: KorisnikDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f11_layout_uredivanje_profila_korisnika, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        korisnikDataSource = KorisnikDataSourceImpl(database)

        postaviPodatkeKorisnika(view)

        view.findViewById<Button>(R.id.btnSpremiP).setOnClickListener {
            spremiPromjene(view)
        }

        view.findViewById<Button>(R.id.btnPromjeniLozinku).setOnClickListener {
            val fragment = PromjenaLozinkeFragment()
            if (fragment is DatabaseConsumer) {
                fragment.database = database
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun postaviPodatkeKorisnika(view: View) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID != null) {
            lifecycleScope.launch {
                try {
                    val korisnik = korisnikDataSource.dajKorisnikaPoID(korisnikID)
                    if (korisnik != null) {
                        val imeEditText = view.findViewById<EditText>(R.id.ImePsa)
                        val prezimeEditText = view.findViewById<EditText>(R.id.Prezime)
                        val emailEditText = view.findViewById<EditText>(R.id.Mail)
                        val korimeEditText = view.findViewById<EditText>(R.id.KorIme)

                        imeEditText.setText(korisnik.ime)
                        prezimeEditText.setText(korisnik.prezime)
                        emailEditText.setText(korisnik.email)
                        korimeEditText.setText(korisnik.korime)
                    } else {
                        prikaziPoruku("Korisnik nije pronađen.")
                    }
                } catch (e: Exception) {
                    prikaziPoruku("Greška pri dohvaćanju podataka: ${e.message}")
                }
            }
        } else {
            prikaziPoruku("Nijedan korisnik nije prijavljen.")
        }
    }

    private fun spremiPromjene(view: View) {
        val imeEditText = view.findViewById<EditText>(R.id.ImePsa)
        val prezimeEditText = view.findViewById<EditText>(R.id.Prezime)
        val emailEditText = view.findViewById<EditText>(R.id.Mail)
        val korimeEditText = view.findViewById<EditText>(R.id.KorIme)

        val novoIme = imeEditText.text.toString()
        val novoPrezime = prezimeEditText.text.toString()
        val noviEmail = emailEditText.text.toString()
        val novoKorime = korimeEditText.text.toString()

        if (novoIme.isEmpty() || novoPrezime.isEmpty() || noviEmail.isEmpty() || novoKorime.isEmpty()) {
            prikaziPoruku("Molimo ispunite sva polja.")
            return
        }

        lifecycleScope.launch {
            try {
                val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
                if (korisnikID == null) {
                    prikaziPoruku("Korisnik nije prijavljen.")
                    return@launch
                }

                korisnikDataSource.azurirajKorisnika(
                    korisnikID = korisnikID,
                    korime = novoKorime,
                    ime = novoIme,
                    prezime = novoPrezime,
                    email = noviEmail
                )

                prikaziPoruku("Promjene su uspješno spremljene.")
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                prikaziPoruku("Greška pri spremanju podataka: ${e.message}")
            }
        }
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show()
    }
}
