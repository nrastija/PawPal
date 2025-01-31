package com.example.pawpal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.data.dataobjects.ZahtjevSImenomPsa


class ZahtjevAdapter (

    private val zahtjeviList: List<ZahtjevSImenomPsa>,
    private val onOdobriClick: (ZahtjevSImenomPsa) -> Unit,
    private val onOdbijClick: (ZahtjevSImenomPsa) -> Unit
) : RecyclerView.Adapter<ZahtjevAdapter.ZahtjevViewHolder>() {

    inner class ZahtjevViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imeKorisnika: TextView = view.findViewById(R.id.imeKorisnika)
        val idPsa: TextView = view.findViewById(R.id.imePsaZaUdomljavanje)
        val btnOdobri: Button = view.findViewById(R.id.odobriZahtjev)
        val btnOdbij: Button = view.findViewById(R.id.odbijZahtjev)

        fun bind(zahtjevsimenompsa: ZahtjevSImenomPsa) {
            val zahtjev = zahtjevsimenompsa.zahtjev
            imeKorisnika.text = "${zahtjev.ime} ${zahtjev.prezime}"
            idPsa.text = zahtjevsimenompsa.imePsa ?: "Ne postoji ime psa"

            btnOdobri.setOnClickListener { onOdobriClick(zahtjevsimenompsa) }
            btnOdbij.setOnClickListener { onOdbijClick(zahtjevsimenompsa) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZahtjevViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f06_korisnici_sa_zahtjevom, parent, false)
        return ZahtjevViewHolder(view)
    }

    override fun onBindViewHolder(holder: ZahtjevViewHolder, position: Int) {
        holder.bind(zahtjeviList[position])
    }

    override fun getItemCount(): Int = zahtjeviList.size
}