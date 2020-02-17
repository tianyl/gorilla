# Ohjelmarungon tuonti Eclipseen
Kloonaa ja tuo projekti Eclipseen käyttämällä Eclipsen Smart Import -työkalua. Ohjattu toiminto löytyy valikosta File > Import > Git > Projects from Git (Smart Import). Toiminto kloonaa projektin koneellesi sekä tuo projektin Eclipseen Maven-konfiguraation mukaisesti. Jos ajat ohjelman Eclipsessä käyttäen "Java Application" -kohtaa, valitse Main-metodiksi `Main - fi.utu.tech.distributed.gorilla`.

# Pelin toiminta
Pelin säännöt ovat yksinkertaiset: kentällä on n gorillaa, jotka heittelevät banaaneja tietyssä kulmassa tietyllä nopeudella toisiaan päin. Mikäli banaani osuu, gorilla kuolee. Viimeinen eloonjäänyt gorilla on voittaja. Peli on jaettu aikarajoitettuihin kierroksiin, jonka aikana kaikki pelaajat päättävät siirtonsa. Kierroksen lopuksi (aika päättynyt tai kaikki gorillat ovat tehneet siirtonsa) gorillat heittävät banaaninsa yhtäaikaisesti. Kierroksen aikana pelissä on aina tietty sää (esimerkiksi tuuli), joka vaikuttaa heittoon.

Käynnistyessään peli toistaa lyhyen Intron ja siirtyy tämän jälkeen valikkotilaan, ellei käyttäjä tätä ennen pakota tilanmuutosta käyttöliittymän painikkeilla.

## Kontrollit
Peliä ohjataan graafisen käyttöliittymän painikkeilla, näppäimistöllä sekä tekstipohjaisilla komennoilla.

### Päävalikon kontrollit
- Nuolinäppäimet + Enter

Huomioi, että päävalikon kontrollit **eivät toimi**, mikäli tekstinsyöttökenttä on valittuna! Napsauta tulostekenttää, jotta fokus siirtyy pois syöttökentästä.


### Alaosan painikkeet
- Intro: Käynnistä pelin intro uudelleen
- Menu: Siirry suoraan päävalikkon
- Game: Siirry pelitilaan esimerkiksi päävalikosta tai introsta
- <<, =, >>: Pysäytä kamera tai ohjaa sitä vasemmalle tai oikealle
- 0: Kohdista kamera omaan pelaajaan
- Restart: Käynnistä koko peli uudelleen - kova restart, sillä koko logiikka alustetaan
- Exit: Poistu pelistä

### Komennot
Komennot syötetään oikean alakulman tekstikenttään. 
- q, quit, exit: Poistu ohjelmasta
- s, chat, say *viesti*: Puhu chattiin
- a, k, angle, kulma: Aseta heittokulma asteina
- v, n, velocity, nopeus: Aseta heittonopeus ja heitä banaani

# Tehtävänanto
Tässä harjoitustyössä on kolme päätehtävää, joista ensimmäinen on ohjelmointiharjoitus ja kaksi viimeistä selvitystehtäviä. Kahteen viimeiseen vastaukset kirjoitetaan [SELVITYS.md-tidostoon](../SELVITYS.md) pääkansiossa. Selvitystehtävät kannattaa tehdä ohjelmointitehtävän jälkeen.

Tehtävät:
1. Toteuta moninpeli (ml. chat) annettuun Gorillapeliin käyttämällä Mesh-tyylistä[^1] verkotusta applikaatiotasolla
2. Mesh-toteutus on mahdollista toteuttaa nopeasti käyttäen Javan ObjectStreameja, jolloin verkon yli välittyy yksittäisiä serialisoituja olioita. Tämä on kuitenkin abstraktion luoma "illuusio", sillä TCP-socketit kuljettavat matalammalla tasolla pelkkää datavirtaa, eivätkä siis pysty erottamaan missä kohtaa serialisoitu olio päättyy ja mistä seuraava alkaa. Oletetaan olevan yksittäisiä resursseja, kuten serialisoituja olioita, jotka halutaan kuljettaa verkon yli, ilman olemassa olevaa ObjectStreamin kaltaista abstraktiota. Miten viestitte/merkkaatte vastaanottajalle yksittäisten resurssien rajat? (Vinkki: Voitte selvittää, miten osa olemassa olevista protokollista ratkaisee tämän ongelman.)
3. Kerro lyhyesti muutama esimerkkiskenaario, mitä tietoturvaan liittyviä ongelmia Mesh-viestien välitys epäluotettujen solmujen kautta (eli siis toisten pelaajien, mahdollisesti toisen ohjelmabinäärin kautta) tuottaa ja miten ongelmia olisi mahdollista ratkaista.[^2] (Vinkki: digital signature, public key cryptography, Diffie–Hellman key exchange). Vastauksessa ei tarvitse keskittyä salausalgoritmien sisäiseen toimintaan/matemaattiseen teoriaan. 

