package com.example.pawpal.remote

data class Kategorija(
    val kategorijaID: Int,
    val naziv: String
)

data class KategorijaResponse(
    val status: String,
    val category: Kategorija
)
