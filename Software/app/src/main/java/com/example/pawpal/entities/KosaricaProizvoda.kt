package com.example.pawpal.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.pawpal.f12_shop.entiteti.Proizvod

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