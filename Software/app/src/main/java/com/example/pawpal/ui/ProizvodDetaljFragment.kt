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
import com.example.pawpal.R
import com.example.pawpal.f12_shop.entiteti.Proizvod
import com.example.pawpal.services.KosaricaManager

class ProizvodDetaljFragment : Fragment() {

    private var proizvodID: Int = 0
    private var naziv: String? = null
    private var cijena: Double = 0.0
    private var opis: String? = null
    private var kategorijaID: Int = 0
    private var imageUrl: String? = null

    companion object {
        const val ARG_PROIZVOD_ID = "sifraProizvoda"
        const val ARG_NAZIV = "nazivProizvoda"
        const val ARG_CIJENA = "cijenaProizvoda"
        const val ARG_OPIS = "opisProizvoda"
        const val ARG_KATEGORIJA_ID = "kategorijaProizvoda"
        const val ARG_IMAGE_URL = "imageUrl"

        fun newInstance(
            proizvodID: Long,
            naziv: String,
            cijena: Double,
            opis: String?,
            kategorijaID: Long,
            imageUrl: String?
        ): ProizvodDetaljFragment {
            val fragment = ProizvodDetaljFragment()
            val args = Bundle()
            //args.putInt(ARG_PROIZVOD_ID, proizvodID)
            args.putString(ARG_NAZIV, naziv)
            args.putDouble(ARG_CIJENA, cijena)
            args.putString(ARG_OPIS, opis)
            //args.putInt(ARG_KATEGORIJA_ID, kategorijaID)
            args.putString(ARG_IMAGE_URL, imageUrl)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            proizvodID = it.getInt(ARG_PROIZVOD_ID)
            naziv = it.getString(ARG_NAZIV)
            cijena = it.getDouble(ARG_CIJENA)
            opis = it.getString(ARG_OPIS)
            kategorijaID = it.getInt(ARG_KATEGORIJA_ID)
            imageUrl = it.getString(ARG_IMAGE_URL)
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

        nazivProizvoda.text = naziv
        cijenaProizvoda.text = "Cijena: $cijena €"
        opisProizvoda.text = opis
        kategorijaProizvoda.text = "Kategorija: $kategorijaID"

        val slikaID = resources.getIdentifier(imageUrl, "drawable", requireContext().packageName)
        if (slikaID != 0) {
            slikaProizvoda.setImageResource(slikaID)
        } else {
            slikaProizvoda.setImageResource(android.R.drawable.ic_menu_report_image)
        }

        // Adapter for spinner
        val kolicinaList = listOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, kolicinaList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKolicina.adapter = adapter

        gumbDodajUKosaricu.setOnClickListener {
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

        }
    }
}
