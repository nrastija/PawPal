package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase


class OdabirPrijaveIliPregledaPsaFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var prijavipsagumb: Button
    private lateinit var pregledpsagumb: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.f02_odabir_prijave_ili_pregleda_psa,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prijavipsagumb = view.findViewById(R.id.prijavapsagumb)
        pregledpsagumb = view.findViewById(R.id.pregledpsagumb)

        prijavipsagumb.setOnClickListener {
            navigateToPrijavaIzgubljenihPasaFragment()
        }

        pregledpsagumb.setOnClickListener {
            navigateToPregledIzgubljenihPasaFragment()
        }
    }

    private fun navigateToPrijavaIzgubljenihPasaFragment() {
        val prijavaFragment = PrijavaIzgubljenihPasaFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, prijavaFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToPregledIzgubljenihPasaFragment() {
        val pregledFragment = PregledIzgubljenihPasaFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, pregledFragment)
            .addToBackStack(null)
            .commit()
    }
}