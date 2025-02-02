package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Promoponuda
import com.example.pawpal.R
class PromoPonudaAdapater(
    private val promoList: MutableList<Promoponuda>,
    private val koristeniPromo: Set<Long>,
    private val onKodClick: (Promoponuda) -> Unit
) : RecyclerView.Adapter<PromoPonudaAdapater.PromoViewHolder>() {

    inner class PromoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val promoSlika: ImageView = view.findViewById(R.id.promoPonuda)
        val promoNaslov: TextView = view.findViewById(R.id.promoPonudaNaslov)
        val promoOpis: TextView = view.findViewById(R.id.promoPonudaOpis)
        val promoDatum: TextView = view.findViewById(R.id.promoPonudaDatum)
        val promoUvjeti: TextView = view.findViewById(R.id.promoPonudaUvjeti)
        val btnGenerirajKod: Button = view.findViewById(R.id.btnGenerirajKod)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.f03_ponuda, parent, false)
        return PromoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val promo = promoList[position]

        holder.promoNaslov.text = promo.naziv
        holder.promoOpis.text = promo.opis
        holder.promoDatum.text = "Vrijedi do: ${promo.datumValjanosti}"
        holder.promoUvjeti.text = "* ${promo.uvjeti}"
        holder.promoSlika.setImageResource(R.drawable.promoponuda)

        holder.btnGenerirajKod.setOnClickListener {
            onKodClick(promo)
        }
    }

    override fun getItemCount(): Int = promoList.size
}
