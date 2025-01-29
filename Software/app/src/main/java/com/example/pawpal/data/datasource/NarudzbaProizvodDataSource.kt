package com.example.pawpal.data.datasource

interface NarudzbaProizvodDataSource {
    suspend fun insertProizvodUNarudzbu(narudzbaId: Long, proizvodId: Long, kolicina: Long)
}