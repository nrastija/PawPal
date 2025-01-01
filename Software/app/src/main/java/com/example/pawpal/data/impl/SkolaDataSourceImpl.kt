package com.example.pawpal.data.impl

import appdatabase.Skola
import com.example.pawpal.data.datasource.SkolaDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SkolaDataSourceImpl(db: AppDatabase) : SkolaDataSource {
    private val queries = db.skolaQueries

    override suspend fun dohvatiSkoluPoId(skolaId: Long): Skola? {
        return withContext(Dispatchers.IO) {
            queries.dohvatiSkoluPoId(skolaId).executeAsOneOrNull()
        }
    }

    override suspend fun dohvatiSveSkole(): List<Skola> {
        return withContext(Dispatchers.IO) {
            queries.dohvatiSveSkole().executeAsList()
        }
    }

    override suspend fun insertSkola(skolaId: Long, naziv: String, opis: String, cijena: Double, termin: String) {
        withContext(Dispatchers.IO) {
            queries.insertSkola(skolaId, naziv, opis, cijena, termin)
        }
    }

    override suspend fun obrisiSveSkole() {
        withContext(Dispatchers.IO) {
            queries.deleteAllSkole()
        }
    }
}