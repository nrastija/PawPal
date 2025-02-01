package com.example.pawpal.remote


import retrofit2.Call
import retrofit2.http.*

interface KategorijaApiService {

    // Dohvat svih kategorija
    @GET("kategorija")
    fun getAllKategorije(): Call<List<Kategorija>>

    // Dohvat jedne kategorije po ID-ju
    @GET("kategorija/{id}")
    fun getKategorijaById(@Path("id") kategorijaID: Int): Call<Kategorija>

    // Dodavanje nove kategorije
    @POST("kategorija")
    fun addKategorija(@Body kategorija: Kategorija): Call<Kategorija>

    // Ažuriranje postojeće kategorije
    @PUT("kategorija/{id}")
    fun updateKategorija(@Path("id") kategorijaID: Int, @Body kategorija: Kategorija): Call<Kategorija>

    // Brisanje kategorije
    @DELETE("kategorija/{id}")
    fun deleteKategorija(@Path("id") kategorijaID: Int): Call<Void>
}