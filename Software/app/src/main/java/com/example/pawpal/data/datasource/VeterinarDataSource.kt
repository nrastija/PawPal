package com.example.pawpal.data.datasource

import appdatabase.Veterinari
import kotlinx.coroutines.flow.Flow

interface VeterinarDataSource {
    suspend fun dodajVeterinara(imePrezime: String, specijalizacija: String, kontakt: String)
    suspend fun dohvatiSveVeterinare(): Flow<List<Veterinari>>
    suspend fun dohvatiVeterinaraPoID(veterinarID: Long): Veterinari?
    suspend fun obrisiVeterinaraPoID(veterinarID: Long)
}