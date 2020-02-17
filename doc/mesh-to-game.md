# Moninpelin toteutus Mesh-verkon päälle

## Vaatimukset
- Toisessa osakokonaisuudessa implementoidun Chatin tulostus tulee siirtää JavaFX-säikeeseen
- Meshin työskentelijäsäikiden tulee kommunikoida säieturvallisen rakenteen läpi JavaFX-säikeelle (Vinkki: threadrunner-tehtävän 4. selvitystehtävä...)
- JavaFX-säiettä ei saa blokata tiedon vastaanottamisen aikana. Lähettämisen saa tehdä JavaFX-säikeestä
- Mikäli jokin joukko pelaajista lähtee pois pelistä, tapausta ei tarvitse käsitellä. Pelissä on toki "luovutussiirto", mutta tätä ei tarvitse implementoida.
- Moninpeli tulee pystyä pelaamaan läpi (banaaneja pitää pystyä heittämään)
- Jokaisen pelaajan gorilla tulee olla samassa sijainnissa ja saman pelaajan kontrolloima kaikkien pelaajien näkymässä
- Tietoa, joka voidaan johtaa siemenluvusta, ei tule välittää verkon ylitse

## Luokat
Luokkarakennetta avattiin jo aiemmin [chatin integrointitehtävän](program-flow-chat.md) yhteydessä. Tässä selostuksessa keskityttiin enemmän käyttöliittymälogiikkaan (`GorillaLogic`-luokka) kuin pelilogiikkaan, johon loput luokat liittyvät enemmissä määrin. Luokkien metodit on dokumentoitu Javadoc-tyylisesti, mutta tässä vielä lyhyet kuvaukset luokkien käyttötarkoituksista:

### GorillaLogic
Sisältää käyttöliittymälogiikan: Eli mitä tapahtuu kun tietty komento annetaan, milloin suoritetaan peliä ja milloin valikkoa, mitä tapahtuu kun aloitetaan moninpeli jne. Kutsuu tick-metodissa myös pelitilan (`GameState`) `tick()`-metodia. Luokka jäsennelty siten, että perittäessä oleellisten funktioiden uudelleenmäärittely on helppoa: Komennot kutsuvat `handleKomennonNimi()` -tyylisiä metodeita, jotka on mahdollista perivässä luokassa yliajaa.

### GameState
Sisältää pelin tilan ja metodit, joilla tilaa muutetaan. `GorillaLogic`-luokan kutsuma `tick()`-metodi päivittää pelin sisäistä ajan etenemistä. Pelin alkutila määritetään `GameConfiguration`-oliolla ja sen pohjalta luoduin pelaajaobjektein. Luokkaan on lisätty toinen, käyttämätön konstruktori, josta saattaa olla apua moninpelin toteutuksessa.

### GameConfiguration
Sisältää tarvittavat tiedot identtisen pelitilan luontiin. Identtisellä pelikonfiguraatiolla tulisi saada tuotettua identtinen pelitila, joka myös etenee deterministisesti. [^1] Luokkaan on lisätty toinen, käyttämätön konstruktori, josta saattaa olla apua moninpelin toteutuksessa.

### GameWorld
Sisältää pelitilan luoman pelimaailman (rakennusten, gorillojen sijainnit yms.)

### Player
Pelaajan tiedot, kuten nimen ja tämän siirrot sisältävä luokka. Pelaajaoliot voidaan luoda `GameConfiguration`-luokan nimilistan pohjalta.


[^1]: GameConfiguration-oliossa on mukana siemenluku, joka annetaan satunnaislukugeneraattorille => satunnaislukugeneraattori generoi identtisiä lukuja kaikissa solmuissa, generaattorin luomaa dataa käytetään pelimaailman tilan satunnaisoperaatiohin => sama, deterministinen pelimaailma kaikilla solmuilla

## Vaiheet:
Loppupelin toimintaansaatto on suurin implementoinnin osakokonaisuuksista, joten siihen kannattaa varata eniten aikaa. Tämän kokoisessa tehtävässä on hyvä jakaa tehtävä vielä pienempiin vaiheisiin. Mikäli alkuunpääsy tuntuu hankalalta, alla lista, jonka mukaan etenemistä kannattaa suunnitella:

- Suositellaan perityn `GorillaMultiplayerLogic` -luokan toteuttamista, mikäli 2. kohdassa ei vielä luotu
- Uuden tiedon saaminen Mesh-verkosta: Miten välittää Mesh-verkosta tietoa JavaFX-säikeeseen turvallisesti?
    - Myös: Uuden tiedon lukeminen JavaFX-säikeessä. Missä kohtaa uudet viestit voisi tarkistaa?
- Siirretään Chatin tulostus Javafx-säikeeseen
- Mitä tapahtuu, kun valitaan "Palvelinyhteys" valikosta?
    - Kuka päättää GameConfigurationin sisällön?
    - Miten ne, jotka eivät päätä GameConfigurationin sisältöä, liittyvät peliin?
    - Miten nämä asiat viestitään verkon yli; Miten neuvottelu tapahtuu?
- Identtisen pelitilan luonti
- Miten pelaajien sijainnit yhdistetään solmuihin?
- Miten siirrot siirretään verkon ylitse ja miten ne kohdistetaan oikeaan pelaajaan?

Näiden osaongelmien ratkaisemisen jälkeen peli tulisi olla minimivaatimuksiltaan hyväksytty