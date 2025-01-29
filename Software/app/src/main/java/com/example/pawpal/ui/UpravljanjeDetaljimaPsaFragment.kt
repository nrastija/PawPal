package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase

class UpravljanjeDetaljimaPsaFragment: Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (requireActivity().application as PawPalApplication).database
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f06_upravljanje_udomljavanjem_detalji_psa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pasId = arguments?.getLong("pasId") ?: return

        val ime = arguments?.getString("ime") ?: ""
        val starost = arguments?.getString("starost") ?: ""
        val kilaza = arguments?.getString("kilaza") ?: ""
        val spol = arguments?.getString("spol") ?: ""
        val pasmina = arguments?.getString("pasmina") ?: ""
        val datumRodenja = arguments?.getString("datumRodenja") ?: ""
        val opis = arguments?.getString("opis") ?: ""
        val cjepiva = arguments?.getString("cjepiva") ?: ""
        val dodatneInfo = arguments?.getString("dodatneInfo") ?: ""

        view.findViewById<EditText>(R.id.ImeDetaljiPas).setText(ime)
        view.findViewById<EditText>(R.id.StarostDetaljiPas).setText(starost)
        view.findViewById<EditText>(R.id.KilazaDetaljiPas).setText(kilaza)
        view.findViewById<EditText>(R.id.SpolDetaljiPas).setText(spol)
        view.findViewById<EditText>(R.id.PasminaDetaljiPas).setText(pasmina)
        view.findViewById<EditText>(R.id.DatumRodenjaDetaljiPas).setText(datumRodenja)
        view.findViewById<EditText>(R.id.OpisDetaljiPas).setText(opis)
        view.findViewById<EditText>(R.id.CjepivaDetaljiPas).setText(cjepiva)
        view.findViewById<EditText>(R.id.DodatneInfoDetaljiPas).setText(dodatneInfo)
    }

}