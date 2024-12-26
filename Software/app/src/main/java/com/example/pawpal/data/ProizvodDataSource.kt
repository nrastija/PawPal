package com.example.pawpal.data

import appdatabase.Proizvod
import kotlinx.coroutines.flow.Flow

interface ProizvodDataSource {

     fun dohvatiProizvode(): Flow<List<Proizvod>>

     fun filtrirajProizvodePoKategoriji(kategorijaId: Long): Flow<List<Proizvod>>

     suspend fun insertProizvod(naziv: String, cijena: Double, opis:String , imageUrl: String, kategorijaID: Long? = null)
}