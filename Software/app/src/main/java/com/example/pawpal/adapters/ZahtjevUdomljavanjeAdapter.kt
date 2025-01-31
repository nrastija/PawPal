import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R

class ZahtjevUdomljavanjeAdapter(
    private val zahtjeviUdomljavanje: List<appdatabase.Zahtjevudomljavanje>,
    private val onCancelClick: (Any) -> Unit
) : RecyclerView.Adapter<ZahtjevUdomljavanjeAdapter.AdoptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f10_adoption_reservation, parent, false)
        return AdoptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdoptionViewHolder, position: Int) {
        holder.bind(zahtjeviUdomljavanje[position])
    }

    override fun getItemCount(): Int = zahtjeviUdomljavanje.size

    inner class AdoptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val naziv: TextView = view.findViewById(R.id.adoptionServiceName)
        val ime: TextView = view.findViewById(R.id.adoptionServiceUserName)
        val email: TextView = view.findViewById(R.id.adoptionServiceUserEmail)
        val btnCancel: Button = view.findViewById(R.id.adoptionServiceCancel)

        fun bind(reservation: appdatabase.Zahtjevudomljavanje) {
            naziv.text = "Udomljavanje Psa"
            ime.text = reservation.ime
            email.text = reservation.email

            btnCancel.setOnClickListener { onCancelClick(reservation) }
        }
    }
}
