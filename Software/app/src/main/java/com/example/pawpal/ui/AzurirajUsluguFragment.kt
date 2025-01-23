package com.example.pawpal.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class AzurirajUsluguFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private var uslugaID: Long = 0

    companion object {
        const val ARG_USLUGA_ID = "uslugaID"
        fun newInstance(uslugaID: Long) = AzurirajUsluguFragment().apply {
            arguments = Bundle().apply { putLong(ARG_USLUGA_ID, uslugaID) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uslugaID = arguments?.getLong(ARG_USLUGA_ID) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.f03_azuriraj_uslugu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nazivInput = view.findViewById<EditText>(R.id.nazivUslugeInput)
        val opisInput = view.findViewById<EditText>(R.id.opisUslugeInput)
        val cijenaInput = view.findViewById<EditText>(R.id.cijenaUslugeInput)
        val trajanjeInput = view.findViewById<EditText>(R.id.trajanjeUslugeInput)

        lifecycleScope.launch {
            val usluga = database.uslugaQueries.dohvatiUsluguPoID(uslugaID).executeAsOne()
            nazivInput.setText(usluga.naziv)
            opisInput.setText(usluga.opis)
            cijenaInput.setText(usluga.cijena.toString())
            trajanjeInput.setText(usluga.trajanje.toString())
        }

        view.findViewById<ImageButton>(R.id.btnNatrag).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnSpremiUslugu).setOnClickListener {
            val naziv = nazivInput.text.toString()
            val opis = opisInput.text.toString()
            val cijena = cijenaInput.text.toString()
            val trajanje = trajanjeInput.text.toString()

            if (naziv.isEmpty() || cijena.isEmpty() || trajanje.isEmpty()) {
                Toast.makeText(context, "Molimo popunite sva obavezna polja", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    database.uslugaQueries.azurirajUslugu(
                        uslugaID = uslugaID,
                        naziv = naziv,
                        opis = opis,
                        cijena = cijena.toDouble(),
                        trajanje = trajanje.toLong()
                    )
                    Toast.makeText(context, "Usluga uspješno ažurirana!", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } catch (e: Exception) {
                    Toast.makeText(context, "Greška prilikom ažuriranja: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
