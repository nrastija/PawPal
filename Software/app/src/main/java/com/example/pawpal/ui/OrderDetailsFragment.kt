package com.example.pawpal.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import appdatabase.Narudzba
import appdatabase.NarudzbaProizvod
import com.example.pawpal.main.DatabaseConsumer
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderDetailsFragment : Fragment(), DatabaseConsumer {

    override lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var orderDateTextView: TextView
    private lateinit var paymentMethodTextView: TextView
    private lateinit var orderItems: List<NarudzbaProizvod>
    private var orderId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            orderId = it.getLong(ARG_ORDER_ID)
        }
    }

    companion object {
        private const val ARG_ORDER_ID = "orderId"

        fun newInstance(orderId: Long): OrderDetailsFragment {
            val fragment = OrderDetailsFragment()
            val args = Bundle().apply {
                putLong(ARG_ORDER_ID, orderId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f10_order_details, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_order_details)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        totalPriceTextView = view.findViewById(R.id.orderTotalPrice)
        orderDateTextView = view.findViewById(R.id.orderDate)
        paymentMethodTextView = view.findViewById(R.id.orderPaymentMethod)

        fetchOrderDetails()

        return view
    }

    private fun fetchOrderDetails() {
        lifecycleScope.launch {
            try {
                val order = getOrderDetails(orderId)
                val products = getOrderProducts(orderId)

                if (order != null) {
                    totalPriceTextView.text = "Cijena: ${order.ukupnaCijena}"
                    orderDateTextView.text = "Datum: ${order.datum}"
                    paymentMethodTextView.text = "Način plačanja: ${order.nacinPlacanja}"
                }


                val adapter = OrderDetailsAdapter(products, database, lifecycleScope = lifecycleScope)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun getOrderDetails(orderId: Long): Narudzba? {
        return withContext(Dispatchers.IO) {
            val order = database.narudzbaQueries.dohvatiNarudzbu(orderId).executeAsOneOrNull()
            return@withContext order
        }
    }

    private suspend fun getOrderProducts(orderId: Long): List<NarudzbaProizvod> {
        return withContext(Dispatchers.IO) {
            Log.d("OrderDetailsFragment", "Fetching products for orderId: $orderId")
            val products = database.narudzbaProizvodQueries.dohvatiProizvodeNarudzbePoId(orderId).executeAsList()
            Log.d("OrderDetailsFragment", "Products fetched: ${products.size} items")
            return@withContext products
        }
    }
}
