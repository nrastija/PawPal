package com.example.pawpal.data.datasource

import kotlinx.coroutines.flow.Flow
import appdatabase.Usluga


interface UslugaDataSource {

    fun dohvatiSveUsluge(): Flow<List<Usluga>>
    suspend fun dohvatiUsluguPoID (uslugaID: Long) : Usluga?

    suspend fun insertUsluga(
        naziv: String,
        cijena: Double,
        opis: String,
        trajanje: Long,
        imageUrl: String,
    )
}