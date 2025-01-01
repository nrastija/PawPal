package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Skola
import com.example.pawpal.R

class SkolaAdapter(
    private val skolaList: MutableList<Skola>,
    private val onDetailsClick: (Skola) -> Unit,
    private val onWishlistClick: (Skola) -> Unit
) : RecyclerView.Adapter<SkolaAdapter.SkolaViewHolder>() {


    inner class SkolaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imeSkole: TextView = view.findViewById(R.id.IME_SKOLE)
        val gumbDetalji: Button = view.findViewById(R.id.gumb1)
        val gumbWishlist: Button = view.findViewById(R.id.gumbWishlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkolaViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.f07_skola, parent, false)
        return SkolaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkolaViewHolder, position: Int) {
        val skola = skolaList[position]
        holder.imeSkole.text = skola.naziv


        holder.gumbDetalji.setOnClickListener {
            onDetailsClick(skola)
        }


        holder.gumbWishlist.setOnClickListener {
            onWishlistClick(skola)
        }
    }

    override fun getItemCount(): Int = skolaList.size
}