package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Voditelj
import com.example.pawpal.R

class VoditeljAdapter(
    private val voditelji: List<Voditelj>,
    private val onVoditeljClick: (Voditelj) -> Unit
) : RecyclerView.Adapter<VoditeljAdapter.VoditeljViewHolder>() {

    inner class VoditeljViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imePrezime: TextView = view.findViewById(R.id.txtImePrezime)

        init {
            view.setOnClickListener {
                onVoditeljClick(voditelji[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoditeljViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.f07_voditelj_item, parent, false)
        return VoditeljViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoditeljViewHolder, position: Int) {
        val voditelj = voditelji[position]
        holder.imePrezime.text = "${voditelj.ime} ${voditelj.prezime}"
    }

    override fun getItemCount(): Int = voditelji.size
}

