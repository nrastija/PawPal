package com.example.pawpal.data.datasource

import appdatabase.Skola

interface WishlistDataSource {
    suspend fun addToWishlist(skolaId: Long, korisnikID: Long)
    suspend fun removeFromWishlist(skolaId: Long, korisnikID: Long)
    suspend fun getAllWishlistItems(korisnikID: Long): List<Skola>
    suspend fun isSkolaInWishlist(skolaId: Long, korisnikID: Long): Boolean
}
