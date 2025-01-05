package com.example.pawpal.data.datasource

import appdatabase.RezervacijaVeterinara
import kotlinx.coroutines.flow.Flow

interface RezervacijaVeterinaraDataSource {
    suspend fun dodajRezervaciju(veterinarID: Long, korisnikID: Long, datum: String, vrijeme: String, uslugaID: Long, dodatniOpis: String)
    suspend fun dohvatiSveRezervacije(): Flow<List<RezervacijaVeterinara>>
    suspend fun dohvatiRezervacijeZaKorisnika(korisnikID: Long): Flow<List<RezervacijaVeterinara>>
    suspend fun dohvatiRezervacijeZaVeterinara(veterinarID: Long): Flow<List<RezervacijaVeterinara>>
    suspend fun obrisiRezervacijuPoID(rezervacijaID: Long)
}