package com.example.pawpal.ui

import NotificationHelper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pawpal.R
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.MainActivity
import com.example.pawpal.main.PawPalApplication
import com.pawpal.appdatabase.AppDatabase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CheckoutActivity : AppCompatActivity()  {

    private val clientId = "AduORiP6xE0YofgC2ady1_ppafkMzyEe8gatyYxNGHUMIXTQC1n86Gx3lAQ12R3pl4yvf9-ydzWhYQEz"
    private val clientSecret = "EBO92QqXn7wmX_cZKDvu-I_Kw_c3R8mTsdf1UjG3lPMur8nT-tM8kI0CXbsS6p3xHkHiP3RA469NAg3p"
    private val baseUrl = "https://api-m.sandbox.paypal.com"
    lateinit var database: AppDatabase
    var kosaricaID: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.f12_checkout)

        val notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel(
            channelId = "checkout_notifications",
            channelName = "Checkout Notifications"
        )

        kosaricaID = intent.getLongExtra("KOSARICA_ID", -1L)

        val radioGroupPlacanja: RadioGroup = findViewById(R.id.odabirPlacanja)
        val placanjeGotovinom: RadioButton = findViewById(R.id.placanjeGotovinom)
        val placanjeKarticom: RadioButton = findViewById(R.id.placanjeKartica)
        val placanjePayPal: RadioButton = findViewById(R.id.placanjePayPal)
        val sekcijaPodaciKartice: LinearLayout = findViewById(R.id.sekcijaPodaciKartice)

        val karticaVisa: RadioButton = findViewById(R.id.visaOption)
        val karticaMastercard: RadioButton = findViewById(R.id.mastercardOption)
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

        val logiranKorisnikID = KorisnikManager.dajUlogiranogKorisnika()

        database = (application as PawPalApplication).database

        val kosarica = database.kosaricaQueries.provjeriPostojanje(logiranKorisnikID).executeAsOneOrNull()

        btnPotvrda.setOnClickListener {
            if (placanjePayPal.isChecked){
                pokreniPlacanje()
                unosNarudzbe("Paypal")

                val narudzbaId = database.narudzbaQueries.zadnjaNarudzbaId().executeAsOne()
                val ukupnaCijenaNarudzbe = database.narudzbaQueries.dohvatiNarudzbu(narudzbaId).executeAsOneOrNull()?.ukupnaCijena

                notificationHelper.sendNotification(
                    channelId = "checkout_notifications",
                    notificationId = narudzbaId.toInt(),
                    naslov = "Narudžba u transakciji!",
                    opis = "Vaša narudžba ${narudzbaId.toInt()} je trenutno u transakciji...",
                    priority = NotificationHelper.Priority.HIGH
                )
                notificationHelper.sendBigStyleNotification(
                    channelId = "checkout_notifications",
                    notificationId = narudzbaId.toInt()+1,
                    naslov = "Narudžba uspješna!",
                    opis = "Vaša narudžba ${narudzbaId.toInt()} u iznosu od $ukupnaCijenaNarudzbe € je uspješno kreirana. Način plaćanja: PayPal.",
                    priority = NotificationHelper.Priority.MEDIUM
                )
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

                    unosNarudzbe("Kartica")

                    val narudzbaId = database.narudzbaQueries.zadnjaNarudzbaId().executeAsOne()
                    val ukupnaCijenaNarudzbe = database.narudzbaQueries.dohvatiNarudzbu(narudzbaId).executeAsOneOrNull()?.ukupnaCijena


                    notificationHelper.sendNotification(
                        channelId = "checkout_notifications",
                        notificationId = narudzbaId.toInt(),
                        naslov = "Narudžba u transakciji!",
                        opis = "Narudžba sa šifrom ${narudzbaId.toInt()} je trenutno u transakciji.",
                        priority = NotificationHelper.Priority.HIGH
                    )

                    if (karticaVisa.isChecked){
                        notificationHelper.sendBigStyleNotification(
                            channelId = "checkout_notifications",
                            notificationId = narudzbaId.toInt()+1,
                            naslov = "Narudžba uspješna!",
                            opis = "Narudžba sa šifrom ${narudzbaId.toInt()} u iznosu od $ukupnaCijenaNarudzbe € je uspješno izvršena. Način plačanja: VISA Kartica (${broj}).",
                            priority = NotificationHelper.Priority.LOW
                        )
                    }
                    else if (karticaMastercard.isChecked){
                        notificationHelper.sendBigStyleNotification(
                            channelId = "checkout_notifications",
                            notificationId = narudzbaId.toInt()+1,
                            naslov = "Narudžba uspješna!",
                            opis = "Narudžba sa šifrom ${narudzbaId.toInt()} u iznosu od $ukupnaCijenaNarudzbe € je uspješno izvršena. Način plačanja: Mastercard Kartica (${broj}).",
                            priority = NotificationHelper.Priority.LOW
                        )
                    }
                }

            }
            else if (placanjeGotovinom.isChecked) {
                Toast.makeText(this, "Placanje gotovinom je odabrano.", Toast.LENGTH_SHORT).show()
                unosNarudzbe("Gotovina")

                val narudzbaId = database.narudzbaQueries.zadnjaNarudzbaId().executeAsOne()
                val dohvacenaNarudzba = database.narudzbaQueries.dohvatiNarudzbu(narudzbaId).executeAsOneOrNull()
                val ukupnaCijenaNarudzbe = dohvacenaNarudzba?.ukupnaCijena

                notificationHelper.sendBigStyleNotification(
                    channelId = "checkout_notifications",
                    notificationId = narudzbaId.toInt()+1,
                    naslov = "Narudžba uspješna!",
                    opis = "Vaša narudžba ${narudzbaId.toInt()} u iznosu od $ukupnaCijenaNarudzbe € je uspješno kreirana. Način plaćanja: Gotovina.",
                    priority = NotificationHelper.Priority.MEDIUM
                )
            }

            Toast.makeText(this, "Placanje uspjesno izvrseno!", Toast.LENGTH_LONG).show()

            if (kosarica != null) {
                database.kosaricaProizvodQueries.brisanjeKosarice(kosarica.kosaricaID)
            }

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
        val ukupnaCijena = database.kosaricaProizvodQueries.dohvatiUkupnuCijenuZaKosaricu(kosaricaID).executeAsOneOrNull()?.SUM ?: 0.0
        val zaokruzenaCijena = BigDecimal(ukupnaCijena).setScale(2, RoundingMode.HALF_UP).toDouble()

        val requestBody = JSONObject().apply {
            put("intent", "CAPTURE")
            put("application_context", JSONObject().apply {
                put("return_url", "com.example.pawpal://paypalpay")
                put("cancel_url", "com.example.pawpal://paypalcancel")
            })
            put("purchase_units", JSONArray().apply {
                put(JSONObject().apply {
                    put("amount", JSONObject().apply {
                        put("currency_code", "EUR")
                        put("value", zaokruzenaCijena ?: "0.00")
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

    private fun unosNarudzbe(nacinPlacanja: String) {
        val ukupnaCijena = database.kosaricaProizvodQueries
            .dohvatiUkupnuCijenuZaKosaricu(kosaricaID)
            .executeAsOneOrNull()?.SUM ?: 0.0

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val datumNarudzbe = dateFormat.format(Date())

        val statusNarudzbe = "Uspješna"

        val logiranKorisnikID = KorisnikManager.dajUlogiranogKorisnika()

        if (logiranKorisnikID != null) {
            database.narudzbaQueries.insertNarudzba(
                korisnikId = logiranKorisnikID,
                ukupnaCijena = ukupnaCijena,
                datum = datumNarudzbe,
                status = statusNarudzbe,
                nacinPlacanja = nacinPlacanja
            )
        }

        val narudzbaId = database.narudzbaQueries.zadnjaNarudzbaId().executeAsOne()

        val proizvodiUKosarici = database.kosaricaProizvodQueries
            .dohvatiProizvodeZaKosaricu(kosaricaID)
            .executeAsList()

        proizvodiUKosarici.forEach { proizvod ->
            Log.d("CheckoutActivity", "Inserting product: ${proizvod.proizvodID}, Quantity: ${proizvod.kolicina}")
            if (proizvod.proizvodID != null && proizvod.kolicina > 0) {
                database.narudzbaProizvodQueries.insertProizvodUNarudzbu(
                    narudzbaId = narudzbaId,
                    proizvodId = proizvod.proizvodID,
                    kolicina = proizvod.kolicina
                )
            } else {
                Log.e("CheckoutActivity", "Invalid product or quantity")
            }
        }


        database.kosaricaProizvodQueries.brisanjeKosarice(kosaricaID)

        val notificationHelper = NotificationHelper(this)

        Toast.makeText(this, "Narudžba uspješno kreirana!", Toast.LENGTH_SHORT).show()
    }

}
