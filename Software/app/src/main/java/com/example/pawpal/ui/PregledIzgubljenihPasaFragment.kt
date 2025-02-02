package com.example.pawpal.ui

import NotificationHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.pawpal.R
import com.example.pawpal.adapters.PsiAdapter
import com.example.pawpal.data.impl.IzgubljeniPsiImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PregledIzgubljenihPasaFragment: Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase

    private lateinit var recyclerView: RecyclerView
    private lateinit var psiAdapter: PsiAdapter
    private lateinit var dataSource: IzgubljeniPsiImpl

    private val trenutnoPrijavljenKorisnikId: Long by lazy { getCurrentUserId() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f02_pregled_izgubljenih_pasa, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val driver = AndroidSqliteDriver(AppDatabase.Schema, requireContext(), "appdatabase.db" )
        database = AppDatabase(driver)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        dataSource = IzgubljeniPsiImpl(database)

        dohvatisvePsice()
    }

    private fun dohvatisvePsice() {
        lifecycleScope.launch {
            dataSource.dohvatiSveIzgubljenePse().collect { psiList ->
                psiAdapter = PsiAdapter(
                    psiList,
                    trenutnoPrijavljenKorisnikId = trenutnoPrijavljenKorisnikId,
                    onContactClicked = { kontakt ->
                        Toast.makeText(requireContext(), kontakt, Toast.LENGTH_SHORT).show()
                    },
                    onDeleteClicked = { pasId ->
                        obrisiPrijavuIzgubljenogPsa(pasId)
                    },
                )

                recyclerView.adapter = psiAdapter
            }
        }
    }

    private fun obrisiPrijavuIzgubljenogPsa(pasId: Long) {

        val notificationHelper = NotificationHelper(requireContext())
        notificationHelper.createNotificationChannel(
            channelId = "lost_dogs_notification",
            channelName = "Lost Dogs Notifications"
        )

        lifecycleScope.launch {
            dataSource.obrisiIzgubljenogPsa(pasId)
            val updatedList = dataSource.dohvatiSveIzgubljenePse().first()
            psiAdapter.updatePsiList(updatedList)
        }

        notificationHelper.sendBigStyleNotification(
            channelId = "lost_dogs_notification",
            notificationId = pasId.toInt(),
            naslov = "Uspješno obrisana objava!",
            opis = "Oho, jeste li napokon pronašli psa? Objava je uspješno obrisana. ",
            priority = NotificationHelper.Priority.MEDIUM
        )
    }

    private fun getCurrentUserId(): Long {
        val userId = KorisnikManager.dajUlogiranogKorisnika()
        if (userId == null || userId == -1L) {
            Toast.makeText(requireContext(), "Korisnik nije prijavljen", Toast.LENGTH_SHORT).show()
        }
        return userId ?: -1L
    }
}
