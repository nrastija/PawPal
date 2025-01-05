package com.example.pawpal.ui

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class PrijavaIzgubljenihPasaFragment: Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var imageView: ImageView
    private lateinit var ponisti: Button
    private lateinit var dodatniopispsa: EditText
    private lateinit var zadnjeviden: EditText
    private lateinit var potvrdiprijavu: Button
    private lateinit var prilozisliku: Button
    private lateinit var imePsa: EditText

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            checkImageSize(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prijava_izgubljenih_pasa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(0, 0, 0, imeInsets.bottom)
            insets
        }

        imageView = view.findViewById(R.id.imageView)
        ponisti = view.findViewById(R.id.ponisti)
        dodatniopispsa = view.findViewById(R.id.dodatniopispsa)
        zadnjeviden = view.findViewById(R.id.zadnjeviden)
        potvrdiprijavu = view.findViewById(R.id.potvrdiprijavu)
        prilozisliku = view.findViewById(R.id.prilozislikupsa)
        imePsa = view.findViewById(R.id.imePsa)

        prilozisliku.setOnClickListener {
            openImageChooser()
        }

        ponisti.setOnClickListener {
            imageView.setImageURI(null)
            dodatniopispsa.text.clear()
            zadnjeviden.text.clear()
            imePsa.text.clear()
        }

        potvrdiprijavu.setOnClickListener {
            val opis = dodatniopispsa.text.toString()
            val lokacija = zadnjeviden.text.toString()
            val ime = imePsa.text.toString()
            val uri = imageView.tag as? Uri

            if (ime.isNotBlank() && opis.isNotBlank() && lokacija.isNotBlank() && uri != null) {
                val base64Image = convertImageToBase64(uri)
                val fragment = PotvrdaPrijaveIzgubljenogPsaFragment().apply {
                    arguments = Bundle().apply {
                        putString("ime", ime)
                        putString("opis", opis)
                        putString("lokacija", lokacija)
                        putString("slika", base64Image)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                showToast("Molimo ispunite sve podatke i priložite sliku.")
            }
        }
    }

    private fun openImageChooser() {
        pickImageLauncher.launch("image/*")
    }

    private fun loadImage(uri: Uri) {
        imageView.setImageURI(uri)
        imageView.tag = uri
    }

    private fun checkImageSize(uri: Uri) {
        try {
            val inputStream: InputStream = requireContext().contentResolver.openInputStream(uri)!!
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            val maxSize = 5000
            if (options.outWidth > maxSize || options.outHeight > maxSize) {
                showToast("Slika je pre velika! Odaberite manju sliku.")
            } else {
                loadImage(uri)
            }
        } catch (e: IOException) {
            showToast("Greška prilikom učitavanja slike")
        }
    }

    private fun convertImageToBase64(uri: Uri): String? {
        val byteArray = convertImageToByteArray(uri)
        return byteArray?.let { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    private fun convertImageToByteArray(uri: Uri): ByteArray? {
        val inputStream: InputStream = requireContext().contentResolver.openInputStream(uri) ?: return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        inputStream.close()
        return byteArrayOutputStream.toByteArray()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}