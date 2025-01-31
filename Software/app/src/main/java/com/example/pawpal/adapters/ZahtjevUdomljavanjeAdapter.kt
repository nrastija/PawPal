import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Pasudomljavanje
import appdatabase.Usluga
import com.example.pawpal.R
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ZahtjevUdomljavanjeAdapter(
    private val zahtjeviUdomljavanje: List<appdatabase.Zahtjevudomljavanje>,
    private val onCancelClick: (Any) -> Unit,
    private val database: AppDatabase,
    private val lifecycleScope: androidx.lifecycle.LifecycleCoroutineScope
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
        val ime: TextView = view.findViewById(R.id.adoptionAnimalName)
        val pasmina: TextView = view.findViewById(R.id.adoptionAnimalRace)
        val opis: TextView = view.findViewById(R.id.adoptionAnimalDescription)
        val btnCancel: Button = view.findViewById(R.id.adoptionCancelButton)

        fun bind(reservation: appdatabase.Zahtjevudomljavanje) {
            lifecycleScope.launch {
                val pas = getDogInfo(reservation.paszahtjevID)
                ime.text = pas.ime
                pasmina.text = pas.pasmina
                opis.text = pas.opis

                btnCancel.setOnClickListener { onCancelClick(reservation) }
            }
        }
    }

    private suspend fun getDogInfo(pasId: Long): Pasudomljavanje {
        return withContext(Dispatchers.IO) {
            database.pasUdomljavanjeQueries.dohvatiPsaPoID(pasId).executeAsOne()
        }
    }
}
