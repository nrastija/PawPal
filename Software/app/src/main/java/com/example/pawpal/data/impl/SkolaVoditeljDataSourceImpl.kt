package com.example.pawpal.data.impl

import com.example.pawpal.data.datasource.SkolaVoditeljDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SkolaVoditeljDataSourceImpl(db: AppDatabase) : SkolaVoditeljDataSource {
    private val queries = db.skolaVoditeljQueries

    override suspend fun poveziSkoluIVoditelja(skolaId: Long, voditeljId: Long) {
        withContext(Dispatchers.IO) {
            queries.insertSkolaVoditelj(skolaId, voditeljId)
        }
    }

    override suspend fun obrisiSveSkolaVoditelj() {
        withContext(Dispatchers.IO) {
            queries.deleteAllSkolaVoditelj()
        }
    }
}
