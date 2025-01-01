package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.SkolaAdapter
import com.example.pawpal.data.impl.SkolaDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class SkolaFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SkolaAdapter
    private val skolaList = mutableListOf<appdatabase.Skola>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f07_layout_odabir_skole, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val skolaDataSource = SkolaDataSourceImpl(database)

        recyclerView = view.findViewById(R.id.recyclerSkole)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SkolaAdapter(skolaList) { skola ->
            navigateToSkolaDetaljFragment(skola)
        }
        recyclerView.adapter = adapter

        fetchSkole(skolaDataSource)
    }

    private fun fetchSkole(skolaDataSource: SkolaDataSourceImpl) {
        lifecycleScope.launch {
            val skole = skolaDataSource.dohvatiSveSkole()
            updateSkolaList(skole)
        }
    }

    private fun updateSkolaList(skole: List<appdatabase.Skola>) {
        skolaList.clear()
        skolaList.addAll(skole)
        adapter.notifyDataSetChanged()
    }

    private fun navigateToSkolaDetaljFragment(skola: appdatabase.Skola) {
        val detaljFragment = SkolaDetaljiFragment.newInstance(skola.skolaID)
        detaljFragment.database = database

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, detaljFragment)
            .addToBackStack(null)
            .commit()
    }
}
