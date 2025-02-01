package com.example.pawpal.data.datasource

import appdatabase.Zahtjevudomljavanje

interface ZahtjevUdomljavanjeDataSource {

    suspend fun insertZahtjev(
        ime: String,
        prezime: String,
        email: String,
        telefon: String,
        drugiLjubimci: String,
        clanObitelji : String,
        iskustvoSPsima: String,
        dodatneInformacije: String,
        paszahtjevID: Long,
        korisnikID: Long)

    suspend fun dohvatiZadnjuRezervaciju(): Zahtjevudomljavanje?

    suspend fun dohvatiBrojZahtjevaKorisnika(korisnikID: Long): Long
    suspend fun dohvatiBrojZahtjevaSvihKorisnika(): Long
}


