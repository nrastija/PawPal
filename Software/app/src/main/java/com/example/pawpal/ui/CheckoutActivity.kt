package com.example.pawpal.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.pawpal.R
import com.example.pawpal.main.BaseActivity
import com.example.pawpal.main.MainActivity
import com.example.pawpal.services.KosaricaManager
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException



class CheckoutActivity : BaseActivity() {

    private val clientId = "AduORiP6xE0YofgC2ady1_ppafkMzyEe8gatyYxNGHUMIXTQC1n86Gx3lAQ12R3pl4yvf9-ydzWhYQEz"
    private val clientSecret = "EBO92QqXn7wmX_cZKDvu-I_Kw_c3R8mTsdf1UjG3lPMur8nT-tM8kI0CXbsS6p3xHkHiP3RA469NAg3p"
    private val baseUrl = "https://api-m.sandbox.paypal.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_checkout)

        val radioGroupPlacanja: RadioGroup = findViewById(R.id.odabirPlacanja)
        val placanjeGotovinom: RadioButton = findViewById(R.id.placanjeGotovinom)
        val placanjeKarticom: RadioButton = findViewById(R.id.placanjeKartica)
        val placanjePayPal: RadioButton = findViewById(R.id.placanjePayPal)
        val sekcijaPodaciKartice: LinearLayout = findViewById(R.id.sekcijaPodaciKartice)

        val karticaVisa: RadioButton = findViewById(R.id.visaOption)
        val karticaBroj : EditText = findViewById(R.id.unosBrojKartice)
        val karticaDatum : EditText = findViewById(R.id.unosDatumIsteka)
        val karticaCVV : EditText = findViewById(R.id.unosCVV)

        placanjeGotovinom.isChecked = true
        karticaVisa.isChecked = true

        radioGroupPlacanja.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.placanjeKartica) {
                sekcijaPodaciKartice.visibility = View.VISIBLE
            } else {
                sekcijaPodaciKartice.visibility = View.GONE
            }
        }

        intent?.data?.let { data ->
            when (data.host) {
                "paypalpay" -> {
                    Toast.makeText(this, "Plaćanje uspješno!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        val btnPotvrda: Button = findViewById(R.id.btnPotvrdiPlacanje)

        btnPotvrda.setOnClickListener {
            if (placanjePayPal.isChecked){
                pokreniPlacanje()
                return@setOnClickListener
            }
            else if (placanjeKarticom.isChecked) {
                val broj = karticaBroj.text.toString().trim()
                val datum = karticaDatum.text.toString().trim()
                val cvv = karticaCVV.text.toString().trim()

                if (broj.isBlank() || datum.isBlank() || cvv.isBlank()) {
                    Toast.makeText(this, "Molimo popunite sva polja!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    Toast.makeText(this, "Plaćanje je u tijeku...", Toast.LENGTH_SHORT).show()

                }

            }
            else if (placanjeGotovinom.isChecked) {
                Toast.makeText(this, "Placanje gotovinom je odabrano.", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, "Placanje uspjesno izvrseno!", Toast.LENGTH_LONG).show()
            KosaricaManager.isprazniKosaricuLista()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pokreniPlacanje() {
        getAccessToken { accessToken ->
            if (accessToken != null) {
                kreirajNarudzbu(accessToken) { approvalUrl ->
                    if (approvalUrl != null) {
                        redirekcijaWeb(approvalUrl)
                    } else {
                        Toast.makeText(this, "Pogreska kod kreiranja Paypal access tokena", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Pogreska kod dohvacanja Paypal access tokena", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAccessToken(callback: (String?) -> Unit) {
        val client = OkHttpClient()
        val credentials = "$clientId:$clientSecret"
        val encodedCredentials = Base64.encodeToString(
            credentials.toByteArray(),
            Base64.NO_WRAP
        )

        val request = Request.Builder()
            .url("$baseUrl/v1/oauth2/token")
            .addHeader("Authorization", "Basic $encodedCredentials")
            .post(FormBody.Builder().add("grant_type", "client_credentials").build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(response.body?.string() ?: "")
                    callback(jsonResponse.getString("access_token"))
                } else {
                    callback(null)
                }
            }
        })
    }

    private fun kreirajNarudzbu(accessToken: String, callback: (String?) -> Unit) {
        val client = OkHttpClient()

        val requestBody = JSONObject().apply {
            put("intent", "CAPTURE")
            put("purchase_units", JSONArray().apply {
            put("application_context", JSONObject().apply {
                put("return_url", "com.example.pawpal://paypalpay")
            })
                put(JSONObject().apply {
                    put("amount", JSONObject().apply {
                        put("currency_code", "EUR")
                        put("value", KosaricaManager.izracunajCijenuLista().toString())
                    })
                })
            })
        }

        val request = Request.Builder()
            .url("$baseUrl/v2/checkout/orders")
            .addHeader("Authorization", "Bearer $accessToken")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(response.body?.string() ?: "")
                    val approvalUrl = jsonResponse
                        .getJSONArray("links")
                        .let { linksArray ->
                            (0 until linksArray.length())
                                .map { linksArray.getJSONObject(it) }
                                .firstOrNull { it.getString("rel") == "approve" }
                                ?.getString("href")
                        }
                    callback(approvalUrl)
                } else {
                    callback(null)
                }
            }
        })
    }

    private fun redirekcijaWeb(approvalUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(approvalUrl))
        startActivity(intent)
    }

}
