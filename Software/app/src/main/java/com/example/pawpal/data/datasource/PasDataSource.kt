package com.example.pawpal.data.datasource


import appdatabase.Pas

interface PasDataSource {
    suspend fun dohvatiPasPoKorisnikID(korisnikID: Long): Pas?
    suspend fun insertPas(ime: String, dob: String, pasmina: String, spol: String, kilaza: String, korisnikID: Long)
    suspend fun obrisiPasPoKorisnikID(korisnikID: Long)
}
