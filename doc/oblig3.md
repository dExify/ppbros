# Rapport – Innlevering 3
**Team:** *Sample group* – *Luka Silic, Even Tveit, Ole Christian Sollid, Åse Holen*

## Roller
- Kundekontakt / Developer - Åse Holen
- Gitsvarlig / Developer - Ole Christian Sollid
- Assetansvarlig / Developer - Luka Silic
- Assetansvarlig / Developer - Even Tveit

## Konsept

### Spillmekanikk:
- Spillbare figurer: To spillere kan styre hver sin figur, en med WASD og en med piltastene.
- Bevegelse: Spillfigurene kan gå til venstre/høyre og hoppe oppover.
- Kamp: Spillerne kan slå fiender for å eliminere dem.

### Spillverden:
- Plattformbasert: Plattformene forsvinner nedover i voiden, noe som tvinger spillerne til å stadig bevege seg oppover.
- Dynamiske fiender: Hver ny plattform introduserer nye fiender med varierende utfordringer.

### Mål og progresjon:
- Poengsystem: Spilleren får poeng ved å slå fiender.
- Nivåer og bosser: Når en spiller når et viss antall poeng (kills), går de videre til neste nivå, og en boss spawner.
- Økende vanskelighetsgrad: Spillet blir progressivt vanskeligere etter hvert som spillerne samler flere poeng.
- Mål: Holde seg i livet og samle så mange poeng som mulig.

## Kjøring
* Kompileres med `mvn package`.
* Kjøres med `java -jar target/power-pipes-bros-1.0-SNAPSHOT-fat.jar`
* Krever Java 21+

## Teamprosess

- Vi velger å hente hovedsakelig metodikk fra Kanban.
    Vi velger å følge kanban sine praksiser:
    * La arbeidet ditt bli synlig ved å committe og pushe ofte, med gode commit meldinger, slik at andre i teamet kan se hvor vi ligger an og for å estimere resterende arbeidsmengde og tidsbruk. 
    * Begrense pågående arbeid i oppfordring av å fullføre påbegynt arbeid før nytt startes. Kan begrenses ved å for eksempel begrense antall WIP kort som kan lages eller begrense antall branches per medlem.   
    * Opprettholde og forbedre arbeidsflyt ved at alle prøver å analysere arbeidsflyten med et formål for å oppnå effektiv og jevn progressjon. Det vil også være viktig å identifisere, kommunisere og håndtere hindringer og utfordringer som oppstår.
    * Lage og gjøre prosessprinsippene klare og tydlige. Når alle har en felles forståelse og enighet for målet med prosjektet blir det lettere å jobbe målrettet med minst mulig misforståelser, komme med innspill og ta avgjørelser som gir positiv innvirkning. 
    * Opprette "feedback loops". Ved å møtes gjevnlig og ofte sørges det for at nødvendig informasjon blir gitt, det sjekkes tilgjengelig kapasitet og det er en mulighet for å respondere til potensielle endringer. Vår løsning på dette er beskrevet nedenfor. 

Vi har også planer om å:
- Sette opp ukentlige møter hvor vi setter mål for uken, bruker trello for oversikt om veien videre
- Delegerer oppgaver ukentlig. deler teamet opp, og 1-2 utviklere jobber på hver sin branch hvor de implementerer/løser bestemt problem
- Etter begge lagene med utviklere er ferdig, bytter utviklerlagene til det andre laget sin branch, og reviewer/tester hver sin branch.
- Teamet avtaler faste møtetider ukentlig, kommunikasjon skjer via Snapchat og Discord
- Google Docs brukes til å skissere planer, Trello for å strukturere det
- Eksempel
> Luka & Åse delegeres til å implementere GameCharacter-klassen, Ole & Even skal lage et spillbrett og implementere det i *view*
> Hvert lag får en frist, og innen denne fristen skal dette pushes til hver sin respektive branch.
> Luka & Åse får nå oppgave om å gå gjennom og teste Ole & Even sin løsning og vice versa. 
> Etter suksessfull testing og review vil branchene merges til main

## Spesifikasjon
Vi skal lage et 2D-plattformspill der spilleren styrer en karakter gjennom ulike plattformer, 
unngår fiender og samler poeng. Målet er å overleve så lenge som mulig samtidig som man prøve å oppnå ny high score." 
 
Kravene til MVP er som følger:
* Vise en statisk spilleverden
* Vise spiller i spillverden
* Flytte spiller venstre, høyre, opp(hopp) og ned (fall)
* Spiller interagerer med terrenget og monstre
* Vise fiender/monstre; de skal interagere med terreng og spiller
* Gi spiller poeng for å bekjempe fiender/monstre.
* Spiller kan dø ved kontakt med fiender, eller ved å falle utfor skjermen/verden
* Start-skjerm UI ved oppstart / game over
 
