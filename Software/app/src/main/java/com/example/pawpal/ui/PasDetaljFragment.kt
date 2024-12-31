package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.impl.KategorijaDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PasDetaljFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private var pasID: Long = 0

    companion object {
        const val ARG_PAS_ID = "pasID"
        fun newInstance(pasID: Long): PasDetaljFragment {
            val fragment = PasDetaljFragment()
            val args = Bundle()
            args.putLong(ARG_PAS_ID, pasID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pasID = it.getLong(ARG_PAS_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f09_detaljilayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slikaPsa: ImageView = view.findViewById(R.id.SlikaDetaljiPas)
        val imePsa: TextView = view.findViewById(R.id.ImeDetaljiPas)
        val opisPsa: TextView = view.findViewById(R.id.OpisDetaljiPas)
        val pasminaPsa: TextView = view.findViewById(R.id.Pasmina)
        val starostPsa: TextView = view.findViewById(R.id.StarostDetaljiPas)
        val zdravljePsa: TextView = view.findViewById(R.id.CjepivaDetaljiPas)
        val spolPsa: TextView = view.findViewById(R.id.SpolDetaljiPas)
        val datumRodjenjaPsa: TextView = view.findViewById(R.id.DatumRodenjaDetaljiPas)
        val kilazaPsa: TextView = view.findViewById(R.id.KilazaDetaljiPas)
        val dodatneInfoPsa: TextView = view.findViewById(R.id.DodatneInfoDetaljiPas)
        val gumbUdomi: Button = view.findViewById(R.id.adopt_button)

        lifecycleScope.launch {
            val pas = database.pasUdomljavanjeQueries.dohvatiPsaPoID(pasID).executeAsOne()
            imePsa.text = pas.ime
            opisPsa.text = pas.opis ?: "Nema opisa"
            pasminaPsa.text = "Pasmina: ${pas.pasmina}"
            starostPsa.text = "Starost: ${pas.dob} godina"
            zdravljePsa.text = "Cijepiva: ${pas.cijepiva}"
            spolPsa.text = "Spol: ${pas.spol}"
            datumRodjenjaPsa.text = "Datum rođenja: ${pas.datumRodenja}"
            kilazaPsa.text = "Kilaza: ${pas.kilaza} kg"
            dodatneInfoPsa.text = "Dodatne informacije: ${pas.dodatneinfo ?: "Nema dodatnih informacija"}"

            val slikaID = resources.getIdentifier(pas.imageUrl, "drawable", requireContext().packageName)
            slikaPsa.setImageResource(if (slikaID != 0) slikaID else android.R.drawable.ic_menu_report_image)
        }

        gumbUdomi.setOnClickListener {
            Toast.makeText(context, "Zahtjev za udomljavanje je poslan!", Toast.LENGTH_SHORT).show()
        }
    }
}

