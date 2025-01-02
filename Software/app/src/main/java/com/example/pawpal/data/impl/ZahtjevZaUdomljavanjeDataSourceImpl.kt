package com.example.pawpal.data.impl
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Proizvod
import com.example.pawpal.data.datasource.ZahtjevUdomljavanjeDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ZahtjevZaUdomljavanjeDataSourceImpl(db: AppDatabase) : ZahtjevUdomljavanjeDataSource {

    private val queries = db.zahtjevUdomljavanjeQueries
    override suspend fun insertZahtjev(
        ime: String,
        prezime: String,
        email: String,
        telefon: String,
        drugiLjubimci: String,
        clanObitelji: String,
        iskustvoSPsima: String,
        dodatneInformacije: String,
        paszahtjevID: Long
    ) {
        withContext(Dispatchers.IO) {
            queries.insertZahtjev(
                paszahtjevID,
                ime,
                prezime,
                email,
                telefon,
                drugiLjubimci,
                clanObitelji,
                iskustvoSPsima,
                dodatneInformacije
            )
        }
    }
}