## Ensimmäisen tehtävän vaiheet ja haasteet
Ensimmäinen tehtävä on tehtävistä aikaavievin ja täten pilkottu kolmeen vaiheeseen helpottamaan työssä etenemistä. Näitä vaiheita ei tarvitse palauttaa erikseen tai muutoin eritellä. Riittää, että lopullinen ohjelma palautetaan. Jossakin vaiheessa saattaa olla tarpeen myös muokata edellisessä vaiheessa kirjoitettua koodia, mutta tässä esitetty järjestys auttaa kuitenkin suunnittelemaan oman työn aikataulutusta.

### Mesh-verkkokerroksen rakentaminen (35%)
Mesh-kerroksen tulisi olla irrallaan pelitoiminnalisuudesta (ts. sen luokkien ei tulisi riippua Gorillaluokista). Sen tehtäväksi siis jää pelkästään datan lähettäminen, välittäminen ja vastaanottaminen (ilman duplikaatteja) muilta solmuilta ("nodeilta"). Myöhemmissä vaiheessa moninpeli toteutetaan tämän Mesh-kerroksen päälle. Haasteena tässä vaiheessa on opetella luomaan ja käyttämään säikeitä sockettien ja Input- ja OutputStreamien kanssa, sekä harjoittaa säikeiden välistä synkronointia ja säieturvallisten rakenteiden käyttöä. Tämän lisäksi on päätettävä, missä muodossa Mesh-viestit välitetään verkon yli (mitä otsakedataa viesteihin liitetään) ja miten ne serialisoidaan.

Tarkemmin Mesh-verkosta, vaatimuksista sekä ehdotetusta toteutuksesta on kerrottu [omalla sivullansa](mesh.md).

### Chatin toimintaansaatto käyttäen Mesh-toteutusta (15 %)
Käytetään edellisen vaiheen Mesh-toteutusta pelin Chat-viestien välittämiseen. Haasteena on tutustua pelin ohjelmarunkoon riittävästi, jotta Mesh-palvelin saadaan käynnistettyä sekä yhdistettyä olemassa olevaan Mesh-verkkoon ja `ChatMessage`-oliot lähetettyä käyttöliittymää käyttäen. Myös vastaanotetut Chat-viestit tulee saada tulostettua vastaanottajan päässä. Riittää, että viestit saadaan lähetettyä kaikille (yksityisviestitukea ei tarvitse implementoida).

Ohjelman kulkua, sekä tarkemmat vaatimukset on selvitetty [omalla sivullansa](program-flow-chat.md).

### Joulu
Joululoman alkaessa olisi hyvä, että vähintään Mesh-toteutus olisi jotakuinkin toiminnassa. Chat-toiminnallisuuden lisääminen ei tähän päälle ole aikataulullisesti iso tehtävä.

### Pelin toimintaansaatto (50 %)
Pelin loppuosan toimintaansaatto on suurin yksittäinen osakokonaisuus. Tässä laajennetaan verkkotoiminnallisuus koko peliin käyttäen hyväksi edellisssä Chatin implementoinnissa opittuja tekniikoita. Haasteena on osata välittää dataa käyttöliittymäsäikeelle säieturvallisesti ilman, että käyttöliittymäsäie jumiutuu tai tietorakenteet rikkoontuvat. On myös tärkeää selvittää, mitkä oliot tulee siirtää verkon ylitse, ja mitkä puolestaan pystytään johtamaan pienemmästä määrästä dataa.

Tarkempi ohjeistus [omalla sivullansa](mesh-to-game.md).

Huomatkaa vielä kerran, että tämä tehtävä on melko laaja ja sisältää paljon eri osia. Tämän vuoksi on ensiarvoisen tärkeää aloittaa tehtävän teko **nyt** ja kysyä tarvittaessa neuvoa tai varmistusta omalle ratkaisulle harjoitustyöpajoissa!

[^1]: Verkko, johon uusi jäsen voi liittyä kenen tahansa jäsenen kautta. Tästä tarkemmin Mesh-verkon toteutuksesta puhuttaessa.
[^2]: Mikäli kiinnostut enemmän, ks. [distributed-crypto](https://gitlab.utu.fi/tech/education/distributed-systems/distributed-crypto)

*Tämän pelin kehityksen aikana ei vahingoitettu oikeita gorilloja. Muutama koodiapina on tosin saattanut menettää yöuniaan.*
