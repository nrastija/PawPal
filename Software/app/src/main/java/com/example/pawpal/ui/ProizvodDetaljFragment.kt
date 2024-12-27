package com.example.pawpal.ui

import android.os.Bundle
import android.util.Log
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
import com.example.pawpal.data.KategorijaDataSourceImpl
import com.example.pawpal.f12_shop.entiteti.Proizvod
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.services.KosaricaManager
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class ProizvodDetaljFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private var proizvodID: Long = 0

    companion object {
        const val ARG_PROIZVOD_ID = "sifraProizvoda"

        fun newInstance(proizvodID: Long): ProizvodDetaljFragment {
            val fragment = ProizvodDetaljFragment()
            val args = Bundle()
            args.putLong(ARG_PROIZVOD_ID, proizvodID)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            proizvodID = it.getLong(ARG_PROIZVOD_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f12_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slikaProizvoda: ImageView = view.findViewById(R.id.slikaProizvoda)
        val nazivProizvoda: TextView = view.findViewById(R.id.nazivProizvoda)
        val cijenaProizvoda: TextView = view.findViewById(R.id.cijenaProizvoda)
        val kategorijaProizvoda: TextView = view.findViewById(R.id.kategorijaProizvoda)
        val opisProizvoda: TextView = view.findViewById(R.id.opisProizvoda)
        val spinnerKolicina: Spinner = view.findViewById(R.id.odabirKolicineSpinner)
        val gumbDodajUKosaricu: Button = view.findViewById(R.id.dodajProizvodUKosaricu)

        lifecycleScope.launch {
            val proizvod = database.proizvodQueries.dohvatiProizvodPoId(proizvodID).executeAsOne()

            nazivProizvoda.text = proizvod.naziv
            cijenaProizvoda.text = "Cijena: ${proizvod.cijena} €"
            opisProizvoda.text = proizvod.opis

            val kategorijaDataSource = KategorijaDataSourceImpl(database)
            val kategorijaNaziv = kategorijaDataSource.dohvatiNazivPoId(proizvod.kategorijaId)
            kategorijaProizvoda.text = "Kategorija: $kategorijaNaziv"

            val slikaID = resources.getIdentifier(proizvod.imageUrl, "drawable", requireContext().packageName)
            slikaProizvoda.setImageResource(if (slikaID != 0) slikaID else android.R.drawable.ic_menu_report_image)
        }

        // Adapter for spinner
        val kolicinaList = listOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kolicinaList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKolicina.adapter = adapter

        /*gumbDodajUKosaricu.setOnClickListener {
            val proizvod = Proizvod(
                proizvodID = proizvodID,
                naziv = naziv ?: "",
                cijena = cijena,
                opis = opis ?: "",
                kategorijaID = kategorijaID,
                kolicina = spinnerKolicina.selectedItem.toString().toInt(),
                imageUrl = imageUrl
            )

            Toast.makeText(requireContext(), "Dodan ${naziv} u košaricu!", Toast.LENGTH_SHORT).show()
            KosaricaManager.dodajProizvodLista(proizvod)

        }*/
    }
}
