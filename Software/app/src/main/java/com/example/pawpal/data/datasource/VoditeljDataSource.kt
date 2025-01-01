package com.example.pawpal.data.datasource

import appdatabase.Voditelj

interface VoditeljDataSource {
    suspend fun dohvatiSveVoditelje(): List<Voditelj>
    suspend fun insertVoditelj(
        voditeljId: Long,
        ime: String,
        prezime: String,
        email: String,
        telefon: String
    )
    suspend fun obrisiSveVoditelje()

    suspend fun dohvatiVoditeljeZaSkolu(skolaId: Long): List<Voditelj>

    suspend fun dohvatiVoditeljaPoId(voditeljId: Long): Voditelj?
}


