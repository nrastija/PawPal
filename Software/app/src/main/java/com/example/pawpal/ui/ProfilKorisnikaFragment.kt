package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.datasource.PasDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.data.impl.PasDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class ProfilKorisnikaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var korisnikDataSource: KorisnikDataSource
    private lateinit var pasDataSource: PasDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f11_layout_profil_korisnika, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        korisnikDataSource = KorisnikDataSourceImpl(database)
        pasDataSource = PasDataSourceImpl(database)

        postaviPodatkeKorisnika(view)

        view.findViewById<Button>(R.id.btnMojLjubimac).setOnClickListener {
            val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
            if (korisnikID != null) {
                lifecycleScope.launch {
                    val pas = pasDataSource.dohvatiPasPoKorisnikID(korisnikID)
                    if (pas != null) {
                        navigateToFragment(PrikazProfilaPsaFragment())
                    } else {
                        navigateToFragment(KreiranjeProfilaPsaFragment())
                    }
                }
            } else {
                prikaziPoruku("Nijedan korisnik nije prijavljen.")
            }
        }

        view.findViewById<Button>(R.id.btnUrediPodatkeK).setOnClickListener {
            navigateToFragment(UredivanjeProfilaKorisnikaFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        view?.let { postaviPodatkeKorisnika(it) }
    }

    private fun postaviPodatkeKorisnika(view: View) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID != null) {
            lifecycleScope.launch {
                val korisnik = korisnikDataSource.dajKorisnikaPoID(korisnikID)
                if (korisnik != null) {
                    prikaziPodatke(view, korisnik)
                } else {
                    prikaziPoruku("Korisnik nije pronađen.")
                }
            }
        } else {
            prikaziPoruku("Nijedan korisnik nije prijavljen.")
        }
    }

    private fun prikaziPodatke(view: View, korisnik: appdatabase.Korisnik) {
        view.findViewById<TextView>(R.id.podatakIme).text = korisnik.ime
        view.findViewById<TextView>(R.id.podatakPrezime).text = korisnik.prezime
        view.findViewById<TextView>(R.id.podatakMail).text = korisnik.email
        view.findViewById<TextView>(R.id.podatakKorIme).text = korisnik.korime
    }

    private fun navigateToFragment(fragment: Fragment) {
        if (fragment is DatabaseConsumer) {
            fragment.database = database
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show()
    }
}
