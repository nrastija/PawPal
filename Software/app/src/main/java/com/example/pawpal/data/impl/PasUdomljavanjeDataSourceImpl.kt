package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Pasudomljavanje
import com.example.pawpal.data.datasource.PasUdomljavanjeDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PasUdomljavanjeDataSourceImpl(db: AppDatabase) : PasUdomljavanjeDataSource {
    val queries = db.pasUdomljavanjeQueries
    override suspend fun dohvatiPsaPoImenu(ime: String): Pasudomljavanje? {
        return withContext(Dispatchers.IO){
            queries.dohvatiPsaPoImenu(ime).executeAsOneOrNull()
        }
    }

    override suspend fun dohvatiPsaPoID(pasudomljavanjeID: Long): Pasudomljavanje? {
        return withContext(Dispatchers.IO){
            queries.dohvatiPsaPoID(pasudomljavanjeID).executeAsOneOrNull()
        }
    }

    override fun dohvatiSvePse(): Flow<List<Pasudomljavanje>> {
        return queries.dohvatiSvePse().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun dodajPasUdomljavanje(
        ime: String,
        dob: Long,
        spol: String,
        opis: String,
        datumRodenja: String,
        kilaza: Double,
        pasmina: String,
        dodatneInfo: String,
        cijepiva: String,
        imageUrl: String,
        imageUrl2: String,
        imageUrl3: String
    ) {
        withContext(Dispatchers.IO) {
            queries.dodajPasUdomljavanje(
                ime, dob, spol, opis, datumRodenja,
                kilaza, pasmina, dodatneInfo,
                cijepiva, imageUrl, imageUrl2, imageUrl3
            )
        }
    }

    override suspend fun azurirajpsaudomljavanje(
        pasudomljavanjeID: Long,
        ime: String,
        dob: Long,
        spol: String,
        opis: String,
        datumRodenja: String,
        kilaza: Double,
        pasmina: String,
        dodatneInfo: String,
        cijepiva: String,
        imageUrl: String,
        imageUrl2: String,
        imageUrl3: String
    ) {
        withContext(Dispatchers.IO) {
            queries.azurirajpsaudomljavanje(
                ime, dob, spol, opis, datumRodenja, kilaza,
                pasmina, dodatneInfo, cijepiva,
                imageUrl, imageUrl2, imageUrl3, pasudomljavanjeID
            )
        }
    }

    override suspend fun obrisipsaudomljavanje(pasudomljavanjeID: Long) {
        withContext(Dispatchers.IO) {
            queries.obrisipsaudomljavanje(pasudomljavanjeID)
        }
    }

}