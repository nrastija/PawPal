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

class PromjenaLozinkeFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var korisnikDataSource: KorisnikDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f11_layout_promjena_lozinke, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        korisnikDataSource = KorisnikDataSourceImpl(database)

        val staraLozinkaEditText = view.findViewById<EditText>(R.id.StaraLozinka)
        val novaLozinkaEditText = view.findViewById<EditText>(R.id.novaLozinka)
        val potvrdiNovuLozinkuEditText = view.findViewById<EditText>(R.id.NovaLozinka)

        val btnSpremiP: Button = view.findViewById(R.id.btnSpremiP)

        btnSpremiP.setOnClickListener {
            val staraLozinka = staraLozinkaEditText.text.toString()
            val novaLozinka = novaLozinkaEditText.text.toString()
            val potvrdiNovuLozinku = potvrdiNovuLozinkuEditText.text.toString()

            if (staraLozinka.isEmpty() || novaLozinka.isEmpty() || potvrdiNovuLozinku.isEmpty()) {
                prikaziPoruku("Sva polja moraju biti popunjena.")
                return@setOnClickListener
            }

            if (novaLozinka.length < 6) {
                prikaziPoruku("Nova lozinka mora sadržavati najmanje 6 znakova.")
                return@setOnClickListener
            }

            if (novaLozinka != potvrdiNovuLozinku) {
                prikaziPoruku("Nova lozinka i potvrda lozinke se ne podudaraju.")
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
                if (korisnikID == null) {
                    prikaziPoruku("Nijedan korisnik nije prijavljen.")
                    return@launch
                }

                try {
                    val trenutniKorisnik = korisnikDataSource.dajKorisnikaPoID(korisnikID)
                    if (trenutniKorisnik == null || trenutniKorisnik.lozinka != staraLozinka) {
                        prikaziPoruku("Trenutna lozinka nije tačna.")
                        return@launch
                    }

                    korisnikDataSource.azurirajLozinku(
                        korisnikID = korisnikID,
                        novaLozinka = novaLozinka
                    )

                    prikaziPoruku("Lozinka uspješno promenjena!")
                    parentFragmentManager.popBackStack()
                } catch (e: Exception) {
                    prikaziPoruku("Greška pri promijeni lozinke: ${e.message}")
                }
            }
        }
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show()
    }
}
