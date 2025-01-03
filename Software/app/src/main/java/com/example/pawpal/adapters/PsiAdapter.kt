package com.example.pawpal.adapters

import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.IzgubljeniPsi
import com.example.pawpal.R

class PsiAdapter(private val psiList: List<IzgubljeniPsi>): RecyclerView.Adapter<PsiAdapter.PsiViewHolder>() {
    class PsiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val slikaPsa: ImageView = itemView.findViewById(R.id.slikaPsa)
        val opisPsa: TextView = itemView.findViewById(R.id.opisPsa)
        val lokacija: TextView = itemView.findViewById(R.id.lokacija)
        val odaberiPsa: Button = itemView.findViewById(R.id.odaberiPsa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PsiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f02_izgubljeni_psi_item, parent, false)
        return PsiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PsiViewHolder, position: Int) {
        val pas = psiList[position]
        holder.opisPsa.text = pas.description
        holder.lokacija.text = pas.lastseenlocation
        val imageUri = Uri.parse(pas.imageUri)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                val source = ImageDecoder.createSource(holder.slikaPsa.context.contentResolver, imageUri)
                val drawable: Drawable = ImageDecoder.decodeDrawable(source)
                holder.slikaPsa.setImageDrawable(drawable)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            holder.slikaPsa.setImageURI(imageUri)
        }

        holder.odaberiPsa.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return psiList.size
    }

}