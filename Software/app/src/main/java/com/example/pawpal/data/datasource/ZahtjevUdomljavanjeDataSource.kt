package com.example.pawpal.data.datasource

import appdatabase.Narudzba
import appdatabase.Zahtjevudomljavanje
import kotlinx.coroutines.flow.Flow

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

    suspend fun dohvatiZahtjevePoIdKlijenta(klijentId: Long): Flow<List<Zahtjevudomljavanje>>
}


