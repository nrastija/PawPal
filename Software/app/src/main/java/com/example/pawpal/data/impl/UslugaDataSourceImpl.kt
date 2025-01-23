package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Pasudomljavanje
import appdatabase.Usluga
import com.example.pawpal.data.datasource.UslugaDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UslugaDataSourceImpl(db: AppDatabase) : UslugaDataSource {

    private val queries = db.uslugaQueries

    override fun dohvatiSveUsluge(): Flow<List<Usluga>> {
        return queries.dohvatiSveUsluge().asFlow().mapToList(context = Dispatchers.IO)

    }

    override suspend fun dohvatiUsluguPoID(uslugaID: Long): Usluga? {
        return withContext(Dispatchers.IO){
            queries.dohvatiUsluguPoID(uslugaID).executeAsOneOrNull()
        }
    }

    override suspend fun insertUsluga(
        naziv: String,
        cijena: Double,
        opis: String,
        trajanje: Long,
        imageUrl: String
    ) {
        withContext(Dispatchers.IO) {
            queries.insertUsluga(
                naziv,
                cijena,
                opis,
                trajanje,
                imageUrl
            )
        }
    }
}
