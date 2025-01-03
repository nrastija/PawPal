package com.example.pawpal.data.impl

import appdatabase.Voditelj
import com.example.pawpal.data.datasource.VoditeljDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoditeljDataSourceImpl(db: AppDatabase) : VoditeljDataSource {
    private val queries = db.voditeljQueries

    override suspend fun dohvatiSveVoditelje(): List<Voditelj> {
        return withContext(Dispatchers.IO) {
            queries.dohvatiSveVoditelje().executeAsList()
        }
    }

    override suspend fun insertVoditelj(
        voditeljId: Long,
        ime: String,
        prezime: String,
        email: String,
        telefon: String
    ) {
        withContext(Dispatchers.IO) {
            queries.insertVoditelj(voditeljId, ime, prezime, email, telefon)
        }
    }

    override suspend fun obrisiSveVoditelje() {
        withContext(Dispatchers.IO) {
            queries.deleteAllVoditelji()
        }
    }

    override suspend fun dohvatiVoditeljeZaSkolu(skolaId: Long): List<Voditelj> {
        return withContext(Dispatchers.IO) {
            queries.dohvatiVoditeljeZaSkolu(skolaId).executeAsList()
        }
    }

    override suspend fun dohvatiVoditeljaPoId(voditeljId: Long): Voditelj? {
        return withContext(Dispatchers.IO) {
            queries.dohvatiVoditeljaPoId(voditeljId).executeAsOneOrNull()
        }
    }

}
