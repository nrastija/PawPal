package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.DohvatiUkupneTroskove
import appdatabase.Kosarica
import appdatabase.Narudzba
import com.example.pawpal.data.datasource.NarudzbaDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
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

    override suspend fun dohvatiUkupneTroskove(korisnikId: Long): Double {
        return withContext(Dispatchers.IO) {
            queries.dohvatiUkupneTroskove(korisnikId).executeAsOneOrNull()?.ukupno ?: 0.0
        }
    }


    override suspend fun dohvatiSveNarudzbeKorisnika(korisnikId: Long): List<Narudzba> {
        return withContext(Dispatchers.IO) {
            queries.dohvatiNarudzbuPoKorisniku(korisnikId).executeAsList()
        }
    }

    override suspend fun dohvatiUkupneTroskoveSvihKorisnika(): Double {
        return withContext(Dispatchers.IO) {
            queries.dohvatiUkupneTroskoveSvihKorisnika().executeAsOneOrNull() ?: 0.0
        }
    }

    override suspend fun dohvatiSveNarudzbe(): List<Narudzba> {
        return withContext(Dispatchers.IO) {
            queries.dohvatiSveNarudzbe().executeAsList()
        }
    }


}