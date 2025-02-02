package com.example.pawpal.ui

import ApiServiceHelper
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import appdatabase.NarudzbaProizvod
import com.example.pawpal.R
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailsAdapter(
    private val items: List<NarudzbaProizvod>,
    private val database: AppDatabase,
    private val lifecycleScope: LifecycleCoroutineScope,
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nazivProizvoda: TextView = view.findViewById(R.id.nazivProizvoda)
        val kategorijaProizvoda: TextView = view.findViewById(R.id.kategorijaProizvoda)
        val cijenaProizvoda: TextView = view.findViewById(R.id.cijenaProizvoda)
        val detaljiProizvoda: TextView = view.findViewById(R.id.detaljiProizvoda)
        val kolicinaProizvoda: TextView = view.findViewById(R.id.kolicinaProizvoda)
        val slikaProizvoda: ImageView = view.findViewById(R.id.slikaProizvoda)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f10_proizvod_item, parent, false)
        return OrderViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val item = items[position]
        val apiServiceHelper = ApiServiceHelper()

        lifecycleScope.launch {
            val productDetails = getProductDetails(item.proizvodId)

            holder.nazivProizvoda.text = productDetails.naziv

            apiServiceHelper.getKategorijaById(
                productDetails.kategorijaId.toInt(),
                onSuccess = { kategorija ->
                    holder.kategorijaProizvoda.text = "Kategorija: ${kategorija.naziv}"
                },
                onError = { errorMessage ->
                    holder.kategorijaProizvoda.text = "Error fetching category"
                }
            )

            holder.cijenaProizvoda.text = productDetails.cijena.toString() + "€"
            holder.detaljiProizvoda.text = productDetails.opis
            holder.kolicinaProizvoda.text = "Količina: " + item.kolicina
            val slikaID = holder.itemView.context.resources.getIdentifier(productDetails.imageUrl, "drawable", holder.itemView.context.packageName)
            holder.slikaProizvoda.setImageResource(slikaID)
        }
    }

    override fun getItemCount(): Int = items.size

    private suspend fun getProductDetails(proizvodId: Long): appdatabase.Proizvod {
        return withContext(Dispatchers.IO) {
            database.proizvodQueries.dohvatiProizvodPoId(proizvodId).executeAsOne()
        }
    }

}
