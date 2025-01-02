package com.example.pawpal.data.datasource

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
}


