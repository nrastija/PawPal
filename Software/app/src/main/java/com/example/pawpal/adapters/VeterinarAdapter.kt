package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Veterinari
import com.example.pawpal.R


class VeterinarAdapter(private var veterinariList: List<Veterinari>,
                       private val onVeterinarClicked: (Veterinari) -> Unit) : RecyclerView.Adapter<VeterinarAdapter.VeterinarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeterinarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.veterinari_item, parent, false)
        return VeterinarViewHolder(view)
    }

    override fun onBindViewHolder(holder: VeterinarViewHolder, position: Int) {
        val veterinar = veterinariList[position]
        holder.bind(veterinar, onVeterinarClicked)
    }

    class VeterinarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imeVet: TextView = itemView.findViewById(R.id.imeVet)
        private val titula: TextView = itemView.findViewById(R.id.titula)
        private val odaberiVet: Button = itemView.findViewById(R.id.odaberiVet)

        fun bind(veterinar: Veterinari, onVeterinarClicked: (Veterinari) -> Unit) {
            imeVet.text = veterinar.imePrezime
            titula.text = veterinar.specijalizacija

            odaberiVet.setOnClickListener {
                onVeterinarClicked(veterinar)
            }
        }
    }

    fun updateVeterinariList(newVeterinariList: List<Veterinari>) {
        veterinariList = newVeterinariList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = veterinariList.size
}