package com.example.pawpal.data.impl

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Korisnik
import com.example.pawpal.data.datasource.KorisnikDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class KorisnikDataSourceImpl(db: AppDatabase) : KorisnikDataSource {
    private val queries = db.korisnikQueries

    override suspend fun dajKorisnikaPoID(korisnikID: Long): Korisnik? {
        return withContext(Dispatchers.IO) {
            queries.dajKorisnikaPoID(korisnikID).executeAsOneOrNull()
        }
    }

    override suspend fun dajKorisnikaPoKorime(korime: String): Korisnik? {
        return withContext(Dispatchers.IO) {
            queries.dajKorisnikaPoKorime(korime).executeAsOneOrNull()
        }
    }

    override fun dajSveKorisnike(): Flow<List<Korisnik>> {
        return queries.dajSveKorisnike().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun obrisiKorisnikaPoID(korisnikID: Long) {
        withContext(Dispatchers.IO){
            queries.obrisiKorisnikaPoID(korisnikID)
        }
    }

    override suspend fun obrisiAdmina() {
        withContext(Dispatchers.IO) {
            queries.obrisiAdmina()
        }
    }

    override suspend fun dodajKorisnik(
        korime: String,
        ime: String,
        prezime: String,
        email: String,
        lozinka: String,
        tip_korisnika: Long
    ) {
        withContext(Dispatchers.IO){
            queries.dodajKorisnik(korime, ime, prezime, lozinka, email, tip_korisnika)
        }
    }

    override suspend fun azurirajKorisnika(
        korisnikID: Long,
        korime: String,
        ime: String,
        prezime: String,
        email: String
    ) {
        withContext(Dispatchers.IO) {
            queries.azurirajKorisnika(
                korisnikID = korisnikID,
                korime = korime,
                ime = ime,
                prezime = prezime,
                email = email
            )
        }
    }

    override suspend fun azurirajLozinku(korisnikID: Long, novaLozinka: String)  {
        withContext(Dispatchers.IO) {
            queries.azurirajLozinku(
                korisnikID = korisnikID,
                novaLozinka = novaLozinka
            )
        }
    }


}