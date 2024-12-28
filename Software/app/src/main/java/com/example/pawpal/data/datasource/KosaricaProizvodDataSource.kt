package com.example.pawpal.data.datasource

interface KosaricaProizvodDataSource {
    suspend fun insertProizvod(kosaricaId: Long, proizvodId: Long, kolicina: Long)

    suspend fun azurirajKolicinu(kosaricaId: Long, proizvodId: Long, kolicina: Long)

    suspend fun brisanjeProizvodaKosarice(kosaricaId: Long)

    suspend fun provjeriPostojanje(proizvodId: Long)
}