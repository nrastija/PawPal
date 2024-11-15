package com.example.pawpal.f12_shop

import com.example.pawpal.f12_shop.entiteti.Proizvod

object KosaricaManager {
    private val kosarica: MutableList<Proizvod> = mutableListOf()

    fun dodajProizvodLista(proizvod: Proizvod) {
        kosarica.add(proizvod)
    }
    fun dohvatiProizvodeLista(): List<Proizvod> {
        return kosarica
    }

    fun obrisiProizvodLista(proizvod: Proizvod) {
        kosarica.remove(proizvod)
    }

    fun povecajKolicinuList(proizvod: Proizvod) {
        proizvod.kolicina++;
    }

    fun smanjiKolicinuList(proizvod: Proizvod) {
        proizvod.kolicina--;
        if (proizvod.kolicina == 0){
            obrisiProizvodLista(proizvod);
        }
    }

    fun izracunajCijenuLista(){

    }

    fun isprazniKosaricuLista() {
        kosarica.clear()
    }
}
