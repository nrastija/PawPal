package com.example.pawpal.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.session.KorisnikManager
import com.pawpal.appdatabase.AppDatabase
import com.example.pawpal.main.PawPalApplication
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import kotlinx.coroutines.launch

class UpravljanjeProfilomFragment : Fragment() {

    private lateinit var korisnikDataSource: KorisnikDataSource
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f01_upravljanje_profilom, container, false)

        database = (requireActivity().application as PawPalApplication).database
        korisnikDataSource = KorisnikDataSourceImpl(database)

        val btnOdjava: Button = view.findViewById(R.id.btnOdjava)
        val btnDeaktivacija: Button = view.findViewById(R.id.btnBrisanjeProfila)
        val btnUredivanje: Button = view.findViewById(R.id.btnUredivanjeProfila)

        btnOdjava.setOnClickListener { prikaziPotvrduOdjave() }
        btnDeaktivacija.setOnClickListener { prikaziPotvrduDeaktivacije() }
        btnUredivanje.setOnClickListener {
            val fragment = UredivanjeProfilaKorisnikaFragment()
            navigateToFragment(fragment)
        }

        return view
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

    private fun prikaziPotvrduOdjave() {
        AlertDialog.Builder(requireContext())
            .setTitle("Potvrda odjave")
            .setMessage("Jeste li sigurni da se želite odjaviti?")
            .setPositiveButton("Da") { _, _ -> odjaviKorisnika() }
            .setNegativeButton("Ne", null)
            .show()
    }

    private fun odjaviKorisnika() {
        KorisnikManager.odjava()
        val intent = Intent(requireContext(), PrijavaActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun prikaziPotvrduDeaktivacije() {
        AlertDialog.Builder(requireContext())
            .setTitle("Potvrda deaktivacije")
            .setMessage("Jeste li sigurni da želite deaktivirati svoj račun? Ova radnja je nepovratna.")
            .setPositiveButton("Da") { _, _ -> deaktivirajKorisnika() }
            .setNegativeButton("Ne", null)
            .show()
    }

    private fun deaktivirajKorisnika() {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()

        if (korisnikID == null) {
            return
        }

        lifecycleScope.launch {
            korisnikDataSource.dajKorisnikaPoID(korisnikID)?.let {
                korisnikDataSource.obrisiKorisnikaPoID(korisnikID)

                if (korisnikDataSource.dajKorisnikaPoID(korisnikID) == null) {
                    KorisnikManager.odjava()
                    startActivity(Intent(requireContext(), PrijavaActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

}
