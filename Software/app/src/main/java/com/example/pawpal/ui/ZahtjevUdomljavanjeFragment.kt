package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.main.MainActivity
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class ZahtjevUdomljavanjeFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private var pasID: Long = 0

    companion object {
        const val ARG_PAS_ID = "pasID"

        fun newInstance(pasID: Long): ZahtjevUdomljavanjeFragment {
            val fragment = ZahtjevUdomljavanjeFragment()
            val args = Bundle()
            args.putLong(ARG_PAS_ID, pasID)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pasID = it.getLong(ARG_PAS_ID)
        }

        database = (activity as MainActivity).database

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f09_obrazac_udomljavanje, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.apply {
            findViewById<DrawerLayout>(R.id.drawerLayout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            supportActionBar?.hide()
        }

        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val imePsaTextView: TextView = view.findViewById(R.id.UdomiPasUdomljavanje)
        val pasSlikaImageView: ImageView = view.findViewById(R.id.PasSlikaUdomljavanje)
        val imeInput: EditText = view.findViewById(R.id.ImeUdomljavanje)
        val prezimeInput: EditText = view.findViewById(R.id.PrezimeUdomljavanje)
        val emailInput: EditText = view.findViewById(R.id.AdresaUdomljavanje)
        val telefonInput: EditText = view.findViewById(R.id.BrojTelefonaUdomljavanje)
        val dodatneInfoInput: EditText = view.findViewById(R.id.DodatneInfoUdomljavanje)
        val radioDrugiLjubimci: RadioGroup = view.findViewById(R.id.RadioDrugiLjubimac)
        val radioClanObitelji: RadioGroup = view.findViewById(R.id.RadioClanObitelji)
        val radioIskustvoSPsima: RadioGroup = view.findViewById(R.id.IskustvoSPsima)
        val submitButton: Button = view.findViewById(R.id.UdomiPsa)

        lifecycleScope.launch {
            val pas = database.pasUdomljavanjeQueries.dohvatiPsaPoID(pasID).executeAsOne()
            imePsaTextView.text = "Udomite psa: ${pas.ime}"

            val slikaID = resources.getIdentifier(pas.imageUrl, "drawable", requireContext().packageName)
            if (slikaID != 0) {
                pasSlikaImageView.setImageResource(slikaID)
            } else {
                pasSlikaImageView.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        }

        submitButton.setOnClickListener {
            val ime = imeInput.text.toString()
            val prezime = prezimeInput.text.toString()
            val email = emailInput.text.toString()
            val telefon = telefonInput.text.toString()
            val dodatneInfo = dodatneInfoInput.text.toString()

            val drugiLjubimci = when (radioDrugiLjubimci.checkedRadioButtonId) {
                R.id.DrugiLjubimciDa -> "Da"
                R.id.DrugiLjubimciNe -> "Ne"
                else -> ""
            }

            val clanObitelji = when (radioClanObitelji.checkedRadioButtonId) {
                R.id.Samac -> "Samac"
                R.id.ClanObitelji -> "Član obitelji"
                else -> ""
            }

            val iskustvoSPsima = when (radioIskustvoSPsima.checkedRadioButtonId) {
                R.id.Imam -> "Imam"
                R.id.Nemam -> "Nemam"
                else -> ""
            }

            if (ime.isEmpty() || prezime.isEmpty() || email.isEmpty() || telefon.isEmpty()) {
                Toast.makeText(context, "Molimo popunite sva obavezna polja.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Submit form data to the database
            lifecycleScope.launch {
                database.zahtjevUdomljavanjeQueries.insertZahtjev(
                    paszahtjevID = pasID,
                    ime = ime,
                    prezime = prezime,
                    email = email,
                    telefon = telefon,
                    drugiLjubimci = drugiLjubimci,
                    clanObitelji = clanObitelji,
                    iskustvoSPsima = iskustvoSPsima,
                    dodatneInformacije = dodatneInfo
                )
                Toast.makeText(context, "Zahtjev uspješno poslan!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.apply {
            findViewById<DrawerLayout>(R.id.drawerLayout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.show()
        }
    }
}
