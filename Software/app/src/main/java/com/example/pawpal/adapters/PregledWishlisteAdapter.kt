package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Skola
import com.example.pawpal.R

class PregledWishlisteAdapter(
    private val wishlistItems: List<Pair<Skola, Long>>
) : RecyclerView.Adapter<PregledWishlisteAdapter.PregledWishlistViewHolder>() {

    inner class PregledWishlistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val prioritet: TextView = view.findViewById(R.id.txtWishlistPrioritet)
        val nazivSkole: TextView = view.findViewById(R.id.txtWishlistSkolaNaziv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PregledWishlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.f07_pregled_wishliste_item, parent, false)
        return PregledWishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PregledWishlistViewHolder, position: Int) {
        val (skola, prioritet) = wishlistItems[position]
        holder.prioritet.text = prioritet.toString()
        holder.nazivSkole.text = skola.naziv
    }

    override fun getItemCount(): Int = wishlistItems.size
}
