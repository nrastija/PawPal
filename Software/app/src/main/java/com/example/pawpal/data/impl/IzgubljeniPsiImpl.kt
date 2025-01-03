package com.example.pawpal.data.impl

import android.net.Uri
import android.util.Base64
import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.IzgubljeniPsi
import com.example.pawpal.data.datasource.IzgubljeniPsiDataSource
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class IzgubljeniPsiImpl(db: AppDatabase) : IzgubljeniPsiDataSource {
    private val queries = db.izgubljeniPsiQueries

    override suspend fun dodajIzgubljenogPsa(opis: String, lokacija: String, slikaUri: String) {
        Log.d("Velicina slike", slikaUri.length.toString())

        withContext(Dispatchers.IO){
            queries.unesiNovogPsa(
                description = opis,
                lastseenlocation = lokacija,
                imageUri = slikaUri
            )
        }
    }



    override suspend fun dohvatiSveIzgubljenePse(): Flow<List<IzgubljeniPsi>> {
        Log.d("Usao sam u fkju dohvatipse", "")
        val psiList = queries.dohvatiSvePse().executeAsList()
        Log.d("Provera podataka", "Podaci iz baze: $psiList")
            return queries.dohvatiSvePse().asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun obrisiIzgubljenogPsa(pasId: Long) {
        withContext(Dispatchers.IO){
            queries.izbrisiPsa(pasId)
        }
    }

}