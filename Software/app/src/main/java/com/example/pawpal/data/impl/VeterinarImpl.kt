package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Veterinari
import com.example.pawpal.data.datasource.VeterinarDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VeterinarImpl(db: AppDatabase) : VeterinarDataSource {
    private val queries = db.veterinarQueries


    override suspend fun dodajVeterinara(imePrezime: String, specijalizacija: String, kontakt: String,) {
        withContext(Dispatchers.IO) {
            queries.dodajVeterinara(
                imePrezime = imePrezime,
                specijalizacija = specijalizacija,
                kontakt = kontakt
            )
        }
    }

    override suspend fun dohvatiSveVeterinare(): Flow<List<Veterinari>> {
        return queries.dohvatiSveVeterinare().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun dohvatiVeterinaraPoID(veterinarID: Long): Veterinari? {
        return withContext(Dispatchers.IO) {
            queries.dohvatiVeterinaraID(veterinarID).executeAsOneOrNull()
        }
    }

    override suspend fun obrisiVeterinaraPoID(veterinarID: Long) {
        withContext(Dispatchers.IO) {
            queries.izbrisiVeterinaraID(veterinarID)
        }
    }
}