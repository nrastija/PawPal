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
Aplikacija PawPal podržava različite funkcionalnosti prema ulogama u sustavu: administrator i klijent:

- Administrator: Administrator ima pristup naprednim funkcionalnostima i alatima za upravljanje aplikacijom, korisnicima, financijama i uslugama. Njegova uloga uključuje nadzor, organizaciju i upravljanje sustavom kako bi aplikacija učinkovito funkcionirala i pružila visokokvalitetnu uslugu svim korisnicima, uključujući upravljanje korisničkim računima kroz kreiranje, deaktivaciju i brisanje računa, dodavanje, ažuriranje i uklanjanje usluga i cijena unutar centra.

- Korisnik: Korisnici imaju funkcionalnosti namijenjene brizi za pse, rezervacijama i pregledavanju pasa za udomljavanje. Aplikacija im omogućuje praćenje termina i korištenje usluga centra. Glavne funkcionalnosti korisnika uključuju registraciju, prijavu i upravljanje profilom za vlastite potrebe i profil ljubimca, prijavu izgubljenih pasa, rezervaciju termina u spa salonu za tretmane kao što su šišanje, četkanje, rezanje noktiju i uređivanje te mogućnost odabira paketa usluga, zakazivanje termina kod veterinara, praćenje vlastitih troškova i posjeta različitim uslugama, pregled i prijavu za udomljavanje pasa kao i prijavu psa u školu za pse. Korisnici također imaju pristup online trgovini za kupovinu proizvoda.

Oznaka | Naziv | Kratki opis | Odgovorni član tima
------ | ----- | ----------- | -------------------
F01 | Registracija, prijava, odjava i brisanje korisničkog računa | Za pristup aplikaciji korisnik se prijavljuje već postojećim korisničkim računom unosom korisničkog imena i lozinke ako ga ima, ukoliko ga nema mora provesti postupak registracije tako što unosi svoje osobne podatke poput imena, prezimena, korisničkog imena, lozinke i drugih podataka. Korisnik ima mogućnost odjave iz aplikacije i ima mogućnost obrisati korisnički račun ukoliko više ne želi koristiti aplikaciju. Administrator ima korisnički račun kojim ima mogućnost deaktivirati postojeće korisničke račune. | Mirta Vuković
F02 | Prijava izgubljenih pasa | Omogućava korisnicima da unesu podatke o izgubljenom psu, uključujući fotografiju, opis i zadnju viđenu lokaciju. Korisnici koji primijete psa mogu putem aplikacije kontaktirati vlasnika i poslati informacije o lokaciji. Aplikacija automatski šalje obavijest osobi ako joj je netko poslao poruku. | Petra Skoko
F03 | Pseći spa salon | Administrator može kreirati promo događanja u salonu na koji se korisnici mogu prijaviti. Uz to, ima mogućnost kreiranja novih paketa i promo akcija, unošenje novih usluga psećeg spa salona te ažuriranje i brisanje postojećih. Korisnik ima mogućnost rezervacije termina za uređivanje svog ljubimca. To uključuje: termin šišanja, termin četkanja, termin rezanja noktiju i termin općenitog uljepšavanja. Korisnik ima uvid u slobodne termine i cijene određenih usluga. | Mirta Vuković
F04 | Pregled kod veterinara | Korisnik ima mogućnost rezervacije termina kod ovlaštenog veterinara kojeg može sam odabrati sa liste svih veterinara uz unos simptoma ili potrebe dolaska (npr. cijepljenje, kontrola zdravlja…). Korisnik ima uvid u slobodne termine i cijene određenih usluga. | Petra Skoko
F05 | Financijsko upravljanje | Omogućava administratorima praćenje popularnosti određenih usluga centra. Uz to, imaju mogućnost uvida u djelovanje online trgovine te ukupne zarade centra. Korisnici mogu pratiti vlastite troškove u određenom vremenskom okviru te imaju uvid koliko puta su posjetili određene dijelove centra. | Nensi Vugrinec
F06 | Upravljanje udomljavanjem pasa | Administrator ima mogućnost nadgledanja svih prijava za udomljavanje pasa, uključujući: pregled i odobravanje prijava za udomljavanje pasa, upravljanje profilima pasa za udomljavanje (dodavanje novih pasa, ažuriranje podataka o postojećim psima, uklanjanje pasa kada su udomljeni). Uz to ima mogućnost komunikacije s korisnicima koji su zainteresirani za udomljavanje, uključujući slanje automatskih obavijesti o statusu njihove prijave. | Petra Skoko
F07 | Škola za pse | Korisnik može upisati svog ljubimca u školu za pse u specijaliziranim centrima (osnovni trening, napredni trening, socijalizacija itd.) koji su vođeni profesionalcima uz pomoć volontera. Korisnik ima uvid u profil voditelja, termine i cijene usluga. Korisnik ima mogućnost slaganja "wish liste" pomoću koje slaže vrstu dresure po prioritetu. Korisnik dobiva poruku nakon što mu se ulazak u jednu od odabranih škola odobri. | Nensi Vugrinec
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
