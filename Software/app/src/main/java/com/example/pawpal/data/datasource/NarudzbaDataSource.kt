package com.example.pawpal.data.datasource

import appdatabase.Narudzba
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

interface NarudzbaDataSource {
    suspend fun insertNarudzba(korisnikId: Long, ukupnaCijena: Double, datum: String, status: String, nacinPlacanja: String)

    suspend fun dohvatiNarudzbu(narudzbaId: Long): Narudzba?

    suspend fun dohvatiUkupneTroskove(korisnikId: Long): Double


    suspend fun dohvatiSveNarudzbeKorisnika(korisnikId: Long): List<Narudzba>

    suspend fun dohvatiUkupneTroskoveSvihKorisnika(): Double
    suspend fun dohvatiSveNarudzbe(): List<Narudzba>

    suspend fun dohvatiBrojNarudzbiKorisnika(korisnikId: Long): Long

    suspend fun dohvatiBrojNarudzbiSvihKorisnika(): Long

}