package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Skola
import com.example.pawpal.R

class WishlistAdapter(
    private val skolaList: MutableList<Pair<Skola, Long>>,
    private val onRemoveClick: (Skola) -> Unit,
    private val onPriorityChange: (Skola, Long) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    inner class WishlistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nazivSkole: TextView = view.findViewById(R.id.nazivSkole)
        val odabirPrioriteta: Spinner = view.findViewById(R.id.odabirPrioriteta)
        val buttonRemove: Button = view.findViewById(R.id.gumbUkloni)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f07_wishlist_item, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val (skola, prioritet) = skolaList[position]
        holder.nazivSkole.text = skola.naziv


        holder.odabirPrioriteta.setSelection((prioritet - 1).toInt())


        holder.odabirPrioriteta.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val noviPrioritet = pos + 1L
                if (noviPrioritet != prioritet) {
                    onPriorityChange(skola, noviPrioritet)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        })


        holder.buttonRemove.setOnClickListener { onRemoveClick(skola) }
    }

    override fun getItemCount(): Int = skolaList.size
}
