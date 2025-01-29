package com.example.pawpal.data.impl

import com.example.pawpal.data.datasource.NarudzbaProizvodDataSource
import com.pawpal.appdatabase.AppDatabase

class NarudzbaProizvodDataSourceImpl(db: AppDatabase) : NarudzbaProizvodDataSource {
    val queries = db.narudzbaProizvodQueries

    override suspend fun insertProizvodUNarudzbu(
        narudzbaId: Long,
        proizvodId: Long,
        kolicina: Long
    ) {
        queries.insertProizvodUNarudzbu(narudzbaId, proizvodId, kolicina)
    }
}