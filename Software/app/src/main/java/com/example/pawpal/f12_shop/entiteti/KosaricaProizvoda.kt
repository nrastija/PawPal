package com.example.pawpal.f12_shop.entiteti

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "kosarica_proizvoda",
    foreignKeys = [ForeignKey(
        entity = Proizvod::class,
        parentColumns = ["proizvodID"],
        childColumns = ["proizvodId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class KosaricaProizvoda(
    @PrimaryKey(autoGenerate = true) val kosaricaId: Int = 0,
    val proizvodId: Int,
    val kolicina: Int
)