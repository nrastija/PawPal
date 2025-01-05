package com.example.pawpal.data.datasource

import appdatabase.VrstaUsluge
import kotlinx.coroutines.flow.Flow

interface VeterinarUslugaDataSource {
    suspend fun dodajUslugu(nazivUsluge: String, cijena: String)
    suspend fun dohvatiSveUsluge(): Flow<List<VrstaUsluge>>
    suspend fun obrisiUsluguPoID(uslugaID: Long)
}