package com.example.pawpal.data
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import appdatabase.Proizvod
import com.pawpal.appdatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProizvodDataSourceImpl(db: AppDatabase) : ProizvodDataSource{

    private val queries = db.proizvodQueries
    override suspend fun dohvatiProizvodPoId(proizvodId: Long): Proizvod? {
        return withContext(Dispatchers.IO){
            queries.dohvatiProizvodPoId(proizvodId).executeAsOneOrNull()
        }
    }

    override fun dohvatiProizvode(): Flow<List<Proizvod>> {
        return queries.dohvatiProizvode().asFlow().mapToList(context = Dispatchers.IO)
    }

    override fun filtrirajProizvodePoKategoriji(kategorijaId: Long): Flow<List<Proizvod>> {
        return queries.filtrirajProizvodePoKategoriji(kategorijaId).asFlow().mapToList(context = Dispatchers.IO)
    }

    override suspend fun insertProizvod(
        naziv: String,
        cijena: Double,
        opis: String,
        imageUrl: String,
        kategorijaID: Long?
    ) {
        if (kategorijaID != null) {
            queries.insertProizvod(naziv, cijena, opis, imageUrl, kategorijaID)
        }
    }

}