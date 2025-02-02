package com.example.pawpal.data.datasource

import appdatabase.Pasudomljavanje
import kotlinx.coroutines.flow.Flow

interface PasUdomljavanjeDataSource {

    suspend fun dohvatiPsaPoImenu (ime:String) : Pasudomljavanje?
    suspend fun dohvatiPsaPoID (pasudomljavanjeID: Long) : Pasudomljavanje?

    fun dohvatiSvePse(): Flow<List<Pasudomljavanje>>

    suspend fun dodajPasUdomljavanje(
        ime: String,
        dob: Long,
        spol:String ,
        opis: String,
        datumRodenja: String,
        kilaza: Double,
        pasmina: String,
        dodatneInfo: String,
        cijepiva: String,
        imageUrl: String,
        imageUrl2: String,
        imageUrl3: String
    )

    suspend fun azurirajpsaudomljavanje(
        pasudomljavanjeID: Long,
        ime: String,
        dob: Long,
        spol: String,
        opis: String,
        datumRodenja: String,
        kilaza: Double,
        pasmina: String,
        dodatneInfo: String,
        cijepiva: String,
        imageUrl: String,
        imageUrl2: String,
        imageUrl3: String
    )

    suspend fun obrisipsaudomljavanje(id: Long)
}