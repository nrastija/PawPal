package com.example.pawpal.f12_shop

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.example.pawpal.main.MainActivity

class CheckoutActivity : BaseActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_checkout)

        val radioGroupPlacanja: RadioGroup = findViewById(R.id.odabirPlacanja)
        val sekcijaPodaciKartice: LinearLayout = findViewById(R.id.sekcijaPodaciKartice)

        radioGroupPlacanja.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.placanjeKartica) {
                sekcijaPodaciKartice.visibility = View.VISIBLE
            } else {
                sekcijaPodaciKartice.visibility = View.GONE
            }
        }

        val btnPotvrda: Button = findViewById(R.id.btnPotvrdiPlacanje)
        btnPotvrda.setOnClickListener{
            Toast.makeText(this, "Narudžba uspješno napravljena!", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            }, 2000)
        }

    }
}