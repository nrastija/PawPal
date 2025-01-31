import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R

class VetPrikazRezervacijaAdapter(
    private val rezervacijeVet: List<appdatabase.RezervacijaVeterinara>,
    private val onCancelClick: (Any) -> Unit
) : RecyclerView.Adapter<VetPrikazRezervacijaAdapter.VetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f10_vet_reservation, parent, false)
        return VetViewHolder(view)
    }

    override fun onBindViewHolder(holder: VetViewHolder, position: Int) {
        holder.bind(rezervacijeVet[position])
    }

    override fun getItemCount(): Int = rezervacijeVet.size

    inner class VetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val naziv: TextView = view.findViewById(R.id.vetServiceName)
        val datum: TextView = view.findViewById(R.id.vetServiceDate)
        val btnCancel: Button = view.findViewById(R.id.vetServiceCancel)

        fun bind(reservation: appdatabase.RezervacijaVeterinara) {
            naziv.text = "Veterinarski Pregled"
            datum.text = reservation.datum

            btnCancel.setOnClickListener { onCancelClick(reservation) }
        }
    }
}
