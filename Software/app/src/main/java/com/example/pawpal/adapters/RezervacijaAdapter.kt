package com.example.pawpal.adapters

class RezervacijaAdapter(
    private val rezervacije: List<Rezervacija>,
    private val onDetaljiClick: (Rezervacija) -> Unit,
    private val onOtkaziClick: (Rezervacija) -> Unit
) : RecyclerView.Adapter<RezervacijaAdapter.RezervacijaViewHolder>() {

    inner class RezervacijaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val naziv: TextView = view.findViewById(R.id.nazivRezervacije)
        val datum: TextView = view.findViewById(R.id.datumVrijeme)
        val status: TextView = view.findViewById(R.id.statusRezervacije)
        val btnDetalji: Button = view.findViewById(R.id.btnDetalji)
        val btnOtkazi: Button = view.findViewById(R.id.btnOtkazi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RezervacijaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return RezervacijaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RezervacijaViewHolder, position: Int) {
        val rezervacija = rezervacije[position]

        holder.naziv.text = rezervacija.naziv
        holder.datum.text = rezervacija.datum
        holder.status.text = rezervacija.status

        holder.btnDetalji.setOnClickListener {
            onDetaljiClick(rezervacija)
        }

        holder.btnOtkazi.setOnClickListener {
            onOtkaziClick(rezervacija)
        }
    }

    override fun getItemCount(): Int = rezervacije.size
}
