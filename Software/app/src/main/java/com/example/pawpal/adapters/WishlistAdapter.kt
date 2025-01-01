package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Skola
import com.example.pawpal.R

class WishlistAdapter(
    private val skolaList: MutableList<Skola>,
    private val onRemoveClick: (Skola) -> Unit,
    private val onPriorityChange: (Skola, Int) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    inner class WishlistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nazivSkole: TextView = view.findViewById(R.id.nazivSkole)
        val buttonRemove: Button = view.findViewById(R.id.buttonRemove)
        val buttonIncreasePriority: Button = view.findViewById(R.id.buttonIncreasePriority)
        val buttonDecreasePriority: Button = view.findViewById(R.id.buttonDecreasePriority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f07_wishlist_item, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val skola = skolaList[position]
        holder.nazivSkole.text = "${position + 1}. ${skola.naziv}"

        holder.buttonRemove.setOnClickListener { onRemoveClick(skola) }
        holder.buttonIncreasePriority.setOnClickListener {
            if (position > 0) {
                onPriorityChange(skola, position - 1)
            }
        }
        holder.buttonDecreasePriority.setOnClickListener {
            if (position < skolaList.size - 1) {
                onPriorityChange(skola, position + 1)
            }
        }
    }

    override fun getItemCount(): Int = skolaList.size
}