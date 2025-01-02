package com.example.pawpal.data.datasource

import appdatabase.Kosarica
import kotlinx.coroutines.flow.Flow

interface KosaricaDataSource {
    fun dohvatikosarice(): Flow<List<Kosarica>>

    suspend fun insertKosarica(korisnikId: Long)

    suspend fun provjeriPostojanje(korisnikId: Long): Kosarica?

}