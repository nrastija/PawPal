package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.RezervacijaTermina
import com.example.pawpal.data.datasource.RezervacijaTerminaUslugeDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RezervacijaTerminaUslugeImpl(db: AppDatabase) : RezervacijaTerminaUslugeDataSource {
    private val queries = db.rezervacijaTerminaUslugeQueries
    override suspend fun dodajUslugu(
        korisnikID: Long,
        datum: String,
        vrijeme: String,
        uslugaID: Long,
        napomene: String
    ) {
        withContext(Dispatchers.IO) {
            queries.dodajRezervaciju(
                korisnikID = korisnikID,
                datum = datum,
                vrijeme = vrijeme,
                uslugaID = uslugaID,
                napomene = napomene
            )
        }
    }

    override suspend fun dohvatiSveRezervacije(): Flow<List<RezervacijaTermina>> {
        return queries.dohvatiSveRezervacije().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun dohvatiRezervacijeZaKorisnika(korisnikID: Long): Flow<List<RezervacijaTermina>> {
        return queries.dohvatiRezervacijeKorisnika(korisnikID).asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun obrisiRezervacijuPoID(rezervacijaTerminID: Long) {
        withContext(Dispatchers.IO) {
            queries.obrisiRezervaciju(rezervacijaTerminID)
        }    }

    override suspend fun dohvatiZadnjuRezervaciju(): RezervacijaTermina? {
        return withContext(Dispatchers.IO) {
            queries.dohvatiZadnjuRezervaciju().executeAsOneOrNull()
        }
    }
}