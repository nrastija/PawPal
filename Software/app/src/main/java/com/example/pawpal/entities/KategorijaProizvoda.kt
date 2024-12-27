package com.example.pawpal.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kategorija_proizvoda")
data class KategorijaProizvoda(
    @PrimaryKey(autoGenerate = true) val kategorijaID: Int = 0,
    val naziv: String
)
