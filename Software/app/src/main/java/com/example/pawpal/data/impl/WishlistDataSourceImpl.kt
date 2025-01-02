package com.example.pawpal.data.impl

import appdatabase.Skola
import com.example.pawpal.data.datasource.WishlistDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishlistDataSourceImpl(private val db: AppDatabase) : WishlistDataSource {
    private val wishlistQueries = db.wishlistQueries
    private val skolaQueries = db.skolaQueries

    override suspend fun addToWishlist(skolaId: Long, korisnikID: Long, prioritet: Long) {
        withContext(Dispatchers.IO) {
            wishlistQueries.insertWishlistItem(skolaId, korisnikID, prioritet)
        }
    }

    override suspend fun removeFromWishlist(skolaId: Long, korisnikID: Long) {
        withContext(Dispatchers.IO) {
            wishlistQueries.deleteWishlistItem(skolaId, korisnikID)
        }
    }

    override suspend fun updateWishlistPrioritet(skolaId: Long, korisnikID: Long, prioritet: Long) {
        withContext(Dispatchers.IO) {
            wishlistQueries.updateWishlistPrioritet(prioritet, skolaId, korisnikID)
        }
    }

    override suspend fun getAllWishlistItemsWithPriorities(korisnikID: Long): List<Pair<Skola, Long>> {
        return withContext(Dispatchers.IO) {
            val wishlistItems = wishlistQueries.dohvatiSveWishlistItems(korisnikID).executeAsList()
            wishlistItems.mapNotNull { (skolaID, prioritet) ->
                val skola = skolaQueries.dohvatiSkoluPoId(skolaID).executeAsOneOrNull()
                skola?.let { it to prioritet }
            }
        }
    }

    override suspend fun isSkolaInWishlist(skolaId: Long, korisnikID: Long): Boolean {
        return withContext(Dispatchers.IO) {
            wishlistQueries.dohvatiSkoluIzWishlista(skolaId, korisnikID).executeAsOneOrNull() != null
        }
    }
}
