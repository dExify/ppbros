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

Justerte krav til MVP:
* Vise en bakgrunn
* Vise enkle platformer på bakgrunn
* Vise spiller på skjerm
* Flytte spiller i forskjellige retninger basert på input fra tastatur
* Spiller interagerer med terrenget
* Vise fiender
* Spiller kan interagere med fiender
* UI som kan starte spillet
 
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

Brukerhistorier for krav å jobbe med videre:

Som en spiller trenger jeg å kunne samle Powerups for å få midlertidige fordeler i spillet.
- Akseptansekriterie: Powerups må dukke opp i spillverdenen, spiller må kunne plukke den opp og når aktivert endres det måten spiller spiller på.
- Arbeidsoppgave: Implementere forskjellige Powerups, lage system for spawning av de, lage visuell og/eller lydmessig feedback når en plukkes opp og implementere nedtelling og deaktivering av powerup effekten.

Som en spiller trenger jeg en Game Over-skjerm når jeg taper
- Akseptansekriterie: Skjermen må vises ved tap og ha alternativer for restart og hovedmeny.
- Arbeidsoppgave: Implementer Game Over UI og koble det til spillets sluttbetingelser.

Som en spiller vil jeg oppleve animasjoner, lydeffekter og visuelle effekter, som skal hjelpe med å se registrerte hits, hopp og liknende.
- Akseptansekriterie: Når viktige hendelser skjer (f.eks. skade, hopp, PowerUps), skal passende animasjon, lyd eller visuell effekt spilles av.
- Arbeidsoppgave: Legg til animasjoner, lydeffekter og/eller visuelle effekter til sentrale spillhendelser.

## Prosjektrapport

### Roller i teamet og hvordan de fungerer
- Alle medlemmene i gruppa har i hovedsak fungert som utviklere. I tillegg har vi hatt noen biroller med mindre ansvar som ble fordelt etter behov.  
- Åse har hatt rollen som kundekontakt, men det har vært lite faktisk kundekontakt i prosjektet, så rollen har vært mer symbolsk.  
- Ole var i starten ansvarlig for merge requests som git-ansvarlig, men dette ansvaret ble etter hvert spredt mer ut på hele teamet.  
- Luka fikk tilgang til assets via en bekjent, så han fikk naturlig ansvar for grafiske ressurser.  
- Vi har ikke sett behov for å endre rollene, de har fungert godt nok i prosjektet

### Vurdering av teamdynamikk og metodevalg
- Gruppens kommunikasjon og samarbeid har generelt fungert godt. Vi har hatt få uenigheter, og ansvarsfordelingen har skjedd naturlig ved at oppgaver ble delegert til enkeltpersoner som tok eierskap.  
- Ved merge til `main` har vi hatt gode diskusjoner for å kvalitetssikre løsningene.  
- Vi startet med Trello for oppgaveorganisering, men gikk bort fra det fordi vi oftest jobbet fysisk sammen. Dette gjorde det lettere å parprogrammere og løse problemer underveis, som ga oss en mer uformell, men fungerende kanban-stil.  
- Møtereferatene våre har vært nyttige for å dokumentere fremgang.  
- I ettertid ser vi at MVP-en vår burde vært enklere og tydeligere definert som en prototype. Vi tok utgangspunkt i en mer komplett spillversjon, noe som gjorde scope og arbeidsmengde større enn planlagt.

### Kommunikasjon og samarbeidsverktøy
- Kommunikasjonen har foregått primært via Discord og Snapchat.  
- Det har fungert effektivt for raske oppdateringer og koordinering.  
- For dokumentasjon og deling av fremgang, har vi brukt Git og delt referat fra møter.

### Retrospektiv vurdering og forbedringspunkter
- Vi har oppnådd god fremgang i prosjektet og samarbeidet har vært effektivt. Samtidig ser vi noen forbedringspunkter:  
  - Vi kunne vært tydeligere med forventninger til arbeid mellom møter.  
  - Det har vært noe skjevfordeling i commits – dette burde bli mer likt til release
  - Vi hadde ingen klar rekkefølge eller sprintplan for hva som skulle lages når, noe som kunne gjort det enklere å fordele arbeid jevnere.
  - Vi har ofte overestimert hvor mye tid nye implementasjoner tar, som har ført til at vi dedikerer for mye tid til en spesifikk oppgave, 
  - og dermed gjør at vi bruker tiden veldig ineffektivt

