package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Kosarica
import com.example.pawpal.data.datasource.KosaricaDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class KosaricaDataSourceImpl(db: AppDatabase) : KosaricaDataSource {
    private val queries = db.kosaricaQueries

    override fun dohvatikosarice(): Flow<List<Kosarica>> {
        return queries.sveKosarice().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun insertKosarica(korisnikId: Long) {
        queries.InsertKosarica(korisnikId)
    }

    override suspend fun provjeriPostojanje(korisnikId: Long): Kosarica? {
        return withContext(Dispatchers.IO){
            queries.provjeriPostojanje(korisnikId).executeAsOneOrNull()
        }
    }
}