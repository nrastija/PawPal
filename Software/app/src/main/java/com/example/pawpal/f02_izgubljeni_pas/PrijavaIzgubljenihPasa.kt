package com.example.pawpal.f02_izgubljeni_pas

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
import java.io.IOException
import java.io.InputStream

class PrijavaIzgubljenihPasa : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_prijava_izgubljenih_pasa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonPrilozi: Button = findViewById(R.id.prilozislikupsa)
        imageView = findViewById(R.id.imageView)
        ponisti = findViewById(R.id.ponisti)
        dodatniopispsa = findViewById(R.id.dodatniopispsa)
        zadnjeviden = findViewById(R.id.zadnjeviden)

        buttonPrilozi.setOnClickListener{
            openImageChooser()
        }

        ponisti.setOnClickListener{
            imageView.setImageURI(null)
            dodatniopispsa.text.clear()
            zadnjeviden.text.clear()
        }
    }


    private lateinit var imageView: ImageView
    private lateinit var ponisti: Button
    private lateinit var dodatniopispsa: EditText
    private lateinit var zadnjeviden: EditText

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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }
}