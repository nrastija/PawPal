package com.example.pawpal.data.impl

import appdatabase.Pas
import com.example.pawpal.data.datasource.PasDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasDataSourceImpl(db: AppDatabase) : PasDataSource {
    private val queries = db.pasQueries

    override suspend fun dohvatiPasPoKorisnikID(korisnikID: Long): Pas? {
        return withContext(Dispatchers.IO) {
            queries.dohvatiPasPoKorisnikID(korisnikID).executeAsOneOrNull()
        }
    }

    override suspend fun insertPas(
        ime: String,
        dob: String,
        pasmina: String,
        spol: String,
        kilaza: String,
        korisnikID: Long
    ) {
        withContext(Dispatchers.IO) {
            queries.insertPas(ime, dob, pasmina, spol, kilaza, korisnikID)
        }
    }

    override suspend fun obrisiPasPoKorisnikID(korisnikID: Long) {
        withContext(Dispatchers.IO) {
            queries.obrisiPasPoKorisnikID(korisnikID)
        }

        }
}
