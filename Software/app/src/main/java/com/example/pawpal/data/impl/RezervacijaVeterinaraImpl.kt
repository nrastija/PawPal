package com.example.pawpal.data.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.RezervacijaVeterinara
import com.example.pawpal.data.datasource.RezervacijaVeterinaraDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RezervacijaVeterinaraImpl(db: AppDatabase) : RezervacijaVeterinaraDataSource {
    private val queries = db.rezervacijaVeterinaraQueries

    override suspend fun dodajRezervaciju(
        veterinarID: Long,
        korisnikID: Long,
        datum: String,
        vrijeme: String,
        uslugaID: Long,
        dodatniOpis: String
    ) {
        withContext(Dispatchers.IO) {
            queries.dodajRezervaciju(
                korisnikID = korisnikID,
                veterinarID = veterinarID,
                uslugaID = uslugaID,
                datum = datum,
                vrijeme = vrijeme,
                dodatniOpis = dodatniOpis
            )
        }
    }
    override suspend fun dohvatiSveRezervacije(): Flow<List<RezervacijaVeterinara>> {
        return queries.dohvatiSveRezervacije().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun dohvatiRezervacijeZaKorisnika(korisnikID: Long): Flow<List<RezervacijaVeterinara>> {
        return queries.dohvatiRezervacijeKorisnika(korisnikID).asFlow().mapToList(context = Dispatchers.IO)
    }
    override suspend fun dohvatiRezervacijeZaVeterinara(veterinarID: Long): Flow<List<RezervacijaVeterinara>> {
        return queries.dohvatiRezervacijeVeterinara(veterinarID).asFlow().mapToList(context = Dispatchers.IO)
    }
    override suspend fun obrisiRezervacijuPoID(rezervacijaID: Long) {
        withContext(Dispatchers.IO) {
            queries.obrisiRezervaciju(rezervacijaID)
        }
    }
}