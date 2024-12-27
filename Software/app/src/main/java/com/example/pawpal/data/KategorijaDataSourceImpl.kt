package com.example.pawpal.data

import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KategorijaDataSourceImpl(db: AppDatabase) : KategorijaDataSource {
    val queries = db.kategorijaQueries
    override suspend fun dohvatiNazivPoId(kategorijaId: Long): String {
        return withContext(Dispatchers.IO){
            queries.dohvatiNazivKategorije(kategorijaId).executeAsOneOrNull().toString()
        }
    }

    override suspend fun insertKategorija(kategorijaId: Long, naziv: String) {
       queries.insertKategorija(kategorijaId, naziv)
    }

}