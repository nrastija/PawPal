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

class KreiranjeProfilaPsaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var pasDataSource: PasDataSource

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f11_layout_kreiranje_profila_psa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pasDataSource = PasDataSourceImpl(database)

        val btnKreirajProfil: Button = view.findViewById(R.id.btnKreirajProfil)
        btnKreirajProfil.setOnClickListener {
            val ime = view.findViewById<EditText>(R.id.Ime).text.toString()
            val dob = view.findViewById<EditText>(R.id.Dob).text.toString()
            val pasmina = view.findViewById<EditText>(R.id.Pasmina).text.toString()
            val spol = view.findViewById<EditText>(R.id.Spol).text.toString()
            val kilaza = view.findViewById<EditText>(R.id.Kilaza).text.toString()

            if (ime.isNotEmpty() && dob.isNotEmpty() && pasmina.isNotEmpty() && spol.isNotEmpty() && kilaza.isNotEmpty()) {
                lifecycleScope.launch {
                    val korisnikID = KorisnikManager.dajUlogiranogKorisnika() ?: return@launch
                    pasDataSource.insertPas(ime, dob, pasmina, spol, kilaza, korisnikID)
                    Toast.makeText(requireContext(), "Profil psa je uspješno kreiran!", Toast.LENGTH_SHORT).show()

                    navigateToFragment(PrikazProfilaPsaFragment())
                }
            } else {
                Toast.makeText(requireContext(), "Molimo ispunite sve podatke.", Toast.LENGTH_SHORT).show()
            }
        }
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
}
