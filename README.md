# Android RPG Project

Ein Android-RPG, entwickelt mit Kotlin und Jetpack Compose.

## Vision und Lernziel

Dieses Projekt ist als Lernprojekt entstanden. Michael Ouaazzani-Chahdi hat den Code von Anfang an selbst geschrieben und die Funktionen Schritt fuer Schritt aufgebaut. ChatGPT und spaeter Codex wurden als Lernbegleitung genutzt: zum Erklaeren, Nachfragen, Pruefen von Aenderungen, Geben von Hints und zum gemeinsamen Verstehen von Kotlin, Jetpack Compose, ViewModel, StateFlow, Git und sauberer Projektstruktur.

Wichtig ist dabei: Michael moechte die Konzepte wirklich verstehen und nicht nur fertige Loesungen uebernehmen. Deshalb wird Code normalerweise selbst geschrieben, danach gemeinsam gelesen, verbessert und eingeordnet.

## Arbeitsregeln fuer Codex

Projektpfad: `C:\Users\acer\AndroidStudioProjects\MyKotlinPlayground`

- Der Nutzer moechte Kotlin/Jetpack Compose selbst lernen und Code-Dateien selbst schreiben.
- Codex soll keine Code-Dateien direkt aendern, ausser der Nutzer bittet ausdruecklich darum.
- Codex darf Code lesen, erklaeren, Hints geben, Alternativen zeigen und Aenderungen pruefen.
- Die README liegt bei Codex und soll aktuell gehalten werden, besonders mit Datum, wenn wichtige Aenderungen gemacht wurden.
- Wenn der Nutzer `git` schreibt, bedeutet das normalerweise: Aenderungen pruefen, sagen ob sie zusammenpassen, README bei Bedarf aktualisieren und dem Nutzer die drei Git-Zeilen geben.
- Der Nutzer fuehrt Git selbst aus. Codex soll normalerweise nicht selbst committen oder pushen, ausser der Nutzer bittet ausdruecklich darum.
- Kleine Aenderungen nicht einzeln committen, sondern sinnvoll buendeln.
- Lernstil: langsam hinfuehren, keine fertigen Komplettloesungen, ausser der Nutzer fragt ausdruecklich danach. Lieber kleine Hints, Verstaendnisfragen und kurze Erklaerungen.
- In neuen Chats zuerst diese Arbeitsregeln lesen.

## Aenderungsverlauf

- Hinweis: Die Datumsangaben sind aus den Git-Commits abgeleitet. Sie zeigen, wann ein Feature oder Refactoring ins Repository gekommen ist.

