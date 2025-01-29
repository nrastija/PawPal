package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.SlikeZaUslugeAdapter
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class DodajUsluguFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var nazivUnos: EditText
    private lateinit var cijenUnos: EditText
    private lateinit var opisUnos: EditText
    private lateinit var trajanjeUnos: EditText
    private lateinit var imageUrlUnos: EditText
    private lateinit var btnSpremi: Button
    private var odabranaSlika: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (requireActivity().application as PawPalApplication).database
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f03_dodaj_uslugu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupImagePicker(view)
    }

    private fun setupViews(view: View) {
        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        nazivUnos = view.findViewById(R.id.noviNazivUsluge)
        cijenUnos = view.findViewById(R.id.novaCijenaUsluge)
        opisUnos = view.findViewById(R.id.noviOpisUsluge)
        trajanjeUnos = view.findViewById(R.id.novoTrajanjeUsluge)
        imageUrlUnos = view.findViewById(R.id.novaSlikaUsluge)
        btnSpremi = view.findViewById(R.id.btnSpremiUslugu)

        btnSpremi.setOnClickListener {
            spremiUslugu()
        }
    }

    private fun setupImagePicker(view: View) {
        val images = listOf(
            R.drawable.spa3,
            R.drawable.spa6,
            R.drawable.spa7,
            R.drawable.spa8,
            R.drawable.spa9,
            R.drawable.spa10,
        )
        val recyclerView = view.findViewById<RecyclerView>(R.id.slikaRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = SlikeZaUslugeAdapter(images) { resourceId ->
            odabranaSlika = resourceId
            imageUrlUnos.setText(resourceId.toString())
        }
    }

    private fun spremiUslugu() {
        val naziv = nazivUnos.text.toString()
        val cijena = cijenUnos.text.toString().toDoubleOrNull()
        val opis = opisUnos.text.toString()
        val trajanje = trajanjeUnos.text.toString().toLongOrNull()
        val imageUrl = odabranaSlika.toString()
        if (naziv.isBlank() || cijena == null || opis.isBlank() || trajanje == null || odabranaSlika == 0) {
            Toast.makeText(context, "Molimo popunite sva polja ispravno", Toast.LENGTH_SHORT).show()
            return
        }
        lifecycleScope.launch {
            database.uslugaQueries.insertUsluga(naziv, cijena, opis, trajanje, imageUrl)
            Toast.makeText(context, "Usluga uspješno dodana!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
