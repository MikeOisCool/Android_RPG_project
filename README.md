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
- Wenn ein neues Thema begonnen wird, soll Codex sich zuerst in die relevanten Dateien vollstaendig einlesen und erst danach Hints, Bewertungen oder naechste Schritte geben.
- Bei jedem Commit-Wunsch soll Codex pruefen, ob die README oder der Aenderungsverlauf angepasst werden muss. Wenn ein relevanter Eintrag fehlt, soll Codex die README vor den Git-Zeilen angleichen.
- Kleine Aenderungen nicht einzeln committen, sondern sinnvoll buendeln.
- Wenn der Nutzer `commit` schreibt, soll Codex vor den Git-Zeilen pruefen, ob es noch eine kleine naheliegende Aenderung gibt, die thematisch zu diesem Commit gehoert. Falls ja, soll Codex Michael zuerst bitten, diese kleine Aenderung noch mitzunehmen.
- Codex soll bei Commit-Wuenschen aktiv fragen: "Passt noch etwas Kleines zu diesem Thema in denselben Commit?" und erst danach die Git-Zeilen geben.
- Lernstil: langsam hinfuehren, keine fertigen Komplettloesungen, ausser der Nutzer fragt ausdruecklich danach. Lieber kleine Hints, Verstaendnisfragen, kleine Challenges und kurze Erklaerungen, damit Michael aktiv mitdenkt und nicht nur abschreibt.
- Michaels Wissensstand ist nicht mehr reiner Anfang. Codex soll ihn als fortgeschrittenen Anfaenger behandeln: nicht alles vorkauen, sondern zuerst Denkfehler finden lassen, kleine Aufgaben stellen und danach gezielt erklaeren.
- Bei einfachen Syntaxfehlern darf Codex kurz direkt helfen. Bei Logik, Architektur, Tests und Compose-State soll Codex erst eine Frage oder einen kleinen Hinweis geben, damit Michael selbst weiterdenkt.
- Codex soll nach Michaels eigener Loesung aktiv pruefen: Passt die Logik? Ist der Name gut? Gibt es einen einfacheren Ausdruck? Passt es zum bestehenden Stil?
- Codex soll Michael passend fordern: erst eine kleine Aufgabe oder Frage stellen, danach das Ergebnis pruefen und dann je nach Verstaendnis mehr fordern oder wieder langsamer erklaeren.
- Codex soll Michael mehr fordern, wenn er ein Thema sichtbar verstanden hat, z.B. durch kleine Zusatztests, Refactoring-Fragen oder "Was passiert bei Level 3?"-Aufgaben.
- Codex soll aber wieder langsamer werden, wenn ein neues Kotlin/Compose-Konzept auftaucht, z.B. `remember`, `by`, `LaunchedEffect`, `Modifier`, `StateFlow` oder Testaufbau.
- Codex soll vor dem naechsten Arbeitsschritt kurz sagen, welchen Schritt es vorschlaegt und warum, damit Michael entscheiden kann, ob dieses Thema jetzt dran ist oder ob ein anderes Thema sinnvoller ist.
- Codex soll darauf achten, nicht zu viele gleichartige Refactor-Schritte direkt hintereinander vorzuschlagen, wenn sie thematisch besser in einen vorherigen Commit gepasst haetten.
- In neuen Chats zuerst diese Arbeitsregeln lesen.

## Aenderungsverlauf, neueste Eintraege zuerst

- Hinweis: Die Datumsangaben sind aus den Git-Commits abgeleitet. Sie zeigen, wann ein Feature oder Refactoring ins Repository gekommen ist.