- 2026-06-14: Projektbasis gestartet (`RPG Version 0.1`), README angelegt, erste Kampf- und Drop-Logik refactored.
- 2026-06-27: Inventar erweitert; Waffen, Heiltraenke, Item-System und Waffenlogik wurden aufgebaut/refactored.
- 2026-06-28: Weapon-Equip-System auf Item-Objekte umgestellt; Armor-System mit Ausruesten, Defense und Inventar-Updates eingebaut.
- 2026-06-28: Compose Previews fuer Game Screens ergaenzt; Game Screens, Player-Stats-Layout und scrollbares Inventar verbessert.
- 2026-06-28: DropManager und Item-Drop-System refactored; Inventory-Handling verbessert.
- 2026-06-30: Drop- und Combat-Systeme weiter refactored.
- 2026-07-03: Drops refactored und Inventory-UI verbessert.
- 2026-07-04: InventorySection und EquipItem UI refactored; PotionItem als eigene Composable extrahiert.
- 2026-07-04: Game-Balance-Werte im GameViewModel extrahiert.
- 2026-07-05: Potion-Usage-Logic und Potion-Logging getrennt/refactored.
- 2026-07-05: Dodge-Handling, Dodge-Chancen und Dodge-Logging klarer getrennt.
- 2026-07-05: DamageResult eingefuehrt und Damage-Logik/Combat-Berechnung schrittweise geklaert.
- 2026-07-05: Base-Damage, Attack-Berechnung und Combat-Damage-Results extrahiert/refactored.
- 2026-07-05: Inventory-Item-Vorbereitung in sichtbare Potion/Weapon/Armor-Listen geklaert.
- 2026-07-06: Inventory-Item-Display weiter verfeinert.
- 2026-07-06: RemoveInventoryItem/Event/Logic/ViewModel/UI eingebaut; nicht ausgeruestete Waffen/Ruestungen koennen aus dem Inventar entfernt werden.
- 2026-07-08: Inventory-Logiktests und DropManager-Tests wurden ergaenzt; grosser Heiltrank Level-1-Test wurde vom Nutzer geschrieben und verstanden.
- 2026-07-08: Arbeitsregeln fuer Codex sowie Vision und Lernziel in der README festgehalten: Nutzer schreibt Code selbst, Codex erklaert/prueft und haelt README aktuell.
- 2026-07-08: Waffe und Ruestung koennen im Inventar abgelegt werden, ohne aus dem Inventar entfernt zu werden; GameLogic-Tests dafuer wurden vom Nutzer ergaenzt.
- 2026-07-11: GameLog-Meldungen fuer Angriff, Entfernen von Inventar-Items sowie Ablegen von Waffe und Ruestung ergaenzt.
- 2026-07-11: Haendler-Konzept mit levelabhaengigem Angebot, dynamischen Heiltrank-Preisen und Verkaufspreisen dokumentiert.
- 2026-07-11: Item-Balancing fuer Heiltraenke, Waffen und Ruestungen zentral in der README dokumentiert.
- 2026-07-14: Haendler/Shop-Screen umgesetzt: levelabhaengige Angebote, Kaufen und Verkaufen, Gold-Pruefung, Stack-Limit fuer Heiltraenke sowie Verkaufsschutz fuer ausgeruestete Waffen/Ruestungen.
- 2026-07-15: GameLogic-Tests fuer Shop-Kauflogik ergaenzt; Heiltrank-Stacking, fehlendes Gold, Verkaufspreise und doppelte Waffenkaeufe werden geprueft.
- 2026-07-18: Shop-UI-Texte und Button-Darstellung ueberarbeitet; eigene Shop-Section und Shop-Buttons eingefuehrt, leere Texte zentriert und Shop-Verlassen-Button korrekt ausserhalb der Waffenliste platziert.
- 2026-07-19: Shop-Verkaufsbereiche weiter refactored; Verkauf von Traenken, Waffen und Ruestung laeuft ueber gemeinsame Shop-Komponenten. Waffen/Ruestung werden nicht mehr im Inventar weggeworfen, sondern ueber den Shop verkauft.
- 2026-07-19: Shop-Angebotsanzeige weiter refactored; Kaufstatus-Texte und Kaufbutton-Text wurden aus der UI-Entscheidung herausgezogen.
- 2026-07-19: Shop-Logs im GameViewModel ergaenzt; doppelte Unique-Kaeufe und Verkauf ausgeruesteter Ausruestung werden abgefangen und mit ViewModel-Tests geprueft.

## Aktuelle Features

