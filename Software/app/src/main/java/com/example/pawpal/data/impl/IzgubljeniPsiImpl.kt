package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.IzgubljeniPsi
import com.example.pawpal.data.datasource.IzgubljeniPsiDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class IzgubljeniPsiImpl(db: AppDatabase) : IzgubljeniPsiDataSource {
    private val queries = db.izgubljeniPsiQueries

    override suspend fun dodajIzgubljenogPsa(opis: String, lokacija: String, slikaUri: String) {
        withContext(Dispatchers.IO){
            queries.unesiNovogPsa(
                description = opis,
                lastseenlocation = lokacija,
                imageUri = slikaUri
            )
        }
    }

    override suspend fun dohvatiSveIzgubljenePse(): Flow<List<IzgubljeniPsi>> {
            return queries.dohvatiSvePse().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun obrisiIzgubljenogPsa(id: Long) {
        withContext(Dispatchers.IO){
            queries.izbrisiPsa(id)
        }
    }

}