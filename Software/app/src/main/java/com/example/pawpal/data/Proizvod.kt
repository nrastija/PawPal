package com.example.pawpal.f12_shop.entiteti

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.example.pawpal.entities.KategorijaProizvoda

@Entity(
    tableName = "proizvod",
    foreignKeys = [ForeignKey(
        entity = KategorijaProizvoda::class,
        parentColumns = ["kategorijaID"],
        childColumns = ["kategorijaID"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class Proizvod(
    @PrimaryKey(autoGenerate = true) val proizvodID: Int = 0,
    val naziv: String,
    val cijena: Double,
    val opis: String,
    val kategorijaID: Int,
    var kolicina: Int,
    val imageUrl: String? = null
)