# PawPal

## Projektni tim

Ime i prezime | E-mail adresa (FOI) | JMBAG | Github korisničko ime | Seminarska grupa
------------  | ------------------- | ----- | --------------------- | ----------------
Niko Rastija | nrastija22@foi.hr | 0016159047 | nrastija22foi | G2
Petra Skoko | pskoko22@foi.hr | 0016159874 | pskoko22 | G2
Nensi Vugrinec | nvugrinec22@foi.hr | 0016158989 | nvugrinec22 | G2
Mirta Vuković | mvukovic22@foi.hr | 0016158765 | mvukovic22 | G2

## Opis domene
PawPal je aplikacija koja pomaže vlasnicima pasa u brizi o njihovim ljubimcima I promiče udomljavanje. Korisnici mogu lako rezervirati grooming, šetnje sa šetačima i dnevne boravke, te pregledavati dostupne pse za udomljavanje s filtrima prema različitim kriterijima. Aplikacija omogućuje kreiranje profila, praćenje i ocjenjivanje usluga, te sudjelovanje u volonterskim aktivnostima. PawPal također nudi edukativne sadržaje o njezi i treningu. Intuitivno sučelje čini je idealnim alatom za sve ljubitelje pasa. 

## Specifikacija projekta
Aplikacija PawPal nudi korisnicima širok spektar funkcionalnosti za brigu o psima i promicanje udomljavanja. Korisnici mogu lako rezervirati grooming, šetnje sa šetačima i dnevne boravke, dok pregledavaju dostupne pse za udomljavanje uz različite filtre. Korisnikove funkcionalnosti podrazumijevaju kreiranje profila, praćenje i ocjenjivanje usluga te sudjelovanje u volonterskim aktivnostima.

Oznaka | Naziv | Kratki opis | Odgovorni član tima
------ | ----- | ----------- | -------------------
F01 | Registracija i prijava korisničkog računa | Za pristup aplikaciji korisnik se prijavljuje već postojećim korisničkim računom unosom korisničkog imena i lozinke ako ga ima, ukoliko ga nema mora provesti postupak registracije tako što unosi svoje osobne podatke poput imena, prezimena, korisničkog imena, lozinke i drugih podataka. | Mirta Vuković
F02 | Odjava i brisanje računa | Korisnik ima mogućnost odjave iz aplikacije i ima mogućnost obrisati korisnički račun ukoliko više ne želi koristiti aplikaciju. | Petra Skoko
F03 | Pseći spa salon | Korisnik ima mogućnost rezervacije termina za uređivanje svog ljubimca. To uključuje: termin šišanja, termin četkanja, termin rezanja noktiju i termin općenitog uljepšavanja. Korisnik ima uvid u slobodne termine i cijene određenih usluga. | Mirta Vuković
F04 | Pregled kod veterinara | Korisnik ima mogućnost rezervacije termina kod ovlaštenog veterinara kojeg može sam odabrati sa liste svih veterinara uz unos simptoma ili potrebe dolaska (npr. cijepljenje, kontrola zdravlja…). Korisnik ima uvid u slobodne termine i cijene određenih usluga.  | Petra Skoko
F05 | Doggy daycare (Dnevni boravak za pse) | Korisnik ima mogućnost rezervacije “dnevnog boravka za pse”. Čuvanje psa može biti cjelodnevno ili poludnevno. Korisnik ima mogućnost vidjeti koliko je zauzetih mjesta te ukoliko nema mjesta ne može odabrati taj termin. Korisnik ima uvid u cijene usluga. | Nensi Vugrinec
F06 | Šetnja psa | Korisnik može rezervirati šetnju za svoje pse s profesionalnim šetačima ili volonterima. Korisnik može postaviti termin, odabrati šetača, trajanje šetnje i dodatnih usluga poput hranjenja nakon šetnje. Nakon rezervacije termina dobiva obavijest o prihvaćanju ili odbijanju termina od strane šetača. | Petra Skoko
F07 | Škola za pse | Korisnik može upisati svog ljubimca u školu za pse u specijaliziranim centrima (osnovni trening, napredni trening, socijalizacija itd.) koji su vođeni profesionalcima uz pomoć volontera. Korisnik ima uvid u profil voditelja, termine i cijene usluga. | Nensi Vugrinec
F08 | Notifikacije i podsjetnici | Korisnik dobiva obavijesti i podsjetnike za nadolazeće rezervacije poput nadolazećeg termina za šišanje psa, šetanje psa i slično. Korisnik ima mogućnost uključiti opciju da dobiva obavijesti kada mora svog psa(ukoliko ga ima) voditi na medicinski tretman odnosno na godišnje preglede i cjepljenja. | Niko Rastija
F09 | Udomljavanje pasa | Korisnik može vidjeti popis svih pasa za udomljavanje te može vidjeti detalje o svakom psu poput imena, dobi, pasmine, rođendana, veterinarske obrade i slično. Ukoliko korisnik želi udomiti psa treba ispuniti obrazac sa svojim podacima te ga šalje i čeka odgovor odgovorne osobe za udomljavanje pasa. | Mirta Vuković
F10 | Pregled rezervacija | Korisnik ima mogućnost pregleda svih rezervacija koje je napravio te ima mogućnost pogledati detalje rezevacije. Korisnik ima mogućnost otkazati rezervaciju ukoliko mu ne odgovara. | Niko Rastija
F11 | Korisnički profil i profil psa | Korisnik ima mogućnost uređivanja svog profila i mjenjanje svojih podataka. Korisnik ima mogućnost izradu profila za svog psa gdje unosi osnovne podatke o svom psu poput dobi, pasmine i ostalih podataka. | Nensi Vugrinec
F12 | Online trgovina | Korisnik ima mogućnost kupovati proizvode poput hrane za pse, igračaka, opreme za njegu, lijekova i slično preko online trgovine. Korisnik ima mogućnost filtriranja i sortiranja proizvoda te mogućnost biranja opcije plaćanja i načina dostave proizvoda. | Niko Rastija

## Tehnologije i oprema
Za implementaciju naše aplikacije za brigu o psima i udomljavanje, koristit ćemo ovdje navedene tehnologije, alate i opremu. Aplikacija će biti razvijena za Android uređaje u programskom jeziku Kotlin. Za razvoj koristiti će se IDE Android Studio, verzija Koala. 

Za upravljanje verzijama koda, koristit ćemo Git u kombinaciji s platformom GitHub u našem projektu.
Sav rad na tehničkoj i projektnoj dokumentaciji bit će obavljen putem GitHub Wiki, dok će koncepti planiranja i praćenja projektnog napretka biti vođeni kroz GitHub Projects (aspekt Project managementa).

## Baze podataka i web server
Za bazu podataka koristili bi lokalnu bazu podataka pomoću Room library-ja u Kotlinu. Room pruža sloj apstrakcije preko SQLitea.

## .gitignore
Koristit ćemo unaprijed definirani .gitignore file koji se nalazi u Software folderu.
