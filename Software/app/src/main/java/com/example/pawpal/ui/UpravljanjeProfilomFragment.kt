package com.example.pawpal.ui

import NotificationHelper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.data.session.KorisnikManager
import com.pawpal.appdatabase.AppDatabase
import com.example.pawpal.main.PawPalApplication
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.example.pawpal.data.impl.KorisnikDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import kotlinx.coroutines.launch

class UpravljanjeProfilomFragment : Fragment() {

    private lateinit var korisnikDataSource: KorisnikDataSource
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f01_upravljanje_profilom, container, false)

        database = (requireActivity().application as PawPalApplication).database
        korisnikDataSource = KorisnikDataSourceImpl(database)

        val btnOdjava: Button = view.findViewById(R.id.btnOdjava)
        val btnDeaktivacija: Button = view.findViewById(R.id.btnBrisanjeProfila)
        val btnUredivanje: Button = view.findViewById(R.id.btnUredivanjeProfila)

        btnOdjava.setOnClickListener { prikaziPotvrduOdjave() }
        btnDeaktivacija.setOnClickListener { prikaziPotvrduDeaktivacije() }
        btnUredivanje.setOnClickListener {
            val fragment = UredivanjeProfilaKorisnikaFragment()
            navigateToFragment(fragment)
        }

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        if (fragment is DatabaseConsumer) {
            fragment.database = database
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun prikaziPotvrduOdjave() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Potvrda brisanja")
            .setMessage("Jeste li sigurni da se želite odjaviti?")
            .setPositiveButton("Da") { dialog, which ->
                odjaviKorisnika()
            }
            .setNegativeButton("Ne") { dialog, which ->
                Toast.makeText(context, "Prekinuta odjava", Toast.LENGTH_SHORT).show()
            }

        builder.create().show()
    }

    private fun odjaviKorisnika() {
        KorisnikManager.odjava()
        val intent = Intent(requireContext(), PrijavaActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun prikaziPotvrduDeaktivacije() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Potvrda brisanja")
            .setMessage("Jeste li sigurni da želite deaktivirati profil?")
            .setPositiveButton("Da") { dialog, which ->
                deaktivirajKorisnika()
            }
            .setNegativeButton("Ne") { dialog, which ->
                Toast.makeText(context, "Otkazana deaktivacija profila", Toast.LENGTH_SHORT).show()
            }

        builder.create().show()

    }

    private fun deaktivirajKorisnika() {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()

        if (korisnikID == null) {
            return
        }

        val notificationHelper = NotificationHelper(requireContext())
        notificationHelper.createNotificationChannel(
            channelId = "deactivation_notifications",
            channelName = "Deactivation Notifications"
        )

        notificationHelper.sendNotification(
            channelId = "checkout_notifications",
            notificationId = korisnikID.toInt(),
            naslov = "Narudžba u transakciji!",
            opis = "Vaš korisnički profil sa šifrom ${korisnikID} je uspješno obrisan iz sustava.",
            priority = NotificationHelper.Priority.LOW
        )

        lifecycleScope.launch {
            korisnikDataSource.dajKorisnikaPoID(korisnikID)?.let {
                korisnikDataSource.obrisiKorisnikaPoID(korisnikID)

                if (korisnikDataSource.dajKorisnikaPoID(korisnikID) == null) {
                    KorisnikManager.odjava()
                    startActivity(Intent(requireContext(), PrijavaActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

}
