package com.example.pawpal.data.impl
import appdatabase.IzgubljeniPsi
import appdatabase.Zahtjevudomljavanje
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

    override suspend fun dohvatiZadnjuRezervaciju(): Zahtjevudomljavanje? {
        return withContext(Dispatchers.IO) {
            queries.dohvatiZadnjiZahtjev().executeAsOneOrNull()
        }
    }
}
