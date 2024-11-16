package com.example.pawpal.f12_shop.entiteti

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategorija_proizvoda")
data class KategorijaProizvoda(
    @PrimaryKey(autoGenerate = true) val kategorijaID: Int = 0,
    val naziv: String
)
