# Rapport – innlevering 1
**Team:** *Sample group* – *Luka Silic, Even Tveit, Ole Christian Sollid, Åse Holen*...

## Roller
- Kundekontakt / Developer - Åse Holen
- Gitsvarlig / Developer - Ole Christian Sollid
- Assetansvarlig / Developer - Luka Silic
- Assetansvarlig / Developer - Even Tveit

## Konsept

- Navn: Power Pipes Bros

## Kjøring
* Kompileres med `mvn package`.
* Kjøres med `java -jar target/power-pipes-bros-1.0-SNAPSHOT-fat.jar`
* Krever Java 21+

## Teamprosess

- Vi velger å hente hovedsakelig metodikk fra Kanban.
- Ukentlige møter hvor vi setter mål for uken, bruker trello for oversikt om veien videre
- Delegerer oppgaver ukentlig. deler teamet opp, og 1-2 utviklere jobber på hver sin branch hvor de implementerer/løser bestemt problem
- Etter begge lagene med utviklere er ferdig, bytter utviklerlagene til det andre laget sin branch, og reviewer/tester hver sin branch.
- Teamet avtaler faste møtetider ukentlig, kommunikasjon skjer via Snapchat og Discord
- Google Docs brukes til å skissere planer, Trello for å strukturere det
- Eksempel
> Luka & Åse delegeres til å implementere GameCharacter-klassen, Ole & Even skal lage et spillbrett og implementere det i *view*
> Hvert lag får en frist, og innen denne fristen skal dette pushes til hver sin respektive branch.
> Luka & Åse får nå oppgave om å gå gjennom og teste Ole & Even sin løsning og vice versa
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
 
Brukerhistorier
 
Som en spiller trenger jeg en spillverden for å kunne spille.
Akseptansekriterie: Spillverdenen må være tilgjengelig og navigerbar.
Arbeidsoppgave: Lage en fungerende spillverden med terreng og plattformer.

Som en spiller trenger jeg ha en karakter for å kunne interagere med spillverden.
Akseptansekriterie: Spilleren må kunne kontrollere en karakter.
Arbeidsoppgave: Implementere en spillbar karakter med grunnleggende funksjoner som bevegelse og interaksjon.
 
Som en spiller trenger jeg fiender for å samle poeng.
Akseptansekriterie: Fiender må eksistere i spillverdenen og gi poeng når man bekjemper de.
Arbeidsoppgave: Lage fiender med oppførsel og poengsystem ved bekjempelse.

Som programmerer trenger jeg å kunne skille mellom bakgrunn og objekter i spillet for å kunne vite hva jeg jobber med.
Akseptansekriterie: Det må være mulig å skille bakgrunn og objekter.
Arbeidsoppgave: Sørge for en tydelig forskjell mellom bakgrunn og objekter.
 
Som en spiller trenger jeg å kunne bevege en karakter for å navigere spillverden.
Akseptansekriterie: Karakteren må kunne bevege seg i alle nødvendige retninger.
Arbeidsoppgave: Implementere bevegelseskontroller med animasjoner og fysikk.
 
Som en spiller trenger jeg å kunne angripe for å kunne bekjempe fiendene.
Akseptansekriterie: Spilleren må kunne utføre angrep som påvirker fiender.
Arbeidsoppgave: Implementere en angrepsmekanisme og visuelle effekter som forteller spilleren at angrepet er utført.
 
Som en fiende trenger jeg å kunne styre meg selv for å utfordre spilleren.
Akseptansekriterie: Fienden må ha en AI som beveger seg og reagerer på spilleren.
Arbeidsoppgave: Implementere AI-styrt bevegelse og angrepsmønstre.