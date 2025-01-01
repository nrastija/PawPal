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
import com.example.pawpal.data.impl.VoditeljDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class SkolaDetaljiFragment : Fragment(), DatabaseConsumer {
    private var skolaId: Long? = null
    override lateinit var database: AppDatabase
    private var prviVoditeljId: Long? = null

    companion object {
        private const val ARG_SKOLA_ID = "skolaId"

        fun newInstance(skolaId: Long): SkolaDetaljiFragment {
            return SkolaDetaljiFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_SKOLA_ID, skolaId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skolaId = arguments?.getLong(ARG_SKOLA_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f07_detalji_skole, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        skolaId?.let { id ->
            val skola = database.skolaQueries.dohvatiSkoluPoId(id).executeAsOneOrNull()
            skola?.let { prikaziPodatke(view, it) }
        }

        val gumbDetaljiVoditelja: Button = view.findViewById(R.id.btnDetaljiVoditelja)
        gumbDetaljiVoditelja.setOnClickListener {
            if (prviVoditeljId != null) {

                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        VoditeljDetaljiFragment.newInstance(prviVoditeljId!!)
                    )
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Nema dostupnih podataka za voditelja", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun prikaziPodatke(view: View, skola: appdatabase.Skola) {
        view.findViewById<TextView>(R.id.podatakNaziv).text = skola.naziv
        view.findViewById<TextView>(R.id.podatakOpis).text = skola.opis
        view.findViewById<TextView>(R.id.podatakCijena).text = "${skola.cijena} €"
        view.findViewById<TextView>(R.id.podatakTermin).text = skola.termin

        val voditeljDataSource = VoditeljDataSourceImpl(database)

        lifecycleScope.launch {
            val voditelji = voditeljDataSource.dohvatiVoditeljeZaSkolu(skola.skolaID)
            val imenaVoditelja = voditelji.joinToString(", ") { "${it.ime} ${it.prezime}" }
            view.findViewById<TextView>(R.id.podatakVoditelj).text = imenaVoditelja
        }
    }


}
