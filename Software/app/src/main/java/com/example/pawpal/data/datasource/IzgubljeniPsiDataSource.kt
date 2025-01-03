package com.example.pawpal.data.datasource

import appdatabase.IzgubljeniPsi
import kotlinx.coroutines.flow.Flow

interface IzgubljeniPsiDataSource {

    suspend fun dodajIzgubljenogPsa(opis: String, lokacija: String, slikaUri: String)
    suspend fun dohvatiSveIzgubljenePse(): Flow<List<IzgubljeniPsi>>
    suspend fun obrisiIzgubljenogPsa(id:Long)
}