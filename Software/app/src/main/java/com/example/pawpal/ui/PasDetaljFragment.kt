package com.example.pawpal.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
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
        return inflater.inflate(R.layout.f09_pas_detalji, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val slikaPsa1: ImageView = view.findViewById(R.id.PasDetaljiSlika1)
        val slikaPsa2: ImageView = view.findViewById(R.id.PasDetaljiSlika2)
        val slikaPsa3: ImageView = view.findViewById(R.id.PasDetaljiSlika3)
        val imePsa: TextView = view.findViewById(R.id.ImeDetaljiPas)
        val opisPsa: TextView = view.findViewById(R.id.OpisDetaljiPas)
        val pasminaPsa: TextView = view.findViewById(R.id.PasminaDetaljiPas)
        val starostPsa: TextView = view.findViewById(R.id.StarostDetaljiPas)
        val zdravljePsa: TextView = view.findViewById(R.id.CjepivaDetaljiPas)
        val spolPsa: TextView = view.findViewById(R.id.SpolDetaljiPas)
        val datumRodjenjaPsa: TextView = view.findViewById(R.id.DatumRodenjaDetaljiPas)
        val kilazaPsa: TextView = view.findViewById(R.id.KilazaDetaljiPas)
        val dodatneInfoPsa: TextView = view.findViewById(R.id.DodatneInfoDetaljiPas)
        val gumbUdomi: Button = view.findViewById(R.id.btnUsvojiMe)

        lifecycleScope.launch {
            val pas = database.pasUdomljavanjeQueries.dohvatiPsaPoID(pasID).executeAsOne()
            imePsa.text = pas.ime
            opisPsa.text = pas.opis ?: "Nema opisa"
            pasminaPsa.text = "Pasmina \n${pas.pasmina}"
            starostPsa.text = "Starost \n${pas.dob} godina"
            zdravljePsa.text = "Cijepiva: \n${pas.cijepiva}"
            spolPsa.text = "Spol \n${pas.spol}"
            datumRodjenjaPsa.text = "Datum rođenja \n${pas.datumRodenja}"
            kilazaPsa.text = "Kilaža \n${pas.kilaza} kg"
            dodatneInfoPsa.text = "Dodatne informacije: \n${pas.dodatneinfo ?: "Nema dodatnih informacija"}"

            setImage(slikaPsa1, pas.imageUrl)
            setImage(slikaPsa2, pas.imageUrl2)
            setImage(slikaPsa3, pas.imageUrl3)
        }

        gumbUdomi.setOnClickListener {
            val formFragment = ZahtjevUdomljavanjeFragment.newInstance(pasID)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, formFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun setImage(view: ImageView, imageName: String?) {
        if (imageName.isNullOrEmpty()) {
            view.setImageResource(android.R.drawable.ic_menu_report_image)
        } else {
            val bitmap = decodeBase64ToBitmap(imageName)
            view.setImageBitmap(bitmap)
        }
    }

    private fun decodeBase64ToBitmap(base64String: String?): Bitmap {
        return try {
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            BitmapFactory.decodeResource(resources, R.drawable.nophoto)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}

