package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Pasudomljavanje
import com.example.pawpal.R

class PasUdomljavanjeAdapter(
    private val pasUdomljavanjeList: MutableList<Pasudomljavanje>,
    private val onItemClick: (Pasudomljavanje) -> Unit
) : RecyclerView.Adapter<PasUdomljavanjeAdapter.PasViewHolder>() {

    inner class PasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pasSlika: ImageView = view.findViewById(R.id.slikaPas)
        val pasIme: TextView = view.findViewById(R.id.imePas)
        val pasOpis: TextView = view.findViewById(R.id.opisPas)
        val gumbicDetalji: ImageView = view.findViewById(R.id.detaljiGumbic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f09_pas_udomljavanje, parent, false)
        return PasViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasViewHolder, position: Int) {
        val pas = pasUdomljavanjeList[position]
        holder.pasIme.text = pas.ime
        holder.pasOpis.text = "${pas.spol}, ${pas.dob} godina"

        val slikicaNaziv = pas.imageUrl
        val slikicaID = holder.itemView.context.resources.getIdentifier(slikicaNaziv, "drawable", holder.itemView.context.packageName)
        holder.pasSlika.setImageResource(slikicaID)

        holder.gumbicDetalji.setOnClickListener {
            onItemClick(pas)
        }
    }

    override fun getItemCount(): Int = pasUdomljavanjeList.size

    fun updateList(newList: List<Pasudomljavanje>) {
        pasUdomljavanjeList.clear()
        pasUdomljavanjeList.addAll(newList)
        notifyDataSetChanged()
    }
}