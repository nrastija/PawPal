package com.example.pawpal.data.impl
import com.example.pawpal.data.datasource.ZahtjevUdomljavanjeDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ZahtjevUdomljavanjeDataSourceImpl(db: AppDatabase) : ZahtjevUdomljavanjeDataSource {

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
