package com.example.pawpal.f12_shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.f12_shop.entiteti.Proizvod

class ProizvodAdapter(
    private val proizvodList: List<Proizvod>,
    private val onRemoveClick: (Proizvod) -> Unit,
    private val onIncreaseClick: (Proizvod) -> Unit,
    private val onDecreaseClick: (Proizvod) -> Unit
) : RecyclerView.Adapter<ProizvodAdapter.ProizvodViewHolder>() {

    inner class ProizvodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nazivProizvoda: TextView = view.findViewById(R.id.nazivProizvoda)
        val cijenaProizvoda: TextView = view.findViewById(R.id.cijenaProizvoda)
        val kategorijaProizvoda: TextView = view.findViewById(R.id.kategorijaProizvoda)
        val slikaProizvoda: ImageView = view.findViewById(R.id.slikaProizvoda)
        val removeButton: TextView = view.findViewById(R.id.remove_button)
        val buttonDecrease: TextView = view.findViewById(R.id.button_decrease)
        val buttonIncrease: TextView = view.findViewById(R.id.button_increase)
        val quantityText: TextView = view.findViewById(R.id.quantity_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProizvodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f12_kosarica_item, parent, false)
        return ProizvodViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProizvodViewHolder, position: Int) {
        val proizvod = proizvodList[position]

        holder.nazivProizvoda.text = proizvod.naziv
        holder.cijenaProizvoda.text = "Cijena: ${proizvod.cijena} kn"
        holder.kategorijaProizvoda.text = "Kategorija: ${proizvod.kategorijaID}"
        // Set a placeholder image or load from URL if needed
        holder.slikaProizvoda.setImageResource(R.drawable.test_slika)
        holder.quantityText.text = "1" // Set initial quantity

        // Set listeners for increase, decrease, and remove actions
        holder.removeButton.setOnClickListener { onRemoveClick(proizvod) }
        holder.buttonIncrease.setOnClickListener { onIncreaseClick(proizvod) }
        holder.buttonDecrease.setOnClickListener { onDecreaseClick(proizvod) }
    }

    override fun getItemCount(): Int = proizvodList.size
}
