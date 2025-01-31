package com.example.pawpal.data.impl
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.IzgubljeniPsi
import appdatabase.Zahtjevudomljavanje
import com.example.pawpal.data.datasource.ZahtjevUdomljavanjeDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
        paszahtjevID: Long,
        korisnikID: Long
    ) {
        withContext(Dispatchers.IO) {
            queries.insertZahtjev(
                paszahtjevID,
                korisnikID,
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

    override suspend fun dohvatiZahtjevePoIdKlijenta(klijentId: Long): Flow<List<Zahtjevudomljavanje>>{
        return queries.dohvatiZahtjevePoIdKlijenta(klijentId).asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun obrisiZahtjevPoId(zahtjevId: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteZahtjevPoId(zahtjevId)
        }    }
}
