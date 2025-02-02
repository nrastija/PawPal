package com.example.pawpal.remote

data class Kategorija(
    val kategorijaID: Int,
    val naziv: String
)

// Odgovor za GET
data class KategorijaResponse(
    val status: String,
    val category: Kategorija
)
