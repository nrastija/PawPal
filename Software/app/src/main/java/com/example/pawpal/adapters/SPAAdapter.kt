package com.example.pawpal.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Usluga
import com.example.pawpal.R

class SPAAdapter(
    private val uslugaList: MutableList<Usluga>,
    private val onUslugaClick: (Usluga) -> Unit
) : RecyclerView.Adapter<SPAAdapter.UslugaViewHolder>() {

    inner class UslugaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val slikaUsluge: ImageView = view.findViewById(R.id.slikaSPA)
        val nazivUsluge: TextView = view.findViewById(R.id.uslugaSPA)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UslugaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f03_usluga, parent, false)
        return UslugaViewHolder(view)
    }

    override fun onBindViewHolder(holder: UslugaViewHolder, position: Int) {
        val usluga = uslugaList[position]

        holder.nazivUsluge.text = usluga.naziv

        val nazivSlike = usluga.imageUrl
        val slikaID = holder.itemView.context.resources.getIdentifier(nazivSlike, "drawable", holder.itemView.context.packageName)
        holder.slikaUsluge.setImageResource(slikaID)

        holder.itemView.setOnClickListener {
            onUslugaClick(usluga)
        }
    }

    override fun getItemCount(): Int = uslugaList.size
}