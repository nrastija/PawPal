package com.example.pawpal.data.impl

import com.example.pawpal.data.datasource.NarudzbaDataSource
import com.pawpal.appdatabase.AppDatabase
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
}