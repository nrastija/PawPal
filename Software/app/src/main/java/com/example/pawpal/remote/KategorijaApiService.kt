package com.example.pawpal.remote


import retrofit2.Call
import retrofit2.http.*

//Definirane funkcije za rad s remote bazom -> tablica kategorija
interface KategorijaApiService {

    // Dohvat svih kategorija
    @GET("kategorija.php")
    fun getAllKategorije(): Call<List<Kategorija>>

    // Dohvat jedne kategorije po ID-ju
    @GET("kategorija.php")
    fun getKategorijaById(@Query("id") kategorijaID: Int): Call<KategorijaResponse>

    // Dodavanje nove kategorije
    @POST("kategorija.php")
    fun addKategorija(@Body kategorija: Kategorija): Call<Kategorija>

    // Ažuriranje postojeće kategorije
    @PUT("kategorija.php")
    fun updateKategorija(@Query("id") kategorijaID: Int, @Body kategorija: Kategorija): Call<Kategorija>

    // Brisanje kategorije
    @DELETE("kategorija.php")
    fun deleteKategorija(@Query("id") kategorijaID: Int): Call<Void>
}