### Forbedringspunkter fra forrige sprint
- Etter forrige innlevering (Oblig 3) forsøkte vi å forbedre samarbeidet vårt basert på punktene vi hadde identifisert. Vi opplevde at møtene våre denne perioden var svært fokuserte og effektive. Det meste av fremdriften skjedde i disse fysiske møtene, hvor vi samarbeidet tett og løste utfordringer i fellesskap. Utenom møtene ble det gjort mindre arbeid, noe som førte til at vi i liten grad fordelte individuelle oppgaver mellom møtene. På grunn av dette merket vi ikke like mye til utfordringene vi tidligere hadde med tidsestimering og ressursfordeling (punkt 2).

- Når det gjaldt prioritering, var det klart hvilke bugs som var mest kritiske – særlig knyttet til gravitasjon og kollisjon – og vi fokuserte derfor hovedsakelig på å løse disse. Dette ga en naturlig prioriteringsrekkefølge, selv om en mer eksplisitt prioriteringsliste fortsatt kunne vært nyttig for å unngå overlapp i arbeidet.

## Krav og Spesifikasjon 
### Krav vi har prioritert, våres progressjon og om oppfylling av MVP
- Vi har hovedsaklig prioritert MVP da vi enda ikke hadde oppfylt alle kravene satt for vår MVP. Hver av oss har fokusert på forskjellige implementasjoner og hjulpet hverandre på møtene. Vi prioriterte å få på plass bakgrunn og platformer da dette ville sette et godt grunnlag for spillet. Likevel var det mulig å eksperimentere med diverse logikk og visning for spiller på siden. Så fort platformer og spiller kunne tegnes og beveges ønsket vi å håndtere kollisjon mellom dem, på den måten lager vi et interaksjonerbart miljø. Det var også tid for mindre prioriterende aspekter for spillet som forside, lydeffekter og animasjon. Disse var lengre nede på prioritetlista da de vil ikke ha noe direkte å si for om spillet er spillbart og spillprogressjon. Som skrevet i MVP'en var fiende en prioritet, og er noe vi hadde ønsket var implementert tidligere da mye av logikken for spillet trengs fiender. 

### Prioritering av oppgaver fra sist

Siden sist innlevering (Oblig 3) har vi fikset bugs med gravitasjon. Spillet fungerer slik at spiller kan hoppe opp, møte motstand med potensielle platformer og bli dratt nedover av en virtuell tyngdekraft. Hvordan fiender oppfører seg og spawner er blitt forbedret. Før oblig 3 kunne fiender kun gå side til side på sin platform, det er nå lagt til at de vil gå mot spiller om spiller er nærme nok. Animasjoner til fiende er også oppdatert. Edge cases til spawning av fiender er blitt jobbet med. Vi bestemte oss at vi ikke skulle implementere gravitasjon for enemies, da det var vanskelig når våres hoved gravitasjon (den som brukes på spiller) ikke var komplett. 

Vi har også fått lagt til en form for power ups, en game over skjerm, en hjelpeside og logikken for at spiller mister liv når de er utfor skjermen. Power upsen fungerer slik at spiller kan drepe fiender og få tilbake litt liv, og for hver femte poeng/drept fiende vil spiller få økt skade den gjør mot fiender. En game over skjerm vises når spiller mister all liv, spiller har så muligheten til å kjøre spillet på nytt eller lukke programmet. En knapp til en hjelpeside er lagt til på startskjermen. På den siden kan spiller bli forklart hvordan spille spillet. Så lenge spiller står utenfor skjermen (gjelder ikke for øverste skjermkant) vil spiller fortsette å ta liv. 

Vi har lagt til flere lydeffekter. Lydeffekter for diverse hendelser i spillet er lagt til. Vi har også flyttet rundt på hvor animasjoner blir håndtert for å følge MVC mønsteret for koden. Det var også viktig å få generere en uendelig bakgrunn.  
Å få inn tester har vært en prioritet. Alle har jobbet med å opprette tester for diverse aspekter av spillet. Kodestilen og javadocs har også vært en stor mengde av arbeidet vårt siden forrige oblig. Nå som vi nærmet oss et komplett spill var det enklere å kunne rydde opp i koden, fjerne unødvendig og ubrukt kode, og skrive javadocs for alle klasser, public og protected metoder. 

Når det gjelder de ekstra implementasjonene (bosses, two-player mode, forskjellige type fiender), var det altfor lite tid igjen til. Ingen av disse implementasjonene ble startet på fordi vi visste de ville ta for lang tid og energi i forhold til hvor mye tid vi hadde igjen og med tanke på alt annet arbeid vi måtte prioritere.  

### Justeringer av MVP
- MVP'en vi lagde var mer omfattende og tidskrevende enn det vi trodde og vi tenker det er mulig å forkorte den mens den alikevel vil bli ansett som en MVP. Justert MVP er skrevet under forrige MVP og er forkortet ved å ha fjernet noen krav for logikk implementasjon og forenklet visuelle krav.