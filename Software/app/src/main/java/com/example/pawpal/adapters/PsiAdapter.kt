package com.example.pawpal.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PsiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f02_izgubljeni_psi_item, parent, false)
        return PsiViewHolder(view)
    }

    override fun onBindViewHolder(holder: PsiViewHolder, position: Int) {
        val pas = psiList[position]
        holder.bind(pas)
    }

    class PsiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val slikaPsa: ImageView = itemView.findViewById(R.id.slikaPsa)
        private val opisPsa: TextView = itemView.findViewById(R.id.opisPsa)
        private val lokacija: TextView = itemView.findViewById(R.id.lokacija)
        private val odaberiPsa: Button = itemView.findViewById(R.id.odaberiPsa)

        fun bind(pas: IzgubljeniPsi) {
            Log.d("Velicina slike", pas.imageUri.length.toString())
            opisPsa.text = pas.description
            lokacija.text = pas.lastseenlocation
            pas.imageUri.let {
                val bitmap = decodeBase64ToBitmap(it)
                slikaPsa.setImageBitmap(bitmap)
            }

            odaberiPsa.setOnClickListener{
            }
        }

        private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
            Log.d("Usao u funkciju", "")
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            Log.d("Dekodiram", "")
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }



    override fun getItemCount(): Int = psiList.size

}