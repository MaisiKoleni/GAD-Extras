# GAD-Extras
TUM GAD 2019 Extras (Tests &amp; co.) von Christian Femers ([MIT-Lizenz](LICENSE))

Hier sind Testfälle zu den Hausaufgaben (und später vielleicht noch anderes) zu finden.

Das stellt für euch (von Seiten der ÜL) kein Problem dar:
> [...] solange die Tests keinen Code enthalten, der die Implementierung der Aufgaben vereinfacht, sollte das kein Problem sein.
 Wichtig ist, dass die Studenten trotzdem die main-Methode aus der Angabe ohne Veränderungen hochladen. Dass **zusätzlicher Code über die Problemstellung hinaus** bei mehreren Studenten gleich ist, stellt kein Problem dar. [...]

Entfernt die Tests/Testklassen bitte trotzdem vor der Abgabe, weil sonst die Tests auf TUM-judge nicht funktionieren.

## Automatische Versionsüberprüfung (Version 1.2)
Testfälle zu veröffentlichen ist immer ein Risiko, da diese dazu führen können, dass Leute ihren Code verschlimmbessern, wenn die Tests fehlerhaft sind. Auch die Suche nach Fehlern kann frustrierend sein, unter anderem auch weil nicht alle möglichen Fehler von den Testklassen ausreichend behandelt werden. Doch das ist nichts, was sich nicht beheben ließe. Stetig werden die Tests verbessert und aktualisiert, Bugs werden gefixt.

Das größte Problem:<br>
**Viele arbeiten immer noch lange mit den alten Tests weiter, ohne zu wissen, dass diese überarbeitet wurden.**

Um dieses Problem zu lösen, gibt es jetzt die (von mir sehr empfohlene) Möglichkeit, durch das Hinzufügen von nur einer weiteren Klasse namens [CFUpdate.java](version/CFUpdate.java) automatisch auf neue Versionen zu prüfen. Dies wird parallel zum Test gemacht und fällt kaum auf (außer es gibt eine neue Version). Die Überprüfung findet auch nur *maximal* alle 30 Minuten statt.

Für dieses Opt-In:
 * [Die Klasse](version/CFUpdate.java) herunterladen *oder* das bisschen Code darin kopieren.
 * Die Klasse im default-Package plazieren.
 * Die Testklassen ausführen und bei Neuerungen informiert werden (können seit 1.2 auch in anderen Packages sein).

_(das geht natürlich erst bei Tests ab Version 1.2 für `blatt02` und Version 1.6 für `blatt03`; bei allen anderen Blättern immer)_

## [Blatt02](blatt02/) <span style="color:grey;font-size:large"> // Version 1.2</span>
Testklasse für das Testen von higher/lower bei `BinSea`. Ausführbar über die main-Methode. Lässt sich einfach zu den anderen Klassen packen.
Vor der Abgabe entfernen bzw. nicht mit abgeben, die Tests laufen sonst nicht durch.

*Die Klasse testet auch mit `int`-Arrays, die mehrfach den selben Wert enthalten, das ist wohl nicht erforderlich.
In dem Fall einfach die Testfälle, die mehrmals die 5 enthalten entfernen oder auskommentieren.*

**Bitte nicht mehr Version 1.0 oder älter verwenden!**

## [Blatt03](blatt03/) <span style="color:grey;font-size:large"> // Version 1.7</span>
Momentan gibt es erstmal nur eine Testklasse für `DynamicArray` (was aber auch am wichtigsten ist), `DynamicStack`, `StackyQueue` und `RingQueue`. Lässt sich einfach zu den anderen Klassen packen und dann ausführen. Vor der Abgabe entfernen bzw. nicht mit abgeben, die Tests laufen sonst nicht durch.

*Wichtig: es gibt auch noch weitere richtige Lösungen, die vielleicht nicht "optimal" sind. Meine Lösung geht davon aus, dass Elemente bei einer Größenänderung an den Arrayanfang geordnet werden und dass immer nur das nötigste kopiert wird (Kopieraufwand minimieren)*

**Bitte nicht mehr Version 1.0 oder älter verwenden!**

**Jetzt mit Tests zu DynamicStack, StackyQueue und RingQueue (ab Version 1.5)**

## [Blatt04](blatt04/) <span style="color:grey;font-size:large"> // Version 1.0</span>
Sehr ähnlich zu den Tests für Blatt 5 gibt es jetzt auch welche für Blatt 4, diese Testen nur grob das Verhalten der `hash(String)`-Methode der Klasse `HashString`, das beinhaltet die Prüfung des Wertebereichs (0 bis m-1), der Konsistenz (mehrmaliges ausführen resultiert in den selben Hashwerten) und der Verteilung, auch mit Diagrammen. Lässt sich einfach zu den anderen Klassen packen und dann ausführen. Vor der Abgabe entfernen bzw. nicht mit abgeben, die Tests laufen sonst nicht durch.

```java
// Diagramm Beispiel:
Test 2: TEST STRING HASH m == 17
  passed 2.1, result: hash() is consistent
  passed 2.2, result min: 0 (>= 0)
  passed 2.2, result max: 16 (<= 16)
  passed 2.3, result: 51,500000 (expected: 50,000000, delta: 25,000000)
 ┌───────────────────────────────────────────────────┐ (max: 60)
 │                         █  █     █           █    │
 │ █  ▄        █        ▄  █  █  █  █     █  █  █  █ │
 │ █  █  ▄     █     █  █  █  █  █  █  ▄  █  █  █  █ │
 │ █  █  █  █  █     █  █  █  █  █  █  █  █  █  █  █ │
 │ █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │ █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │ █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │ █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │ █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │ █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 └─┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼─┘ (y-scale: 6,0)
   ⁰  ¹  ²  ³  ⁴  ⁵  ⁶  ⁷  ⁸  ⁹ ¹⁰ ¹¹ ¹² ¹³ ¹⁴ ¹⁵ ¹⁶
```

## [Blatt05](blatt05/) <span style="color:grey;font-size:large"> // Version 1.1</span>
Jetzt gibt es Tests zu Blatt 5 für die folgenden Klassen: `DoubleHashInt`, `DoubleHashString`, `DoubleHashTable` (sowohl mit Integer als auch String). Es gibt auch hilfreiche Diagramme in der Konsole, um die Qualität der Hashfunktionen zu bestimmen und Probleme besser zu erkennen. Lässt sich einfach zu den anderen Klassen packen und dann ausführen. Vor der Abgabe entfernen bzw. nicht mit abgeben, die Tests laufen sonst nicht durch.

```java
// Diagramm Beispiel:
Test 2: TEST INT HASH_TICK
  passed 2.1, result: hashTick() is consistent
  passed 2.2, result min: 1 (>= 1)
  passed 2.2, result max: 16 (<= 16)
  passed 2.3, result: 53,000000 (expected: 53,125000, delta: 25,000000)
 ┌───────────────────────────────────────────────────┐ (max: 54)
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 │    █  █  █  █  █  █  █  █  █  █  █  █  █  █  █  █ │
 └─┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼──┼─┘ (y-scale: 5,4)
   ⁰  ¹  ²  ³  ⁴  ⁵  ⁶  ⁷  ⁸  ⁹ ¹⁰ ¹¹ ¹² ¹³ ¹⁴ ¹⁵ ¹⁶
```

**Bitte nicht mehr Version 1.0 oder älter verwenden!**
