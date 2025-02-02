package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Veterinari
import com.example.pawpal.R


class VeterinarAdapter(
    private var veterinariList: MutableList<Veterinari>,
    private val onVeterinarClicked: (Veterinari) -> Unit
) : RecyclerView.Adapter<VeterinarAdapter.VeterinarViewHolder>() {

    inner class VeterinarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imeVet: TextView = view.findViewById(R.id.imeVet)
        val titula: TextView = view.findViewById(R.id.titula)
        val gumbOdaberi: Button = view.findViewById(R.id.odaberiVet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeterinarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f04_veterinari_item, parent, false)
        return VeterinarViewHolder(view)
    }

    override fun onBindViewHolder(holder: VeterinarViewHolder, position: Int) {
        val veterinar = veterinariList[position]
        holder.imeVet.text = veterinar.imePrezime
        holder.titula.text = veterinar.specijalizacija

        holder.gumbOdaberi.setOnClickListener{
            onVeterinarClicked(veterinar)
        }
    }
    override fun getItemCount(): Int = veterinariList.size


}