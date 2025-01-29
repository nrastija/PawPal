package com.example.pawpal.data.datasource

interface SkolaVoditeljDataSource {
    suspend fun poveziSkoluIVoditelja(skolaId: Long, voditeljId: Long)
    suspend fun obrisiSveSkolaVoditelj()
}
