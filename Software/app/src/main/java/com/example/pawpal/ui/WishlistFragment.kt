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
import com.example.pawpal.adapters.WishlistAdapter
import com.example.pawpal.data.impl.WishlistDataSourceImpl
import com.example.pawpal.data.session.KorisnikManager
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class WishlistFragment : Fragment(), DatabaseConsumer {
    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WishlistAdapter
    private val wishlist = mutableListOf<appdatabase.Skola>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f07_layout_wishlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerWishlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WishlistAdapter(wishlist, ::removeSkola, ::changePriority)
        recyclerView.adapter = adapter

        fetchWishlist()

        view.findViewById<View>(R.id.buttonSendRequest).setOnClickListener {
            sendRequest()
        }
    }

    private fun fetchWishlist() {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID == null) {
            Toast.makeText(requireContext(), "Korisnik nije prijavljen!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val wishlistDataSource = WishlistDataSourceImpl(database)
            val wishlistItems = wishlistDataSource.getAllWishlistItems(korisnikID)

            wishlist.clear()
            wishlist.addAll(wishlistItems)
            adapter.notifyDataSetChanged()
        }
    }



    private fun removeSkola(skola: appdatabase.Skola) {
        val korisnikID = KorisnikManager.dajUlogiranogKorisnika()
        if (korisnikID == null) {
            Toast.makeText(requireContext(), "Korisnik nije prijavljen!", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val wishlistDataSource = WishlistDataSourceImpl(database)
            wishlistDataSource.removeFromWishlist(skola.skolaID, korisnikID)
            fetchWishlist()
        }
    }



    private fun changePriority(skola: appdatabase.Skola, newIndex: Int) {
        wishlist.remove(skola)
        wishlist.add(newIndex, skola)
        adapter.notifyDataSetChanged()
    }

    private fun sendRequest() {
        val sortedSkole = wishlist.map { it.naziv }.joinToString("\n")
        Toast.makeText(requireContext(), "Zahtjev poslan:\n$sortedSkole", Toast.LENGTH_SHORT).show()
    }
}