package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.adapters.SkolaAdapter
import com.example.pawpal.data.impl.SkolaDataSourceImpl
import com.example.pawpal.data.impl.WishlistDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
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
        adapter = SkolaAdapter(
            skolaList,
            onDetailsClick = { skola -> navigateToSkolaDetaljFragment(skola) },
            onWishlistClick = { skola -> addToWishlist(skola) }
        )
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

    private fun addToWishlist(skola: appdatabase.Skola) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID == null) {
            Toast.makeText(requireContext(), "Korisnik nije prijavljen!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val wishlistDataSource = WishlistDataSourceImpl(database)
            val isInWishlist = wishlistDataSource.isSkolaInWishlist(skola.skolaID, korisnikID)

            if (!isInWishlist) {
                wishlistDataSource.addToWishlist(skola.skolaID, korisnikID)
                Toast.makeText(requireContext(), "${skola.naziv} dodano u wishlist!", Toast.LENGTH_SHORT).show()
            } else {
                wishlistDataSource.removeFromWishlist(skola.skolaID, korisnikID)
                Toast.makeText(requireContext(), "${skola.naziv} uklonjeno iz wishlist-a!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}