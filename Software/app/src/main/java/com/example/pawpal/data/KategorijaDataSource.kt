package com.example.pawpal.data

interface KategorijaDataSource {
    suspend fun dohvatiNazivPoId(kategorijaId: Long): String

    suspend fun insertKategorija(kategorijaId: Long, naziv: String)
}