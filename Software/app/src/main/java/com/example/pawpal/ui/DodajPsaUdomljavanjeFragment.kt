package com.example.pawpal.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class DodajPsaUdomljavanjeFragment: Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var imePsa: EditText
    private lateinit var starostPas: EditText
    private lateinit var kilazaPas: EditText
    private lateinit var spolPas: EditText
    private lateinit var pasminaPas: EditText
    private lateinit var datumRodjenjaPas: EditText
    private lateinit var opisPas: EditText
    private lateinit var cjepivaPas: EditText
    private lateinit var dodatneInfoPas: EditText
    private lateinit var btnDodajPsa: Button
    private lateinit var slike: ArrayList<Uri>

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView

    private val pickMultipleImagesLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.size <= 3) {
            slike.clear()
            slike.addAll(uris)
            if (slike.size > 0) imageView1.setImageURI(slike[0])
            if (slike.size > 1) imageView2.setImageURI(slike[1])
            if (slike.size > 2) imageView3.setImageURI(slike[2])
        }
        else{
            Toast.makeText(context, "Možete odabrati najviše 3 slike", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = (requireActivity().application as PawPalApplication).database
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.f06_upravljanje_udomljavanjem_dodavanje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DodajPsa", "View: $view")
        setupViews(view)

    }

    private fun setupViews(view: View) {
        imePsa = view.findViewById(R.id.ImeDetaljiPas)
        starostPas = view.findViewById(R.id.StarostDetaljiPas)
        kilazaPas = view.findViewById(R.id.KilazaDetaljiPas)
        spolPas = view.findViewById(R.id.SpolDetaljiPas)
        pasminaPas = view.findViewById(R.id.PasminaDetaljiPas)
        datumRodjenjaPas = view.findViewById(R.id.DatumRodenjaDetaljiPas)
        opisPas = view.findViewById(R.id.OpisDetaljiPas)
        cjepivaPas = view.findViewById(R.id.CjepivaDetaljiPas)
        dodatneInfoPas = view.findViewById(R.id.DodatneInfoDetaljiPas)
        btnDodajPsa = view.findViewById(R.id.btnDodajPas)

        imageView1 = view.findViewById(R.id.PasDetaljiSlika1)
        imageView2 = view.findViewById(R.id.PasDetaljiSlika2)
        imageView3 = view.findViewById(R.id.PasDetaljiSlika3)

        slike = ArrayList()

        imageView1.setOnClickListener{openImageChooser()}
        imageView2.setOnClickListener{openImageChooser()}
        imageView3.setOnClickListener{openImageChooser()}

        btnDodajPsa.setOnClickListener {
            dodajPsa()
        }
    }

    private fun openImageChooser() {
        pickMultipleImagesLauncher.launch("image/*")
    }

    private fun dodajPsa() {
        val ime = imePsa.text.toString()
        val starost = starostPas.text.toString()
        val kilaza = kilazaPas.text.toString()
        val spol = spolPas.text.toString()
        val pasmina = pasminaPas.text.toString()
        val datumRodjenja = datumRodjenjaPas.text.toString()
        val opis = opisPas.text.toString()
        val cjepiva = cjepivaPas.text.toString()
        val dodatneInfo = dodatneInfoPas.text.toString()

        if (ime.isEmpty() || starost.isEmpty() || kilaza.isEmpty() || spol.isEmpty() || pasmina.isEmpty() || datumRodjenja.isEmpty() || opis.isEmpty() || cjepiva.isEmpty() || dodatneInfo.isEmpty() || slike.isEmpty()) {
            Toast.makeText(context, "Molimo popunite sva polja i odaberite slike", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val base64Images = slike.map { convertImageToBase64(it) }

                if (base64Images.isEmpty()) {
                    Toast.makeText(context, "Nema slika za pohranu", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val dob = starost.toLongOrNull() ?: 0L
                val kilazaVal = kilaza.toDoubleOrNull() ?: 0.0

                val imageUrl = base64Images.getOrNull(0) ?: ""
                val imageUrl2 = base64Images.getOrNull(1) ?: ""
                val imageUrl3 = base64Images.getOrNull(2) ?: ""

                database.pasUdomljavanjeQueries.dodajPasUdomljavanje(
                    ime = ime,
                    dob = dob,
                    kilaza = kilazaVal,
                    spol = spol,
                    pasmina = pasmina,
                    datumRodenja = datumRodjenja,
                    opis = opis,
                    cijepiva = cjepiva,
                    dodatneinfo = dodatneInfo,
                    imageUrl = imageUrl,
                    imageUrl2 = imageUrl2,
                    imageUrl3 = imageUrl3
                )


                Toast.makeText(context, "Pas uspješno dodan!", Toast.LENGTH_SHORT).show()

                parentFragmentManager.popBackStack()

            } catch (e: Exception) {
                Toast.makeText(context, "Greška: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun convertImageToBase64(uri: Uri): String {
        val byteArray = convertImageToByteArray(uri)
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }

    private fun convertImageToByteArray(uri: Uri): ByteArray {
        val inputStream: InputStream = requireContext().contentResolver.openInputStream(uri) ?: throw IOException("Failed to open InputStream")
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        inputStream.close()
        return byteArrayOutputStream.toByteArray()
    }

}