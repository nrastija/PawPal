package com.example.pawpal.f12_shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.f12_shop.entiteti.Proizvod

class ProizvodShopAdapter (
    private val proizvodList: List<Proizvod>,
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
            holder.kategorijaProizvoda.text = "Kategorija: ${proizvod.kategorijaID}"
            holder.slikaProizvoda.setImageResource(R.drawable.test_slika)
        }

        override fun getItemCount(): Int = proizvodList.size
}