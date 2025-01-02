package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Proizvod
import com.example.pawpal.R

class ProizvodShopAdapter(
    private val proizvodList: MutableList<Proizvod>,
    private val onProizvodClick: (Proizvod) -> Unit
) : RecyclerView.Adapter<ProizvodShopAdapter.ProizvodViewHolder>() {

    inner class ProizvodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val slikaProizvoda: ImageView = view.findViewById(R.id.slikaProizvodaShop)
        val nazivProizvoda: TextView = view.findViewById(R.id.nazivProizvodaShop)
        val kategorijaProizvoda: TextView = view.findViewById(R.id.kategorijaProizvodaShop)
        val cijenaProizvoda: TextView = view.findViewById(R.id.cijenaProizvodaShop)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProizvodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f12_shop_product, parent, false)
        return ProizvodViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProizvodViewHolder, position: Int) {
        val proizvod = proizvodList[position]

        holder.nazivProizvoda.text = proizvod.naziv
        holder.cijenaProizvoda.text = "Cijena: ${proizvod.cijena} €"
        holder.kategorijaProizvoda.text = "Kategorija: ${proizvod.kategorijaId}"

        val nazivSlike = proizvod.imageUrl
        val slikaID = holder.itemView.context.resources.getIdentifier(nazivSlike, "drawable", holder.itemView.context.packageName)
        holder.slikaProizvoda.setImageResource(slikaID)

        holder.itemView.setOnClickListener {
            onProizvodClick(proizvod)
        }
    }

    override fun getItemCount(): Int = proizvodList.size
}