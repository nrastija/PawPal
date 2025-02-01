package com.example.pawpal.data.datasource

import appdatabase.RezervacijaTermina
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface RezervacijaTerminaUslugeDataSource {
    suspend fun dodajUslugu(
        korisnikID: Long,
        datum: String,
        vrijeme: String,
        uslugaID: Long,
        napomene: String)
    suspend fun dohvatiSveRezervacije(): Flow<List<RezervacijaTermina>>
    suspend fun dohvatiRezervacijeZaKorisnika(korisnikID: Long): Flow<List<RezervacijaTermina>>
    suspend fun obrisiRezervacijuPoID(rezervacijaTerminID: Long)
    suspend fun dohvatiZadnjuRezervaciju(): RezervacijaTermina?
    suspend fun dohvatiUkupneTroskoveRezervacija(korisnikID: Long): Double
    suspend fun dohvatiRezervacijeKorisnikaSDetaljima(korisnikId: Long): List<Pair<String, Triple<Double, String, String>>>

    suspend fun dohvatiUkupneTroskoveRezervacijaSvihKorisnika(): Double

    suspend fun dohvatiBrojRezervacijaUslugaKorisnika(korisnikID: Long): Long

    suspend fun dohvatiBrojRezervacijaUslugaSvihKorisnika(): Long
}