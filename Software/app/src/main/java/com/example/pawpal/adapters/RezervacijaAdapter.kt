import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R

class RezervacijaAdapter(
    private val rezervacijeSPA: List<appdatabase.RezervacijaTermina>,
    private val rezervacijeVet: List<appdatabase.RezervacijaVeterinara>,
    private val narudzbeShop: List<appdatabase.Narudzba>,
    private val zahtjeviUdomljavanje: List<appdatabase.Zahtjevudomljavanje>,
    private val onCancelClick: (Any) -> Unit,
    private val onInfoClick: (Any) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SPA = 0
        const val TYPE_VET = 1
        const val TYPE_SHOP = 2
        const val TYPE_ADOPTION = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SPA -> SpaViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.f10_spa_reservation, parent, false))
            TYPE_VET -> VetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.f10_vet_reservation, parent, false))
            TYPE_SHOP -> ShopViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.f10_shop_orders, parent, false))
            TYPE_ADOPTION -> AdoptionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.f10_adoption_reservation, parent, false))
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SpaViewHolder -> {
                if (position < rezervacijeSPA.size) {
                    holder.bind(rezervacijeSPA[position])
                }
            }
            is VetViewHolder -> {
                val vetPosition = position - rezervacijeSPA.size
                if (vetPosition < rezervacijeVet.size) {
                    holder.bind(rezervacijeVet[vetPosition])
                }
            }
            is ShopViewHolder -> {
                val shopPosition = position - (rezervacijeSPA.size + rezervacijeVet.size)
                if (shopPosition < narudzbeShop.size) {
                    holder.bind(narudzbeShop[shopPosition])
                }
            }
            is AdoptionViewHolder -> {
                val adoptionPosition = position - (rezervacijeSPA.size + rezervacijeVet.size + narudzbeShop.size)
                if (adoptionPosition < zahtjeviUdomljavanje.size) {
                    holder.bind(zahtjeviUdomljavanje[adoptionPosition])
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            position < rezervacijeSPA.size -> TYPE_SPA
            position < rezervacijeSPA.size + rezervacijeVet.size -> TYPE_VET
            position < rezervacijeSPA.size + rezervacijeVet.size + narudzbeShop.size -> TYPE_SHOP
            position < rezervacijeSPA.size + rezervacijeVet.size + narudzbeShop.size + zahtjeviUdomljavanje.size -> TYPE_ADOPTION
            else -> throw IllegalArgumentException("Invalid position")
        }
    }


    override fun getItemCount(): Int {
        return rezervacijeSPA.size + rezervacijeVet.size + narudzbeShop.size + zahtjeviUdomljavanje.size
    }

    inner class SpaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val naziv: TextView = view.findViewById(R.id.spaServiceName)
        val datum: TextView = view.findViewById(R.id.spaServiceDate)
        val napomene: TextView = view.findViewById(R.id.spaServiceNotes)
        val btnCancel: Button = view.findViewById(R.id.spaServiceCancel)

        fun bind(reservation: appdatabase.RezervacijaTermina) {
            naziv.text = "Spa Usluga"
            datum.text = reservation.datum
            napomene.text = reservation.napomene

            btnCancel.setOnClickListener { onCancelClick(reservation) }
        }
    }

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

    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val naziv: TextView = view.findViewById(R.id.shopServiceName)
        val datum: TextView = view.findViewById(R.id.shopServiceDate)
        val status: TextView = view.findViewById(R.id.shopServiceStatus)
        val btnInfo: Button = view.findViewById(R.id.shopServiceInfo)

        fun bind(reservation: appdatabase.Narudzba) {
            naziv.text = "Kupovina Proizvoda" // Use data from your model if needed
            datum.text = reservation.datum
            status.text = reservation.status

            btnInfo.setOnClickListener { onInfoClick(reservation) }
        }
    }

    inner class AdoptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ime: TextView = view.findViewById(R.id.adoptionAnimalName)
        val pasmina: TextView = view.findViewById(R.id.adoptionAnimalRace)
        val opis: TextView = view.findViewById(R.id.adoptionAnimalDescription)
        val btnCancel: Button = view.findViewById(R.id.adoptionCancelButton)

        fun bind(reservation: appdatabase.Zahtjevudomljavanje) {
            ime.text = reservation.ime
            opis.text = reservation.email

            btnCancel.setOnClickListener { onCancelClick(reservation) }
        }
    }
}
