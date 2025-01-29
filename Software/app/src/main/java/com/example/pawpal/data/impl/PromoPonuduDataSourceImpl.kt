package com.example.pawpal.data.impl

import PromoPonudaDataSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Promoponuda
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PromoPonuduDataSourceImpl(db: AppDatabase) : PromoPonudaDataSource {
    private val queries = db.promoPonudaQueries
    override suspend fun dohvatiPromoPonuduPoID(promoponudaID: Long): Promoponuda? {
        return withContext(Dispatchers.IO){
            queries.dohvatiPromoPonuduPoID(promoponudaID).executeAsOneOrNull()
        }
    }

    override fun dohvatiSvePonude(): Flow<List<Promoponuda>> {
        return queries.dohvatiSvePonude().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun obrisiPromoPonuduPoID(promoponudaID: Long) {
        withContext(Dispatchers.IO) {
            queries.obrisiPromoPonuduPoID(promoponudaID)
        }
    }

    override suspend fun insertPromoPonuda(
        naziv: String,
        opis: String,
        datumValjanosti: String,
        uvjeti: String
    ) {
        queries.insertPromoPonuda(naziv, opis, datumValjanosti, uvjeti)
    }
}