package com.example.pawpal.data.datasource

import appdatabase.IzgubljeniPsi
import appdatabase.RezervacijaVeterinara
import kotlinx.coroutines.flow.Flow

interface IzgubljeniPsiDataSource {

    suspend fun dodajIzgubljenogPsa(ime: String, opis: String, lokacija: String, slikaUri: String, korisnikid: Long)
    suspend fun dohvatiSveIzgubljenePse(): Flow<List<IzgubljeniPsi>>
    suspend fun obrisiIzgubljenogPsa(id:Long)
    suspend fun dohvatiZadnjuRezervaciju(): IzgubljeniPsi?
}