## Brukerhistorier
Som en spiller trenger jeg en spillverden for å kunne spille.
- Akseptansekriterie: Spillverdenen må være tilgjengelig og navigerbar.
- Arbeidsoppgave: Lage en fungerende spillverden med terreng og plattformer.

Som en spiller trenger jeg ha en karakter for å kunne interagere med spillverden.
- Akseptansekriterie: Spilleren må kunne kontrollere en karakter.
- Arbeidsoppgave: Implementere en spillbar karakter med grunnleggende funksjoner som bevegelse og interaksjon.
 
Som en spiller trenger jeg fiender for å samle poeng.
- Akseptansekriterie: Fiender må eksistere i spillverdenen og gi poeng når man bekjemper de.
- Arbeidsoppgave: Lage fiender med oppførsel og poengsystem ved bekjempelse.

Som programmerer trenger jeg å kunne skille mellom bakgrunn og objekter i spillet for å kunne vite hva jeg jobber med.
- Akseptansekriterie: Det må være mulig å skille bakgrunn og objekter.
- Arbeidsoppgave: Sørge for en tydelig forskjell mellom bakgrunn og objekter.
 
Som en spiller trenger jeg å kunne bevege en karakter for å navigere spillverden.
- Akseptansekriterie: Karakteren må kunne bevege seg i alle nødvendige retninger.
- Arbeidsoppgave: Implementere bevegelseskontroller med animasjoner og fysikk.
 
Som en spiller trenger jeg å kunne angripe for å kunne bekjempe fiendene.
- Akseptansekriterie: Spilleren må kunne utføre angrep som påvirker fiender.
- Arbeidsoppgave: Implementere en angrepsmekanisme og visuelle effekter som forteller spilleren at angrepet er utført.
 
Som en fiende trenger jeg å kunne styre meg selv for å utfordre spilleren.
- Akseptansekriterie: Fienden må ha en AI som beveger seg og reagerer på spilleren.
- Arbeidsoppgave: Implementere AI-styrt bevegelse og angrepsmønstre.


## Prosjektrapport
* Hvordan fungerer rollene i teamet? Trenger dere å oppdatere hvem som er teamlead eller kundekontakt?
- Har ikke hatt noen store problemer med roller. Vi har alle hovedsakelig vært utviklere, hvor vi alle fikk en bi-rolle ga en liten ekstraoppgave.  

* Trenger dere andre roller? Skriv ned noen linjer om hva de ulike rollene faktisk innebærer for dere.
- Kundekontakt har ikke hatt så mye kontakt med kunder, så vært en litt symbolsk rolle for Åse. Ole som gitansvarlig har gjort mesteparten av mergerequests osv i starten, men lengre inn i prosjektet har har dette ansvaret blitt litt mer spredt over alle medlemmene. Luka fikk assets av en kompis, så passet bra. 

* Er det noen erfaringer enten team-messig eller mtp prosjektmetodikk som er verdt å nevne? Synes teamet at de valgene dere har tatt er gode? Hvis ikke, hva kan dere gjøre annerledes for å forbedre måten teamet fungerer på?
- I ettertid så ville vi lagd en litt mindre krevende MVP. MVP som opprinnelig ble laget reflekterte mer et komplett spill, i motsetning til noe som burde fungere mer som en prototype. Vi startet med et trello-brett for å organisere oppgaver, men dette ble fort lagt vekk dersom vi som oftest satt fysisk sammen og jobbet med spillet. Når vi satt sammen ble det naturlig å parprogrammere på forskjellige problemer/oppgaver vi ga hverandre, og vi oppfylte dermed kanban-metodikken vi planla til en viss grad. Referatene fra møtene våres gir et mye bedre innblikk i hva som har blitt gjort enn trello.

* Hvordan er gruppedynamikken? Er det uenigheter som bør løses?
- Majoriteten av gruppen har hatt ganske god kommunikasjon og fremgang. Det har vært få uenigheter angående koden og hvordan forskjellige ting skal løses, da vi som oftest har delegert ansvaret for en løsning til en enkelt utvikler. Ved møter hvor vi skal merge løsninger inn til main, har vi hatt konstruktive diskusjoner om løsningene og eventuelle problemer ved merge.

* Hvordan fungerer kommunikasjonen for dere?
- 

* Gjør et kort retrospektiv hvor dere vurderer hva dere har klart til nå, og hva som kan forbedres. Dette skal handle om prosjektstruktur, ikke kode. Dere kan selvsagt diskutere kode, men dette handler ikke om feilretting, men om hvordan man jobber og kommuniserer.
- 

Under vurdering vil det vektlegges at alle bidrar til kodebasen. Hvis det er stor forskjell i hvem som committer, må dere legge ved en kort forklaring for hvorfor det er sånn. Husk å committe alt. (Også designfiler)
- 

Bli enige om maks tre forbedringspunkter fra retrospektivet, som skal følges opp under neste sprint.
