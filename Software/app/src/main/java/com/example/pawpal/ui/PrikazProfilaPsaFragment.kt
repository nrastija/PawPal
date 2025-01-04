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
import com.example.pawpal.data.datasource.PasDataSource
import com.example.pawpal.data.impl.PasDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PrikazProfilaPsaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var pasDataSource: PasDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f11_layout_prikaz_profila_psa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pasDataSource = PasDataSourceImpl(database)
        prikaziPodatkePsa(view)

        view.findViewById<Button>(R.id.btnObrisiPsa).setOnClickListener {
            obrisiPsa()
        }

        view.findViewById<Button>(R.id.btnUrediPodatkeP).setOnClickListener {
            val fragment = UredivanjeProfilaPsaFragment()
            if (fragment is DatabaseConsumer) {
                fragment.database = database
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun prikaziPodatkePsa(view: View) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID != null) {
            lifecycleScope.launch {
                val pas = pasDataSource.dohvatiPasPoKorisnikID(korisnikID)
                if (pas != null) {
                    prikaziPodatke(view, pas)
                } else {
                    prikaziPoruku("Profil psa nije pronađen.")
                }
            }
        } else {
            prikaziPoruku("Korisnik nije prijavljen.")
        }
    }

    private fun prikaziPodatke(view: View, pas: appdatabase.Pas) {
        view.findViewById<TextView>(R.id.podatakIme).text = pas.ime
        view.findViewById<TextView>(R.id.podatakDob).text = pas.dob
        view.findViewById<TextView>(R.id.podatakPasmina).text = pas.pasmina
        view.findViewById<TextView>(R.id.podatakSpol).text = pas.spol
        view.findViewById<TextView>(R.id.podatakKilaza).text = pas.kilaza
    }

    private fun obrisiPsa() {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID == null) {
            prikaziPoruku("Korisnik nije prijavljen.")
            return
        }

        lifecycleScope.launch {
            try {
                pasDataSource.obrisiPasPoKorisnikID(korisnikID)
                prikaziPoruku("Profil psa uspješno obrisan.")

                val fragment = ProfilKorisnikaFragment()
                if (fragment is DatabaseConsumer) {
                    fragment.database = database
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()

            } catch (e: Exception) {
                prikaziPoruku("Greška pri brisanju profila psa: ${e.message}")
            }
        }
    }

    private fun prikaziPoruku(poruka: String) {
        Toast.makeText(requireContext(), poruka, Toast.LENGTH_SHORT).show()
    }
}
