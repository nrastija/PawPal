package com.example.pawpal.data.datasource

interface KosaricaProizvodDataSource {
    suspend fun insertProizvod(kosaricaId: Long, proizvodId: Long, kolicina: Long)

    suspend fun azurirajKosaricu(kosaricaId: Long, proizvodId: Long, kolicina: Long)

    suspend fun brisanjeProizvodaKosarice(kosaricaId: Long)

}