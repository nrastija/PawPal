package com.example.pawpal.data.session

object KorisnikManager {

    private var trenutniKorisnikID: Long? = null

    fun ulogiranKorisnik(korisnikID: Long) {
        trenutniKorisnikID = korisnikID
    }

    fun dajUlogiranogKorisnika(): Long? {
        return trenutniKorisnikID
    }

    fun odjava() {
        trenutniKorisnikID = null
    }
}
