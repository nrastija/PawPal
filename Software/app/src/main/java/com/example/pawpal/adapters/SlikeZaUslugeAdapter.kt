package com.example.pawpal.adapters

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SlikeZaUslugeAdapter(
    private val slike: List<Int>,
    private val odabranaSlika: (Int) -> Unit
) : RecyclerView.Adapter<SlikeZaUslugeAdapter.ImageViewHolder>() {

    private var odabranaPozicija = -1

    inner class ImageViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView) {
        init {
            imageView.setOnClickListener {
                val pozicija = adapterPosition
                if (pozicija != RecyclerView.NO_POSITION) {
                    val odabrana = odabranaPozicija
                    odabranaPozicija = pozicija
                    notifyItemChanged(odabrana)
                    notifyItemChanged(odabranaPozicija)
                    odabranaSlika(slike[pozicija])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                width = 300
                height = 300
                setMargins(8, 8, 8, 8)
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, pozicija: Int) {
        holder.imageView.setImageResource(slike[pozicija])
        holder.imageView.alpha = if (pozicija == odabranaPozicija) 1f else 0.6f
    }

    override fun getItemCount() = slike.size
}
