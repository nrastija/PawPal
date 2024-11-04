package com.example.pawpal.f12_shop.baza

import androidx.room.Dao
import androidx.room.Query
import com.example.pawpal.f12_shop.entiteti.Proizvod
import kotlinx.coroutines.flow.Flow

@Dao
interface ProizvodDAO {

    @Query("SELECT * FROM products WHERE kategorijaID = :kategorijaId ORDER BY naziv ASC")
    fun sortirajProizvodePoKategoriji(kategorijaId: Int): Flow<List<Proizvod>>

    @Query("SELECT * FROM products ORDER BY cijena ASC")
    fun sortirajProizvodePoCijeni(kategorijaId: Int): Flow<List<Proizvod>>

    @Query("SELECT * FROM products ORDER BY naziv ASC")
    fun sortirajProizvodePoNazivuUzlazno(): Flow<List<Proizvod>>

    @Query("SELECT * FROM products ORDER BY naziv DESC")
    fun sortirajProizvodePoNazivuSilazno(): Flow<List<Proizvod>>

    @Query("SELECT * FROM products")
    fun dohvatiProizvode(): Flow<List<Proizvod>>
}
