# Dokumentasjonsbeskrivelse

### Prosjekt av Runar Saur Modahl

## Del 1: Beskrivelse av appen

I dette prosjektet har jeg lagd en sjakk-app. Den inneholder en startside hvor man har et par diverse valg. Man kan velge å spille vanlig sjakk og fischer random, laste inn et spill fra en FEN-string enten direkte eller via en fil. Det er mulig underveis i et spill, eller etter fullført spill, å eksportere stillingen til en fil. På denne måten kan en fortsette et sjakkspill ved en senere anledning, eller for å teste ut hva en kunne gjort annerledes. Prosjektet inneholder en god del klasser, og jeg har forsøkt å sortere de i enkelte ulike pakker for å holde litt mer oversikt over dem. Den ene pakken er pieces, hvor alle brikkene er lagret. Her er Piece en abstrakt klasse som utvides av Pawn, King, Kight og LinearPiece. LinearPiece er enda en abstrakt klasse som utvides av Queen, Bishop og Rook, siden disse 3 har svært lik lovig-trekk-logikk. Dette er gjort siden alle brikker har svært lik funksjonalitet, men enkelte metoder som skal være annerledes implementert. Det er også en utils-pakke som inneholder litt generell utility-logikk-klasser, som FENParser. Det er også en controllers-pakke som inneholder de ulike kontrollerne. Til slutt har vi sjakk-pakken som inneholder mye av kjerne-logikken som feks ChessBoard og Position.

## Del 2: Diagram

### Klassediagram

Jeg har valgt å vise litt av hvordan chessboard-klassen, player, position og pieces henger sammen. For å gjøre det litt mer oversiktelig har jeg valgt å fjerne en del av de metodene og feltene som ikke er så relevante for å forstå hvordan klassene henger sammen.
![Klassediagram](classdiagram.svg)

## Del 3: Spørsmål

### Spørsmål 1:

Dette prosjektet prøver å dekke mye av pensum i emnet. Jeg har blant annet brukt arv (feks Pawn fra Piece), interface (Kontrollerene er Initializable), delegering (feks ChessGameController til ChessGame), polymorfi (alle pieces), abstrakte klasser og innkapsling. Jeg har også implementert grensesnittet Iterable, slik at jeg kan iterere over brettet. Dette gjorde det lett å for eksempel finne ut om en spiller var i sjakkmatt ved å se om det var noen brikker som hadde minst 1 lovelig trekk ved å iterere gjennom brikkene på brettet. I tillegg benyttes det JUnit tester for å teste deler av koden, og jeg har brukt Javadoc til å dokumentere koden underveis.

### Spørsmål 2:

Det er også enkelte konsepter jeg ikke har brukt i prosjektet. Et eksempel er ikke bruker observatør-observert-konseptet. Nå når jeg er ferdig, innser jeg at det ville mulig gjort koden min en god del ryddigere. Jeg har også brukt veldig lite lambda-utrykk som kan gjøre koden mer lesbar og effektiv. Annet enn i testene hvor det er brukt noen suppliers, brukte jeg streams kun én gang i FENParser.

### Spørsmål 3:

Jeg har valgt å implementere dette prosjektet med tanke på å holde modellen og kontrolleren så adskilt som mulig der det har latt seg gjøre. Enkelte steder var det svært vanskelig å finne gode måter å implementere det på. Eksempelvis føltes det veldig naturlig at jeg viser et popup vindu for å velge hva en kan oppgradere en bonde til om den når enden av brettet i move metoden i ChessBoard-klassen. For å endre dette ville jeg måtte sende med en referanse til et vindu i ChessBoard-klassen, noe som føltes veldig feil. Dermed valgte jeg å lage et felt som lagret en mulig bonde som skal kunne bli promotert om den blir flyttet til enden av brettet, i move-metoden i ChessBoard. Deretter sjekket jeg om det feltet hadde noen verdi fra kontrolleren etter chessboard hadde flyttet en bonde, og da eventuelt viste jeg popup vinduet. Virker ikke optimalt, men det er sånn det ble. En svakhet jeg tror jeg ikke har luket helt bort er at jeg kanskje har for mye logikk i kontrollerene mine som ikke direkte kobler sammen modellen og brukergrensesnittet. Et eksempel på dette er at jeg i promotePawn metoden i ChessGameController har logikk for å lage ny brikke (dronning, springer, tårn og løper), i stedet for å ha det i modellen.

### Spørsmål 4:

Testene mine for dette prosjektet tester hvordan modellen fungerer. Jeg valgte å lage flere av testene før jeg i det hele tatt startet å utvikle metodene som krevdes for å få testene godkjent. Det gjorde jeg på samtlige sjakk.legalMoves-tester, siden jeg allerede veldig tidlig visste hva disse testene skulle ta for seg, nemmelig sjekke at alle lovige trekk blir returnert riktig. I sjakk merket jeg at det er veldig mange edge-cases på hva et lovlig trekk er, og da var det veldig greit å ha ferdiglagde tester underveis i utviklingen. Dermed jobbet jeg meg fra ingen godkjente tester til at én og én ny test ble riktig, etterhvert som jeg fikset små feil og bugs.

Jeg har også laget en del tester for å forsikre meg at bevegelse av brikker gjennom chessboardet fungerer med både rokade og en passant. I tillegg sjekker jeg at promotion av bonde fungerer i modellen og ikke bare i appen gjennom kontrolleren. Jeg har også forsøkt å teste en del av FENParseren jeg har lagd, og i dette også fillagring/lesing, da dette er et ganske stort konsept i prosjektet mitt, og jeg ønsket å være sikker på at det fungerte som det skulle.

Jeg har valgt å ikke lage noen tester av verken Player-klassen eller Position-klassen siden dette implisitt blir testet gjennom så og si hver eneste annen test. Om det hadde vært noe feil med Position, ville ikke for eksempel testene som sjekker hvor en brikke kan bevege seg fungert.
