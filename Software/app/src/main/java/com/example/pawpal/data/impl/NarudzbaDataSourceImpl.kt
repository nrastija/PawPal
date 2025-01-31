package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Kosarica
import appdatabase.Narudzba
import appdatabase.RezervacijaTermina
import com.example.pawpal.data.datasource.NarudzbaDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date

class NarudzbaDataSourceImpl(db: AppDatabase) : NarudzbaDataSource {
    val queries = db.narudzbaQueries

    override suspend fun insertNarudzba(
        korisnikId: Long,
        ukupnaCijena: Double,
        datum: String,
        status: String,
        nacinPlacanja: String
    ) {
        queries.insertNarudzba(korisnikId, ukupnaCijena, datum, status, nacinPlacanja)
    }

    override suspend fun dohvatiNarudzbu(narudzbaId: Long): Narudzba? {
        return withContext(Dispatchers.IO) {
            queries.dohvatiNarudzbu(narudzbaId).executeAsOneOrNull()
        }
    }

    override suspend fun dohvatiNarudzbePoIdKlijenta(klijentId: Long): Flow<List<Narudzba>> {
        return queries.dohvatiNarudzbuPoIdKorisnika(klijentId).asFlow().mapToList(context = Dispatchers.IO)
    }
}