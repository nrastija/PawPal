package com.example.pawpal.data.datasource

import appdatabase.RezervacijaTermina
import kotlinx.coroutines.flow.Flow

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

}