- 2026-08-20: BattleScene-Grafik verbessert; Himmel, Sonne/Wolken, Figuren-Schatten und Plattform-Boden ergaenzt.
- 2026-08-17: GameLogic schuetzt tote Spieler vor Ausruestungswechseln; Equip/Unequip fuer Waffen und Ruestungen wird bei Tod ignoriert und mit Tests abgesichert.
- 2026-08-16: Querformat-GameScreen weiter stabilisiert; TopLog, Spielerwerte, Gegnerwerte und Aktionsbuttons wurden klarer in linke und rechte Bereiche aufgeteilt, der Shop ist auch im Querformat erreichbar.
- 2026-08-16: Inventar schuetzt tote Spieler vor Ausruestungswechseln; Waffe und Ruestung koennen nach dem Tod nicht mehr aus- oder angelegt werden.
- 2026-08-15: Arbeitsregeln fuer Codex aktualisiert; Michaels Lernniveau wird als fortgeschrittener Anfaenger eingeordnet, Codex soll aktiver fordern, eigene Loesungen genauer pruefen und bei Commit-Wuenschen thematisch passende kleine Restarbeiten vorher ansprechen.
- 2026-08-04: Stats-Anzeige im Hoch- und Querformat weiter refactored; kleine StatRow/StatQuer-Bausteine reduzieren doppelte Label/Wert-Anzeigen.
- 2026-08-02: GameScreenHoch weiter refactored; PlayerStatsBlock, GameActionButtons und EnemyStatsBlock trennen Anzeige, Werte und Button-Aktionen klarer vom ViewModel.
- 2026-08-01: BattleScene-Tod-Feedback verbessert; beim Besiegen eines Gegners oder beim Tod des Spielers werden Schaden, Tod und Sieg kurz sichtbar angezeigt, bevor zum naechsten Zustand gewechselt wird.
- 2026-07-27: Angriffssperre gegen sehr schnelle Mehrfachklicks eingebaut; AttackEnemy wird bei laufendem Angriff, totem Spieler oder besiegtem Gegner ignoriert und der Angriffsbutton wird im Hoch- und Querformat sichtbar gesperrt.
- 2026-07-26: BattleScene-Feedback verbessert; Treffer-, Ausweich-, Tod- und Siegtexte werden links/rechts an der passenden Kampfposition angezeigt und zeitgesteuert ausgeblendet.
- 2026-07-24: Kritische Treffer werden vor der Verteidigung berechnet; BattleScene zeigt Treffertexte fuer Spieler- und Gegnerangriffe.
- 2026-07-20: Erste 2D-Kampfszene im Hochformat eingebaut; GameScreen ist scrollbaar, BattleScene zeigt Spieler/Gegner mit Namen, HP-Balken und Boden.
- 2026-07-20: Erste Kampfanimation eingebaut; Spieler und Gegner bewegen sich beim Angriff kurz aufeinander zu, BattleScene nutzt gegnerspezifische Icons.
- 2026-07-19: Shop-Logtexte in eigene Hilfsfunktionen ausgelagert und mit GameLogText-Tests abgesichert.
- 2026-07-19: Item-Zustandspruefungen fuer Shop-Events ausgelagert; ViewModel nutzt Helfer fuer ausgeruestete Items, Unique-Items und Waffen/Ruestungen.
- 2026-07-19: Shop-Logs im GameViewModel ergaenzt; doppelte Unique-Kaeufe und Verkauf ausgeruesteter Ausruestung werden abgefangen und mit ViewModel-Tests geprueft.
- 2026-07-19: Shop-Verkaufsbereiche weiter refactored; Verkauf von Traenken, Waffen und Ruestung laeuft ueber gemeinsame Shop-Komponenten. Waffen/Ruestung werden nicht mehr im Inventar weggeworfen, sondern ueber den Shop verkauft.
- 2026-07-19: Shop-Angebotsanzeige weiter refactored; Kaufstatus-Texte und Kaufbutton-Text wurden aus der UI-Entscheidung herausgezogen.
- 2026-07-19: Item-Pruefhelfer aus GameLogic in eigene Datei ItemChecks verschoben.
- 2026-07-19: Item-Icons fuer Shop und Inventar eingefuehrt; gemeinsame UI-Text-Hilfe fuer Item-Icons angelegt und Preis-Texte kompakter gemacht.
- 2026-07-19: Verkaufspruefung fuer vorhandene Inventar-Items ausgelagert und getestet.
- 2026-07-19: Unit-Tests fuer Item-Zustandshelfer ergaenzt.
- 2026-07-19: Shop-Kaufpruefungen fuer volle Heiltrank-Stacks und Unique-Items vereinheitlicht; GameLogic, ViewModel und ShopScreen nutzen gemeinsame Helfer.
- 2026-07-18: Shop-UI-Texte und Button-Darstellung ueberarbeitet; eigene Shop-Section und Shop-Buttons eingefuehrt, leere Texte zentriert und Shop-Verlassen-Button korrekt ausserhalb der Waffenliste platziert.
- 2026-07-15: GameLogic-Tests fuer Shop-Kauflogik ergaenzt; Heiltrank-Stacking, fehlendes Gold, Verkaufspreise und doppelte Waffenkaeufe werden geprueft.
- 2026-07-14: Haendler/Shop-Screen umgesetzt: levelabhaengige Angebote, Kaufen und Verkaufen, Gold-Pruefung, Stack-Limit fuer Heiltraenke sowie Verkaufsschutz fuer ausgeruestete Waffen/Ruestungen.
- 2026-07-11: GameLog-Meldungen fuer Angriff, Entfernen von Inventar-Items sowie Ablegen von Waffe und Ruestung ergaenzt.
- 2026-07-11: Haendler-Konzept mit levelabhaengigem Angebot, dynamischen Heiltrank-Preisen und Verkaufspreisen dokumentiert.
- 2026-07-11: Item-Balancing fuer Heiltraenke, Waffen und Ruestungen zentral in der README dokumentiert.
- 2026-07-08: Inventory-Logiktests und DropManager-Tests wurden ergaenzt; grosser Heiltrank Level-1-Test wurde vom Nutzer geschrieben und verstanden.
- 2026-07-08: Arbeitsregeln fuer Codex sowie Vision und Lernziel in der README festgehalten: Nutzer schreibt Code selbst, Codex erklaert/prueft und haelt README aktuell.
- 2026-07-08: Waffe und Ruestung koennen im Inventar abgelegt werden, ohne aus dem Inventar entfernt zu werden; GameLogic-Tests dafuer wurden vom Nutzer ergaenzt.
- 2026-07-06: Inventory-Item-Display weiter verfeinert.
- 2026-07-06: RemoveInventoryItem/Event/Logic/ViewModel/UI eingebaut; nicht ausgeruestete Waffen/Ruestungen koennen aus dem Inventar entfernt werden.
- 2026-07-05: Dodge-Handling, Dodge-Chancen und Dodge-Logging klarer getrennt.
- 2026-07-05: Potion-Usage-Logic und Potion-Logging getrennt/refactored.
- 2026-07-05: DamageResult eingefuehrt und Damage-Logik/Combat-Berechnung schrittweise geklaert.
- 2026-07-05: Inventory-Item-Vorbereitung in sichtbare Potion/Weapon/Armor-Listen geklaert.
- 2026-07-05: Base-Damage, Attack-Berechnung und Combat-Damage-Results extrahiert/refactored.
- 2026-07-04: InventorySection und EquipItem UI refactored; PotionItem als eigene Composable extrahiert.
- 2026-07-04: Game-Balance-Werte im GameViewModel extrahiert.
- 2026-07-03: Drops refactored und Inventory-UI verbessert.
- 2026-06-30: Drop- und Combat-Systeme weiter refactored.
- 2026-06-28: Weapon-Equip-System auf Item-Objekte umgestellt; Armor-System mit Ausruesten, Defense und Inventar-Updates eingebaut.
- 2026-06-28: Compose Previews fuer Game Screens ergaenzt; Game Screens, Player-Stats-Layout und scrollbares Inventar verbessert.
- 2026-06-28: DropManager und Item-Drop-System refactored; Inventory-Handling verbessert.
- 2026-06-27: Inventar erweitert; Waffen, Heiltraenke, Item-System und Waffenlogik wurden aufgebaut/refactored.
- 2026-06-14: Projektbasis gestartet (`RPG Version 0.1`), README angelegt, erste Kampf- und Drop-Logik refactored.

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
- Items aus Inventar entfernen - hinzugefuegt am 2026-07-06; Waffen und Ruestungen werden inzwischen bevorzugt ueber den Shop verkauft
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
- Shop-Logtexte - refactored und getestet am 2026-07-19
- Shop-Item-Zustandspruefungen - refactored am 2026-07-19
- Item-Zustandshelfer-Tests - hinzugefuegt am 2026-07-19
- Shop-Kaufpruefungen - vereinheitlicht am 2026-07-19
- Inventar-Verkaufspruefung - refactored und getestet am 2026-07-19
- ItemChecks-Datei - angelegt am 2026-07-19
- Item-Icons in Shop und Inventar - hinzugefuegt am 2026-07-19
- 2D-Kampfszene im Hochformat - hinzugefuegt am 2026-07-20
- Kampfanimation in BattleScene - hinzugefuegt am 2026-07-20
- BattleScene-Treffertexte und Krit-Schadensberechnung - angepasst am 2026-07-24
- BattleScene-Feedbacktexte fuer Treffer, Ausweichen, Tod und Sieg - angepasst am 2026-07-26
- Angriffssperre fuer schnelle Mehrfachklicks - hinzugefuegt am 2026-07-27
- BattleScene-Tod- und Siegfeedback - verbessert am 2026-08-01
- GameScreenHoch-UI-Bloecke und Button-Aktionen - refactored am 2026-08-02
- Stat-Anzeigen fuer Hoch- und Querformat - refactored am 2026-08-04
- Querformat-GameScreen mit eigenem TopLog, Shop-Aktion und klarerer Bereichsaufteilung - verbessert am 2026-08-16
- Ausruestungsaenderungen werden blockiert, wenn der Spieler tot ist - hinzugefuegt am 2026-08-16
- GameLogic-Tests fuer blockierte Ausruestungswechsel bei totem Spieler - hinzugefuegt am 2026-08-17
- BattleScene-Grafik mit Himmel, Deko, Schatten und Plattform-Boden - verbessert am 2026-08-20

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

## Naechste Lernfelder

- Tests fuer neue GameLogic-Regeln konsequenter schreiben
- ViewModel-Logs und GameLogic-Zustand sauber getrennt halten
- Compose-Layouts weiter in kleine, wiederverwendbare Bausteine aufteilen
- BattleScene und Querformat-UI weiter stabilisieren
- Bestehende Features wie Haendler, Kampfanimationen und BattleFeedback weiter verbessern

## Geplante Features

- Quests
- Speichern und Laden
- Bessere Grafik/Assets

## Technologien

- Kotlin
- Jetpack Compose
- ViewModel
- StateFlow
- Navigation Compose
- JUnit
- Coroutines

## Status

Lernprojekt in aktiver Entwicklung
