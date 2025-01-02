import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import appdatabase.Voditelj
import com.example.pawpal.R
import com.example.pawpal.adapters.VoditeljAdapter
import com.example.pawpal.data.impl.VoditeljDataSourceImpl
import com.example.pawpal.main.DatabaseConsumer
import com.example.pawpal.ui.VoditeljDetaljiFragment
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.launch

class SkolaDetaljiFragment : Fragment(), DatabaseConsumer {
    private var skolaId: Long? = null
    override lateinit var database: AppDatabase
    private lateinit var recyclerVoditelji: RecyclerView
    private val voditeljiList = mutableListOf<Voditelj>()

    companion object {
        private const val ARG_SKOLA_ID = "skolaId"

        fun newInstance(skolaId: Long): SkolaDetaljiFragment {
            return SkolaDetaljiFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_SKOLA_ID, skolaId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        skolaId = arguments?.getLong(ARG_SKOLA_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f07_detalji_skole, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerVoditelji = view.findViewById(R.id.recyclerVoditelji)
        recyclerVoditelji.layoutManager = LinearLayoutManager(requireContext())
        recyclerVoditelji.adapter = VoditeljAdapter(voditeljiList) { voditelj ->
            navigateToVoditeljDetaljiFragment(voditelj)
        }

        skolaId?.let { id ->
            val skola = database.skolaQueries.dohvatiSkoluPoId(id).executeAsOneOrNull()
            skola?.let { prikaziPodatke(view, it) }
        }
    }

    private fun prikaziPodatke(view: View, skola: appdatabase.Skola) {
        view.findViewById<TextView>(R.id.podatakNaziv).text = skola.naziv
        view.findViewById<TextView>(R.id.podatakOpis).text = skola.opis
        view.findViewById<TextView>(R.id.podatakCijena).text = "${skola.cijena} €"
        view.findViewById<TextView>(R.id.podatakTermin).text = skola.termin

        val voditeljDataSource = VoditeljDataSourceImpl(database)

        lifecycleScope.launch {
            val voditelji = voditeljDataSource.dohvatiVoditeljeZaSkolu(skola.skolaID)
            voditeljiList.clear()
            voditeljiList.addAll(voditelji)
            recyclerVoditelji.adapter?.notifyDataSetChanged()
        }
    }

    private fun navigateToVoditeljDetaljiFragment(voditelj: Voditelj) {
        val fragment = VoditeljDetaljiFragment.newInstance(voditelj.voditeljID)
        fragment.database = database
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

}
