package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R


class TrosakAdapter(
    private val troskoviList: List<Pair<String, Triple<Double, String, String>>>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<TrosakAdapter.TroskoviViewHolder>() {

    inner class TroskoviViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nazivTroska: TextView = itemView.findViewById(R.id.trosakNaziv)
        val cijenaTroska: TextView = itemView.findViewById(R.id.trosakCijena)
        val datumTroska: TextView = itemView.findViewById(R.id.trosakDatum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TroskoviViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f05_item_trosak, parent, false)
        return TroskoviViewHolder(view)
    }

    override fun onBindViewHolder(holder: TroskoviViewHolder, position: Int) {
        val trosak = troskoviList[position]
        val naziv = trosak.first
        val (cijena, datum, izvor) = trosak.second

        holder.nazivTroska.text = if (izvor == "Webshop") {
            "Kupnja u webshopu"
        } else {
            naziv
        }

        holder.cijenaTroska.text = String.format("-€%.2f", cijena)
        holder.datumTroska.text = datum

        holder.itemView.setOnClickListener {
            onItemClick(naziv)
        }
    }

    override fun getItemCount(): Int = troskoviList.size
}

