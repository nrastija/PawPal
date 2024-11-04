package com.example.pawpal.f12_shop.baza

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pawpal.f12_shop.entiteti.KategorijaProizvoda
import com.example.pawpal.f12_shop.entiteti.Proizvod

@Database(
    entities = [Proizvod::class, KategorijaProizvoda::class],
    version = 1,
    exportSchema = false
)

abstract class ProizvodDB : RoomDatabase() {

    abstract val dao: ProizvodDAO
}