package com.example.pawpal.data.impl

import android.util.Log
import com.example.pawpal.data.datasource.KosaricaProizvodDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class KosaricaProizvodDataSourceImpl(db: AppDatabase) : KosaricaProizvodDataSource {
    private val queries = db.kosaricaProizvodQueries

    override suspend fun insertProizvod(kosaricaId: Long, proizvodId: Long, kolicina: Long) {
        queries.dodajProizvodUKosaricu(kosaricaId, proizvodId, kolicina)
    }

    override suspend fun azurirajKolicinu(kosaricaId: Long, proizvodId: Long, kolicina: Long) {
        queries.azurirajKolicinu(kolicina, kosaricaId, proizvodId)
    }

    override suspend fun brisanjeProizvodaKosarice(kosaricaId: Long) {
        withContext(Dispatchers.IO){
            queries.brisanjeKosarice(kosaricaId)
        }
    }

    override suspend fun brisanjeProizvodaKosarice(kosaricaId: Long, proizvodId: Long) {
        withContext(Dispatchers.IO){
            queries.brisanjeProizvodaKosarice(kosaricaId, proizvodId)
        }
    }

    override suspend fun provjeriPostojanje(proizvodId: Long) {
        return withContext(Dispatchers.IO){
            queries.provjeriPostojanje(proizvodId).executeAsOneOrNull()
        }
    }

    override suspend fun dohvatiProizvodeZaKosaricu(kosaricaId: Long) {
        return withContext(Dispatchers.IO) {
            queries.dohvatiProizvodeZaKosaricu(kosaricaId).executeAsList()
        }
    }

}