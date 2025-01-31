package com.example.pawpal.data.datasource

import appdatabase.Narudzba
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface NarudzbaDataSource {
    suspend fun insertNarudzba(korisnikId: Long, ukupnaCijena: Double, datum: String, status: String, nacinPlacanja: String)

    suspend fun dohvatiNarudzbu(narudzbaId: Long): Narudzba?

    suspend fun dohvatiNarudzbePoIdKlijenta(klijentId: Long): Flow<List<Narudzba>>
}