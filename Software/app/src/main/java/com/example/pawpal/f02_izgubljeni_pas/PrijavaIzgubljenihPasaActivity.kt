package com.example.pawpal.f02_izgubljeni_pas

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pawpal.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class PrijavaIzgubljenihPasaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prijava_izgubljenih_pasa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(0, 0, 0, imeInsets.bottom)
            insets
        }
        imageView = findViewById(R.id.imageView)
        ponisti = findViewById(R.id.ponisti)
        dodatniopispsa = findViewById(R.id.dodatniopispsa)
        zadnjeviden = findViewById(R.id.zadnjeviden)
        potvrdiprijavu = findViewById(R.id.potvrdiprijavu)
        prilozisliku = findViewById(R.id.prilozislikupsa)
        imePsa = findViewById(R.id.imePsa)

        prilozisliku.setOnClickListener{
            openImageChooser()
        }

        ponisti.setOnClickListener{

            imageView.setImageURI(null)
            dodatniopispsa.text.clear()
            zadnjeviden.text.clear()
        }

        potvrdiprijavu.setOnClickListener{
            val opis = dodatniopispsa.text.toString()
            val lokacija = zadnjeviden.text.toString()
            val ime = imePsa.text.toString()
            val uri = imageView.tag as? Uri

            if(ime.isNotBlank() &&  opis.isNotBlank() && lokacija.isNotBlank() && uri !=null){
                val base64Image = convertImageToBase64(uri)
                val intent = Intent(this, PotvrdaPrijaveIzgubljenogPsaActivity::class.java).apply{
                    putExtra("ime", ime)
                    putExtra("opis", opis)
                    putExtra("lokacija", lokacija)
                    putExtra("slika", base64Image)
                }
                startActivity(intent)
            } else{
                showToast("Molimo ispunite sve podatke i priložite sliku.")
            }

        }
    }


    private lateinit var imageView: ImageView
    private lateinit var ponisti: Button
    private lateinit var dodatniopispsa: EditText
    private lateinit var zadnjeviden: EditText
    private lateinit var potvrdiprijavu: Button
    private lateinit var prilozisliku: Button
    private lateinit var imePsa: EditText

    private fun openImageChooser() {
        pickImageLauncher.launch("image/*")
    }


    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            checkImageSize(it)
        }
    }

    private fun loadImage(uri: Uri) {
        imageView.setImageURI(uri)
        imageView.tag = uri
    }

    private fun checkImageSize(uri: Uri) {
        try{
            val inputStream: InputStream = contentResolver.openInputStream(uri)!!
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            val maxSize = 5000
            if(options.outWidth > maxSize || options.outHeight > maxSize){
                showToast("Slika je pre velika! Odaberite manju sliku.")
            }
            else{
                loadImage(uri)
            }
        } catch (e: IOException){
            showToast("Greška prilikom učitavanja slike")
        }
    }

    private fun convertImageToBase64(uri: Uri): String? {
        val byteArray = convertImageToByteArray(uri)
        return byteArray?.let { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    private fun convertImageToByteArray(uri: Uri): ByteArray? {
        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

}