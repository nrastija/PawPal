package com.example.pawpal.data.datasource

import appdatabase.Skola

interface SkolaDataSource {
    suspend fun dohvatiSkoluPoId(skolaId: Long): Skola?

    suspend fun dohvatiSveSkole(): List<Skola>

    suspend fun insertSkola(skolaId: Long, naziv: String, opis: String, cijena: Double, termin: String)

    suspend fun obrisiSveSkole()
}