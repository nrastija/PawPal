package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.VrstaUsluge
import com.example.pawpal.data.datasource.VeterinarUslugaDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class VeterinarUslugaImpl(db:AppDatabase) : VeterinarUslugaDataSource {

    private val queries = db.vrstaUslugeQueries

    override suspend fun dodajUslugu(nazivUsluge: String, cijena: String) {
        withContext(Dispatchers.IO) {
            queries.dodajVrstuUsluge(
                nazivUsluge = nazivUsluge,
                cijena = cijena
            )
        }
    }

    override suspend fun dohvatiSveUsluge(): Flow<List<VrstaUsluge>> {
        return queries.dohvatiSveUsluge().asFlow().mapToList(context = Dispatchers.IO)
    }
    override suspend fun obrisiUsluguPoID(uslugaID: Long) {
        withContext(Dispatchers.IO) {
            queries.izbrisiUsluguID(uslugaID)
        }
    }

}