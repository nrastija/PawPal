package com.example.pawpal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Skola
import com.example.pawpal.R
import com.example.pawpal.adapters.PregledWishlisteAdapter
import com.example.pawpal.data.impl.WishlistDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class PregledWishlisteFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PregledWishlisteAdapter
    private val wishlistItems = mutableListOf<Pair<Skola, Long>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f07_layout_pregled_wishliste, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerPregledWishlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PregledWishlisteAdapter(wishlistItems)
        recyclerView.adapter = adapter

        fetchWishlist()
    }

    private fun fetchWishlist() {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID == null) {
            return
        }

        lifecycleScope.launch {
            val wishlistDataSource = WishlistDataSourceImpl(database)
            val items = wishlistDataSource.getAllWishlistItemsWithPriorities(korisnikID)

            wishlistItems.clear()
            wishlistItems.addAll(items.sortedBy { it.second })
            adapter.notifyDataSetChanged()
        }
    }
}

