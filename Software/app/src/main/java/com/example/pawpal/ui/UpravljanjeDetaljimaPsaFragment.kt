package com.example.pawpal.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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

class UpravljanjeDetaljimaPsaFragment: Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private var pasId: Long = -1L

    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private var selectedImageView: ImageView? = null

    private var slika1: String? = null
    private var slika2: String? = null
    private var slika3: String? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageView?.setImageURI(it)
                val base64Image = convertImageToBase64(it)
                when (selectedImageView) {
                    imageView1 -> slika1 = base64Image
                    imageView2 -> slika2 = base64Image
                    imageView3 -> slika3 = base64Image
                }
            }
        }

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

        pasId = arguments?.getLong("pasId") ?: return

        imageView1 = view.findViewById(R.id.PasDetaljiSlika1)
        imageView2 = view.findViewById(R.id.PasDetaljiSlika2)
        imageView3 = view.findViewById(R.id.PasDetaljiSlika3)

        imageView1.setOnClickListener { selectImage(it as ImageView) }
        imageView2.setOnClickListener { selectImage(it as ImageView) }
        imageView3.setOnClickListener { selectImage(it as ImageView) }

        loadDogDetails(view)

        view.findViewById<Button>(R.id.btnAzurirajPas).setOnClickListener {
            saveDogDetails(view)
        }

        view.findViewById<Button>(R.id.btnObrisiPsa).setOnClickListener {
            potvrdabrisanja()
        }

        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun potvrdabrisanja() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Brisanje psa")
        builder.setMessage("Jeste li sigurni da želite obrisati ovog psa?")
        builder.setPositiveButton("Da") { _, _ ->
            brisanjepsa()
        }
        builder.setNegativeButton("Ne", null)
        builder.show()
    }

    private fun brisanjepsa() {
        lifecycleScope.launch {
            try {
                database.pasUdomljavanjeQueries.obrisipsaudomljavanje(pasId)
                Toast.makeText(context, "Pas obrisan!", Toast.LENGTH_SHORT).show()

                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Došlo je do greške pri brisanju psa.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage(imageView: ImageView) {
        selectedImageView = imageView
        pickImageLauncher.launch("image/*")
    }

    private fun loadDogDetails(view: View) {
        lifecycleScope.launch {
            val pas = database.pasUdomljavanjeQueries.dohvatiPsaPoID(pasId).executeAsOneOrNull()
            pas?.let {
                view.findViewById<EditText>(R.id.ImeDetaljiPas).setText(it.ime)
                view.findViewById<EditText>(R.id.StarostDetaljiPas).setText(it.dob.toString())
                view.findViewById<EditText>(R.id.KilazaDetaljiPas).setText(it.kilaza.toString())
                view.findViewById<EditText>(R.id.SpolDetaljiPas).setText(it.spol)
                view.findViewById<EditText>(R.id.PasminaDetaljiPas).setText(it.pasmina)
                view.findViewById<EditText>(R.id.OpisDetaljiPas).setText(it.opis)
                view.findViewById<EditText>(R.id.CjepivaDetaljiPas).setText(it.cijepiva)
                view.findViewById<EditText>(R.id.DodatneInfoDetaljiPas).setText(it.dodatneinfo)
                view.findViewById<EditText>(R.id.DatumRodenjaDetaljiPas).setText(it.datumRodenja)


                slika1 = it.imageUrl
                slika2 = it.imageUrl2
                slika3 = it.imageUrl3

                if (!slika1.isNullOrEmpty()) imageView1.setImageBitmap(decodeBase64ToBitmap(slika1))
                if (!slika2.isNullOrEmpty()) imageView2.setImageBitmap(decodeBase64ToBitmap(slika2))
                if (!slika3.isNullOrEmpty()) imageView3.setImageBitmap(decodeBase64ToBitmap(slika3))
            }
        }
    }

    private fun saveDogDetails(view: View) {
        val ime = view.findViewById<EditText>(R.id.ImeDetaljiPas).text.toString()
        val starost = view.findViewById<EditText>(R.id.StarostDetaljiPas).text.toString().toLongOrNull() ?: 0L
        val kilaza = view.findViewById<EditText>(R.id.KilazaDetaljiPas).text.toString().toDoubleOrNull() ?: 0.0
        val spol = view.findViewById<EditText>(R.id.SpolDetaljiPas).text.toString()
        val pasmina = view.findViewById<EditText>(R.id.PasminaDetaljiPas).text.toString()
        val opis = view.findViewById<EditText>(R.id.OpisDetaljiPas).text.toString()
        val cijepivo = view.findViewById<EditText>(R.id.CjepivaDetaljiPas).text.toString()
        val dodatneInfo = view.findViewById<EditText>(R.id.DodatneInfoDetaljiPas).text.toString()
        val datumRodenja = view.findViewById<EditText>(R.id.DatumRodenjaDetaljiPas).text.toString()

        val pasIdLong = pasId

        val slika1Base64 = slika1 ?: ""
        val slika2Base64 = slika2 ?: ""
        val slika3Base64 = slika3 ?: ""

        lifecycleScope.launch {
            try {
                database.pasUdomljavanjeQueries.azurirajpsaudomljavanje(
                    ime, starost, spol,opis, datumRodenja, kilaza, pasmina,
                    dodatneInfo, cijepivo, slika1Base64, slika2Base64, slika3Base64, pasIdLong
                )
                Toast.makeText(context, "Podaci spremljeni!", Toast.LENGTH_SHORT).show()

                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Došlo je do greške pri spremanju podataka.", Toast.LENGTH_SHORT).show()
            }
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

    private fun convertImageToBase64(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}