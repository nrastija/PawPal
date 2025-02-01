import android.util.Log
import com.example.pawpal.remote.Kategorija
import com.example.pawpal.remote.KategorijaResponse
import com.example.pawpal.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiServiceHelper {

    fun getAllKategorije(onSuccess: (List<Kategorija>) -> Unit, onError: (String) -> Unit) {
        RetrofitClient.instance.getAllKategorije().enqueue(object : Callback<List<Kategorija>> {
            override fun onResponse(call: Call<List<Kategorija>>, response: Response<List<Kategorija>>) {
                if (response.isSuccessful) {
                    val kategorije = response.body()
                    if (kategorije != null) {
                        onSuccess(kategorije)
                    } else {
                        onError("No data found")
                    }
                } else {
                    onError("Failed to fetch data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Kategorija>>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

    fun getKategorijaById(kategorijaID: Int, onSuccess: (Kategorija) -> Unit, onError: (String) -> Unit) {
        RetrofitClient.instance.getKategorijaById(kategorijaID).enqueue(object : Callback<KategorijaResponse> {
            override fun onResponse(call: Call<KategorijaResponse>, response: Response<KategorijaResponse>) {
                if (response.isSuccessful) {
                    val kategorijaResponse = response.body()
                    if (kategorijaResponse != null && kategorijaResponse.status == "success") {
                        val kategorija = kategorijaResponse.category
                        Log.d("responseBody", "Kategorija response $kategorija")
                        onSuccess(kategorija)
                    } else {
                        onError("No data found or invalid status")
                    }
                } else {
                    onError("Failed to fetch data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<KategorijaResponse>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }


    fun addKategorija(kategorija: Kategorija, onSuccess: (Kategorija) -> Unit, onError: (String) -> Unit) {
        RetrofitClient.instance.addKategorija(kategorija).enqueue(object : Callback<Kategorija> {
            override fun onResponse(call: Call<Kategorija>, response: Response<Kategorija>) {
                if (response.isSuccessful) {
                    val newKategorija = response.body()
                    if (newKategorija != null) {
                        onSuccess(newKategorija)
                    } else {
                        onError("No data returned")
                    }
                } else {
                    onError("Failed to add data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Kategorija>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

    fun updateKategorija(kategorijaID: Int, kategorija: Kategorija, onSuccess: (Kategorija) -> Unit, onError: (String) -> Unit) {
        RetrofitClient.instance.updateKategorija(kategorijaID, kategorija).enqueue(object : Callback<Kategorija> {
            override fun onResponse(call: Call<Kategorija>, response: Response<Kategorija>) {
                if (response.isSuccessful) {
                    val updatedKategorija = response.body()
                    if (updatedKategorija != null) {
                        onSuccess(updatedKategorija)
                    } else {
                        onError("No data returned")
                    }
                } else {
                    onError("Failed to update data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Kategorija>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

    fun deleteKategorija(kategorijaID: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        RetrofitClient.instance.deleteKategorija(kategorijaID).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to delete data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }
}