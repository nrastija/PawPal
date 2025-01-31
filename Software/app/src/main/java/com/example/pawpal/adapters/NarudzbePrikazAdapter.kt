import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.ui.OrderDetailsFragment
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class NarudzbePrikazAdapter(
    private val narudzbeShop: List<appdatabase.Narudzba>,
    private val onInfoClick: (Any) -> Unit,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val database: AppDatabase,
    private val parentFragment: Fragment
) : RecyclerView.Adapter<NarudzbePrikazAdapter.ShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.f10_shop_orders, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        holder.bind(narudzbeShop[position])
    }
    override fun getItemCount(): Int = narudzbeShop.size

    inner class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val naziv: TextView = view.findViewById(R.id.shopServiceName)
        val datum: TextView = view.findViewById(R.id.shopServiceDate)
        val status: TextView = view.findViewById(R.id.shopServiceStatus)
        val nacinPlacanja: TextView = view.findViewById(R.id.shopPaymentType)
        val btnInfo: Button = view.findViewById(R.id.shopServiceInfo)

        fun bind(order: appdatabase.Narudzba) {
            naziv.text = "Kupovina"
            datum.text = order.datum
            status.text = order.status
            nacinPlacanja.text = order.nacinPlacanja

            btnInfo.setOnClickListener {
                lifecycleScope.launch {
                    navigateToOrderDetailsFragment(order.narudzbaID)
                }
            }
        }
    }

    private fun navigateToOrderDetailsFragment(narudzbaId: Long) {
        val fragment = OrderDetailsFragment.newInstance(narudzbaId)
        fragment.database = database

        parentFragment.parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
