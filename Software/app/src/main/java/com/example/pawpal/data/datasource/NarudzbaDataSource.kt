package com.example.pawpal.data.datasource

import java.util.Date

interface NarudzbaDataSource {
    suspend fun insertNarudzba(korisnikId: Long, ukupnaCijena: Double, datum: String, nacinPlacanja: String)
}