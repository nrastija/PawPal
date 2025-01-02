package com.example.pawpal.data.datasource

import appdatabase.Skola

interface WishlistDataSource {
    suspend fun addToWishlist(skolaId: Long, korisnikID: Long, prioritet: Long)
    suspend fun removeFromWishlist(skolaId: Long, korisnikID: Long)
    suspend fun updateWishlistPrioritet(skolaId: Long, korisnikID: Long, prioritet: Long)
    suspend fun getAllWishlistItemsWithPriorities(korisnikID: Long): List<Pair<Skola, Long>>
    suspend fun isSkolaInWishlist(skolaId: Long, korisnikID: Long): Boolean
}
