package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pawpal.R
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase

class VoditeljDetaljiFragment : Fragment(), DatabaseConsumer {
    private var voditeljId: Long? = null
    override lateinit var database: AppDatabase

    companion object {
        private const val ARG_VODITELJ_ID = "voditeljId"

        fun newInstance(voditeljId: Long): VoditeljDetaljiFragment {
            return VoditeljDetaljiFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_VODITELJ_ID, voditeljId)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        voditeljId = arguments?.getLong(ARG_VODITELJ_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f07_detalji_voditelja, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        voditeljId?.let { id ->
            prikaziPodatke(id, view)
        } ?: run {

            Toast.makeText(requireContext(), "Voditelj nije pronađen", Toast.LENGTH_SHORT).show()
        }
    }


    private fun prikaziPodatke(voditeljId: Long, view: View) {
        val voditelj = database.voditeljQueries.dohvatiVoditeljaPoId(voditeljId).executeAsOneOrNull()
        voditelj?.let {
            view.findViewById<TextView>(R.id.podatakImeVoditelja).text = it.ime
            view.findViewById<TextView>(R.id.podatakPrezimeVoditelja).text = it.prezime
            view.findViewById<TextView>(R.id.podatakTelefonVoditelja).text = it.telefon
            view.findViewById<TextView>(R.id.podatakEmailVoditelja).text = it.email
        } ?: run {

            Toast.makeText(requireContext(), "Voditelj nije pronađen", Toast.LENGTH_SHORT).show()
        }
    }

}
