import appdatabase.Promoponuda
import kotlinx.coroutines.flow.Flow

interface PromoPonudaDataSource {

    suspend fun dohvatiPromoPonuduPoID(promoponudaID: Long): Promoponuda?

    fun dohvatiSvePonude(): Flow<List<Promoponuda>>

    suspend fun obrisiPromoPonuduPoID(promoponudaID: Long)

    suspend fun insertPromoPonuda(
        naziv: String,
        opis: String,
        datumValjanosti: String,
        uvjeti: String
    )
}
