package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.ProizvodShopAdapter
import com.example.pawpal.data.impl.ProizvodDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class ShopFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProizvodShopAdapter
    private val proizvodList = mutableListOf<appdatabase.Proizvod>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f12_layout_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val proizvodDataSource = ProizvodDataSourceImpl(database)
        val floatingButton: FloatingActionButton = view.findViewById(R.id.floatingButton)

        recyclerView = view.findViewById(R.id.recyclerShop)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProizvodShopAdapter(proizvodList) { proizvod ->
            navigateToProizvodDetaljFragment(proizvod)
        }
        recyclerView.adapter = adapter

        fetchProducts(proizvodDataSource)

        // Set up category filter buttons
        val btnZdravlje: Button = view.findViewById(R.id.filterZdravlje)
        val btnHrana: Button = view.findViewById(R.id.filterHrana)
        val btnHigijena: Button = view.findViewById(R.id.filterHigijena)
        val btnOstalo: Button = view.findViewById(R.id.filterOstalo)
        val btnReset: Button = view.findViewById(R.id.filterReset)

        btnZdravlje.setOnClickListener { fetchFilteredProducts(proizvodDataSource, 1) } // Example category ID
        btnHrana.setOnClickListener { fetchFilteredProducts(proizvodDataSource, 2) }
        btnHigijena.setOnClickListener { fetchFilteredProducts(proizvodDataSource, 3) }
        btnOstalo.setOnClickListener { fetchFilteredProducts(proizvodDataSource, 4) }
        btnReset.setOnClickListener { fetchProducts(proizvodDataSource) }

        floatingButton.setOnClickListener {
            lifecycleScope.launch {
                //POTREBNO KASNIJE DOHVATITI KORISNIKID!
                val kosarica = database.kosaricaQueries.provjeriPostojanje(1).executeAsOneOrNull()
                    ?: let {
                        database.kosaricaQueries.InsertKosarica(1)
                        database.kosaricaQueries.provjeriPostojanje(1).executeAsOneOrNull()
                    }

                if (kosarica != null) {
                    navigateToKosaricaFragment(kosarica)
                }
            }
        }
    }

    private fun fetchProducts(proizvodDataSource: ProizvodDataSourceImpl) {
        lifecycleScope.launch {
            proizvodDataSource.dohvatiProizvode().collect { products ->
                updateProductList(products)
            }
        }
    }

    private fun fetchFilteredProducts(proizvodDataSource: ProizvodDataSourceImpl, kategorijaId: Long) {
        lifecycleScope.launch {
            proizvodDataSource.filtrirajProizvodePoKategoriji(kategorijaId).collect { products ->
                updateProductList(products.distinct())
            }
        }
    }

    private fun updateProductList(products: List<appdatabase.Proizvod>) {
        proizvodList.clear()
        proizvodList.addAll(products)
        adapter.notifyDataSetChanged()
    }

    private fun navigateToProizvodDetaljFragment(proizvod: appdatabase.Proizvod) {
        val detaljFragment = ProizvodDetaljFragment.newInstance(
            proizvod.proizvodID
        )

        detaljFragment.database = database

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, detaljFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToKosaricaFragment(kosarica: appdatabase.Kosarica) {
        val kosaricaFragment = KosaricaFragment.newInstance(
            kosarica.kosaricaID
        )
        kosaricaFragment.database = database

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.fragmentContainer, kosaricaFragment)
            .addToBackStack(null)
            .commit()
    }
}