- Kampfsystem - seit 2026-06-14, mehrfach refactored am 2026-07-05
- Kritische Treffer - seit 2026-06-14, Chance/Benennung refactored am 2026-07-05
- Ausweichen - seit 2026-06-14, shared Dodge-Handling refactored am 2026-07-05
- XP-System - seit 2026-06-14
- Levelsystem - seit 2026-06-14
- Gegner-Skalierung - seit 2026-06-14
- Navigation - seit 2026-06-14
- Inventarsystem - erweitert am 2026-06-27
- Waffen - hinzugefuegt/umgebaut am 2026-06-27, Equip-System auf Item-Objekte umgestellt am 2026-06-28
- Ruestung/Ausruestung - hinzugefuegt am 2026-06-28
- Heiltraenke - hinzugefuegt am 2026-06-27, Potion-Logik refactored am 2026-07-05
- Grosser Heiltrank - nachweisbar seit 2026-06-27, Tests ergaenzt am 2026-07-08
- Heiltrank-Drops - nachweisbar seit 2026-06-14, DropManager refactored am 2026-06-28
- Inventar-Screen - seit 2026-06-14, UI refactored am 2026-07-04 und 2026-07-06
- Items aus Inventar entfernen - hinzugefuegt am 2026-07-06
- Waffe und Ruestung ablegen - hinzugefuegt am 2026-07-08
- Hoch- und Querformat - Game-Screen-Previews/Layout verbessert am 2026-06-28
- Inventory- und DropManager-Tests - hinzugefuegt am 2026-07-08
- Haendler/Shop-Screen - hinzugefuegt am 2026-07-14
- Items kaufen und verkaufen - hinzugefuegt am 2026-07-14
- Shop-Kauflogik-Tests - hinzugefuegt am 2026-07-15
- Shop-UI-Texte und Shop-Buttons - ueberarbeitet am 2026-07-18
- Shop-Verkaufsbereiche - refactored am 2026-07-19
- Shop-Angebotsstatus - refactored am 2026-07-19
- Shop-Logs und ViewModel-Tests - hinzugefuegt am 2026-07-19

## Item-Balancing

- Heiltraenke skalieren mit dem Spieler-Level.
- Waffen haben feste Schadenswerte.
- Ruestungen haben feste Verteidigungswerte.
- Heiltrank-Preise koennen levelabhaengig berechnet werden, weil Heiltraenke mit hoeherem Level staerker wirken.

## Haendler-Konzept

Der Haendler ist als eigener Shop-Screen umgesetzt. Er verkauft Items levelabhaengig und nutzt Gold als Waehrung. Der Einstieg zum Haendler kann spaeter nach einem Level-Up angeboten werden, z.B. mit der Frage: "Moechtest du zum Haendler gehen?"

### Angebot

Das Haendler-Angebot ist erstmal fest und vom Spieler-Level abhaengig. Dadurch bleibt das Balancing verstaendlich und die Logik leichter lernbar.

- Level 1-2: kleine Heiltraenke, Holzschwert, einfache Ruestung
- Level 3-4: grosser Heiltrank, Eisenschwert, Eisen-Ruestung
- Level 5-6: bessere oder seltenere Ausruestung/Waffen
- Spaeter: optional zufaellige Tagesangebote oder Rabatte

### Preise

Jedes Item hat einen Grundpreis (`itemPrice`).

Der Kaufpreis wird mit `buyPrice(item, playerLevel)` berechnet.

- Waffen und Ruestungen behalten erstmal ihren festen Grundpreis.
- Heiltraenke werden mit hoeherem Spieler-Level teurer, weil sie durch die Level-Skalierung auch staerker wirken.

Der Verkaufspreis wird aus dem aktuellen Kaufpreis abgeleitet:

```kotlin
sellPrice = 50% von buyPrice(item, playerLevel)
```

Dadurch muss nicht gespeichert werden, zu welchem Preis ein Item frueher gekauft wurde.

### Kaufen

Beim Kaufen gelten folgende Regeln:

- Der Spieler braucht genug Gold.
- Das Item muss fuer das aktuelle Level freigeschaltet sein.
- Stackbare Items wie Heiltraenke duerfen das Maximum nicht ueberschreiten.
- Wenn ein Stack voll ist, erscheint eine Meldung im GameLog.
- Unique Items wie Waffen und Ruestungen sollen nicht doppelt gekauft werden.

### Verkaufen

Beim Verkaufen gelten folgende Regeln:

- Der Spieler bekommt Gold entsprechend dem Verkaufspreis.
- Heiltraenke werden stueckweise verkauft.
- Waffen und Ruestungen werden als ganzes Item verkauft.
- Ausgeruestete Waffen und Ruestungen werden im Shop nicht als verkaufbar angeboten.

## Geplante Features

- Kampf-Grafiken/Animationen fuer Angriffe, Schaden, Ausweichen und kritische Treffer
- Händler
- Quests
- Speichern und Laden

## Technologien

- Kotlin
- Jetpack Compose
- ViewModel
- StateFlow
- Navigation Compose

## Status

Version 0.1
