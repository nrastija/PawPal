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
        paszahtjevID: Long)

    suspend fun dohvatiZadnjuRezervaciju(): Zahtjevudomljavanje?
}


