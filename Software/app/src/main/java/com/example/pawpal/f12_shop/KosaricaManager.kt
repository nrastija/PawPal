package com.example.pawpal.f12_shop

import com.example.pawpal.f12_shop.entiteti.Proizvod
import java.math.BigDecimal
import java.math.RoundingMode

object KosaricaManager {
    private val kosarica: MutableList<Proizvod> = mutableListOf()

    fun dodajProizvodLista(proizvod: Proizvod) {
        val postojeciProizvod = kosarica.find { it.proizvodID == proizvod.proizvodID }
        if (postojeciProizvod != null){
            ;postojeciProizvod.kolicina += proizvod.kolicina
        }
        else{
            kosarica.add(proizvod)
        }

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

    fun izracunajCijenuLista() : Double{
        val ukupnaCijena = kosarica.sumOf { it.kolicina * it.cijena }
        return BigDecimal(ukupnaCijena).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    fun filtrirajProizvodePoKategoriji(kategorija: Kategorija, proizvodi: List<Proizvod>): List<Proizvod> {
        return proizvodi.filter { it.kategorijaID == kategorija.id }
    }

        fun isprazniKosaricuLista() {
        kosarica.clear()
    }

}
