package com.example.pawpal.data.datasource

import appdatabase.Korisnik
import kotlinx.coroutines.flow.Flow

interface KorisnikDataSource {

    suspend fun dajKorisnikaPoID (korisnikID:Long) : Korisnik?
    suspend fun dajKorisnikaPoKorime (korime: String) : Korisnik?


    fun dajSveKorisnike(): Flow<List<Korisnik>>

    suspend fun obrisiKorisnikaPoID(korisnikID:Long)

    suspend fun dodajKorisnik(korime: String, ime: String,
                              prezime: String, email: String, lozinka: String)

    suspend fun azurirajKorisnika(korisnikID: Long, korime: String, ime: String, prezime: String, email: String)

    suspend fun azurirajLozinku(korisnikID: Long, novaLozinka: String)
}