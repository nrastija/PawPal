package com.example.pawpal.data

import appdatabase.Kosarica
import com.example.pawpal.f12_shop.entiteti.Proizvod
import kotlinx.coroutines.flow.Flow

interface KosaricaProizvodDataSource {
    suspend fun insertProizvod(kosaricaId: Long, proizvodId: Long, kolicina: Long)

    suspend fun azurirajKosaricu(kosaricaId: Long, proizvodId: Long, kolicina: Long)

    suspend fun brisanjeProizvodaKosarice(kosaricaId: Long)

}