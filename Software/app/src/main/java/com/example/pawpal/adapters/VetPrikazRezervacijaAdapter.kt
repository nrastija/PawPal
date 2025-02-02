import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VetPrikazRezervacijaAdapter(
    private val rezervacijeVet: MutableList<appdatabase.RezervacijaVeterinara>,
    private val onCancelClick: (Any) -> Unit,
    private val database: AppDatabase,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val context: Context?
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

            btnCancel.setOnClickListener {
                lifecycleScope.launch {
                    deleteReservation(reservation)
                }
            }
        }

        private suspend fun deleteReservation(reservation: appdatabase.RezervacijaVeterinara) {
            withContext(Dispatchers.IO) {
                database.rezervacijaVeterinaraQueries.obrisiRezervaciju(reservation.rezervacijaID)
            }
            rezervacijeVet.remove(reservation)

            Toast.makeText(context, "Rezervacija uspješno otkazana", Toast.LENGTH_SHORT).show()

            notifyDataSetChanged()
        }
    }
}
