package com.example.pawpal.data.datasource

interface KategorijaDataSource {
    suspend fun dohvatiNazivPoId(kategorijaId: Long): String

    suspend fun insertKategorija(kategorijaId: Long, naziv: String)
}