package com.example.pawpal.data.impl

import com.example.pawpal.data.datasource.KosaricaProizvodDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class KosaricaProizvodDataSourceImpl(db: AppDatabase) : KosaricaProizvodDataSource {
    private val queries = db.kosaricaProizvodQueries

    override suspend fun insertProizvod(kosaricaId: Long, proizvodId: Long, kolicina: Long) {
        queries.dodajProizvodUKosaricu(kosaricaId, proizvodId, kolicina)
    }

    override suspend fun azurirajKosaricu(kosaricaId: Long, proizvodId: Long, kolicina: Long) {
        queries.azurirajKosaricu(kosaricaId, proizvodId, kolicina)
    }

    override suspend fun brisanjeProizvodaKosarice(kosaricaId: Long) {
        withContext(Dispatchers.IO){
            queries.brisanjeKosarice(kosaricaId)
        }
    }

}