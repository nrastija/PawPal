package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.DohvatiProizvodeZaKosaricu
import appdatabase.KosaricaProizvod
import com.example.pawpal.R


class ProizvodKosaricaAdapter(
    private val proizvodList: List<DohvatiProizvodeZaKosaricu>,
    private val obrisiProizvod: (DohvatiProizvodeZaKosaricu) -> Unit,
    private val povecajKolicinu: (DohvatiProizvodeZaKosaricu) -> Unit,
    private val smanjiKolicinu: (DohvatiProizvodeZaKosaricu) -> Unit
) : RecyclerView.Adapter<ProizvodKosaricaAdapter.ProizvodViewHolder>() {

    inner class ProizvodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nazivProizvoda: TextView = view.findViewById(R.id.nazivProizvoda)
        val cijenaProizvoda: TextView = view.findViewById(R.id.cijenaProizvoda)
        val kategorijaProizvoda: TextView = view.findViewById(R.id.kategorijaProizvoda)
        val slikaProizvoda: ImageView = view.findViewById(R.id.slikaProizvoda)
        val removeButton: TextView = view.findViewById(R.id.btnObrisi)
        val buttonDecrease: TextView = view.findViewById(R.id.btnSmanji)
        val buttonIncrease: TextView = view.findViewById(R.id.btnPovecaj)
        val quantityText: TextView = view.findViewById(R.id.kolicinaBroj)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProizvodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f12_kosarica_item, parent, false)
        return ProizvodViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProizvodViewHolder, position: Int) {
        val proizvod = proizvodList[position]


        holder.nazivProizvoda.text = proizvod.naziv
        holder.cijenaProizvoda.text = "Cijena: ${proizvod.cijena} €"
        holder.kategorijaProizvoda.text = "Kategorija: ${proizvod.kategorijaId}"
        holder.quantityText.text = "${proizvod.kolicina}"

        val nazivSlike = proizvod.imageUrl
        val slikaID = holder.itemView.context.resources.getIdentifier(nazivSlike, "drawable", holder.itemView.context.packageName)
        holder.slikaProizvoda.setImageResource(slikaID)

        holder.removeButton.setOnClickListener { obrisiProizvod(proizvod) }
        holder.buttonIncrease.setOnClickListener { povecajKolicinu(proizvod) }
        holder.buttonDecrease.setOnClickListener { smanjiKolicinu(proizvod) }
    }

    override fun getItemCount(): Int = proizvodList.size
}
