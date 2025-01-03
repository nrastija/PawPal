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
import com.example.pawpal.data.datasource.PasDataSource
import com.example.pawpal.data.impl.PasDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class UredivanjeProfilaPsaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var pasDataSource: PasDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f11_layout_uredivanje_profila_psa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pasDataSource = PasDataSourceImpl(database)

        postaviPodatkePsa(view)

        view.findViewById<Button>(R.id.btnSpremiP).setOnClickListener {
            spremiPromjene(view)
        }
    }

    private fun postaviPodatkePsa(view: View) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID != null) {
            lifecycleScope.launch {
                try {
                    val pas = pasDataSource.dohvatiPasPoKorisnikID(korisnikID)
                    if (pas != null) {
                        val imeEditText = view.findViewById<EditText>(R.id.Ime)
                        val dobEditText = view.findViewById<EditText>(R.id.Dob)
                        val pasminaEditText = view.findViewById<EditText>(R.id.Pasmina)
                        val spolEditText = view.findViewById<EditText>(R.id.Spol)
                        val kilazaEditText = view.findViewById<EditText>(R.id.Kilaza)

                        imeEditText.setText(pas.ime)
                        dobEditText.setText(pas.dob)
                        pasminaEditText.setText(pas.pasmina)
                        spolEditText.setText(pas.spol)
                        kilazaEditText.setText(pas.kilaza)
                    } else {
                        prikaziPoruku("Profil psa nije pronađen.")
                    }
                } catch (e: Exception) {
                    prikaziPoruku("Greška pri dohvaćanju podataka: ${e.message}")
                }
            }
        } else {
            prikaziPoruku("Korisnik nije prijavljen.")
        }
    }

    private fun spremiPromjene(view: View) {
        val imeEditText = view.findViewById<EditText>(R.id.Ime)
        val dobEditText = view.findViewById<EditText>(R.id.Dob)
        val pasminaEditText = view.findViewById<EditText>(R.id.Pasmina)
        val spolEditText = view.findViewById<EditText>(R.id.Spol)
        val kilazaEditText = view.findViewById<EditText>(R.id.Kilaza)

        val novoIme = imeEditText.text.toString()
        val novaDob = dobEditText.text.toString()
        val novaPasmina = pasminaEditText.text.toString()
        val noviSpol = spolEditText.text.toString()
        val novaKilaza = kilazaEditText.text.toString()

        if (novoIme.isEmpty() || novaDob.isEmpty() || novaPasmina.isEmpty() || noviSpol.isEmpty() || novaKilaza.isEmpty()) {
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

                pasDataSource.insertPas(
                    ime = novoIme,
                    dob = novaDob,
                    pasmina = novaPasmina,
                    spol = noviSpol,
                    kilaza = novaKilaza,
                    korisnikID = korisnikID
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
