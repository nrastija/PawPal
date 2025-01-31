import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import appdatabase.RezervacijaTermina
import appdatabase.Usluga
import com.example.pawpal.R
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Adapter za SPA rezervacije

class SpaPrikazRezervacijaAdapter(
    private val rezervacijeSPA: MutableList<RezervacijaTermina>,
    private val onCancelClick: (Any) -> Unit,
    private val database: AppDatabase,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val context: Context?
) : RecyclerView.Adapter<SpaPrikazRezervacijaAdapter.SpaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f10_spa_reservation, parent, false)
        return SpaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpaViewHolder, position: Int) {
        holder.bind(rezervacijeSPA[position])
    }

    override fun getItemCount(): Int = rezervacijeSPA.size

    inner class SpaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val naziv: TextView = view.findViewById(R.id.spaServiceName)
        val datum: TextView = view.findViewById(R.id.spaServiceDate)
        val napomene: TextView = view.findViewById(R.id.spaServiceNotes)
        val btnCancel: Button = view.findViewById(R.id.spaServiceCancel)

        @SuppressLint("SetTextI18n")
        fun bind(reservation: appdatabase.RezervacijaTermina) {
            lifecycleScope.launch {
                val usluga = getSPAService(reservation.uslugaID)
                naziv.text = usluga.naziv

                datum.text = reservation.datum + reservation.vrijeme
                napomene.text = usluga.cijena.toString() + "€"

                btnCancel.setOnClickListener {
                    lifecycleScope.launch {
                        deleteReservation(reservation)
                    }
                }
            }
        }
    }

    private suspend fun getSPAService(uslugaId: Long): Usluga {
        return withContext(Dispatchers.IO) {
            database.uslugaQueries.dohvatiUsluguPoID(uslugaId).executeAsOne()
        }
    }

    private suspend fun deleteReservation(reservation: appdatabase.RezervacijaTermina) {
        withContext(Dispatchers.IO) {
            database.rezervacijaTerminaUslugeQueries.obrisiRezervaciju(reservation.rezervacijaTerminID)
        }
        rezervacijeSPA.remove(reservation)

        Toast.makeText(context, "Rezervacija uspješno otkazana", Toast.LENGTH_SHORT).show()

        notifyDataSetChanged()
    }
}

