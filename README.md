# GAD-Extras
TUM GAD 2019 Extras (Tests &amp; co.)

Hier sind Testfälle zu den Hausaufgaben (und später vielleicht noch anderes) zu finden.

Das stellt für euch (von Seiten der ÜL) kein Problem dar:
> [...] solange die Tests keinen Code enthalten, der die Implementierung der Aufgaben vereinfacht, sollte das kein Problem sein.
 Wichtig ist, dass die Studenten trotzdem die main-Methode aus der Angabe ohne Veränderungen hochladen. Dass **zusätzlicher Code über die Problemstellung hinaus** bei mehreren Studenten gleich ist, stellt kein Problem dar. [...]

Entfernt die Tests/Testklassen bitte trotzdem vor der Abgabe, weil sonst die Tests auf TUM-judge nicht funktionieren.

## NEU: Automatische Versionsüberprüfung
Testfälle zu veröffentlichen ist immer ein Risiko, da diese dazu führen können, dass Leute ihren Code verschlimmbessern, wenn die Tests fehlerhaft sind. Auch die Suche nach Fehlern kann frustrierend sein, unter anderem auch weil nicht alle möglichen Fehler von den Testklassen ausreichend behandelt werden. Doch das ist nichts, was sich nicht beheben ließe. Stetig werden die Tests verbessert und aktualisiert, Bugs werden gefixt.

Das größte Problem:<br>
**Viele arbeiten immer noch lange mit den alten Tests weiter, ohne zu wissen, dass diese überarbeitet wurden.** 

Um dieses Problem zu lösen, gibt es jetzt die (von mir sehr empfohlene) Möglichkeit, durch das Hinzufügen von nur einer weiteren Klasse namens [CFUpdate.java](version/CFUpdate.java) automatisch nach neuen Versionen zu prüfen. Dies wird parallel zum Test gemacht und fällt kaum auf (außer es gibt eine neue Version). Die Überprüfung findet auch nur *maximal* alle 30 Minuten statt.

Für dieses Opt-In:
 * [Die Klasse](version/CFUpdate.java) herunterladen oder das bisschen Code darin kopieren.
 * Die Klasse zu den anderen Testklassen (in default-Package) ablegen.
 * Die Testklassen ausführen und bei Neuerungen informiert werden.

_(das geht natürlich erst bei Tests ab Version 1.2 für `blatt02` und Version 1.6 für `blatt03`)_

## Blatt02
Testklasse für das Testen von higher/lower bei BinSea. Ausführbar über die main-Methode. Lässt sich einfach zu den anderen Klassen packen.
Vor der Abgabe entfernen bzw. nicht mit abgeben, die Tests laufen sonst nicht durch.

*Die Klasse testet auch mit int-Arrays, die mehrfach den selben Wert enthalten, das ist wohl nicht erforderlich.
In dem Fall einfach die Testfälle, die mehrmals die 5 enthalten entfernen oder auskommentieren.*

**Bitte nicht mehr Version 1.0 oder älter verwenden! (aktuell: 1.2)**

## Blatt03
Momentan gibt es erstmal nur eine Testklasse für DynamicArray (was aber auch am wichtigsten ist), DynamicStack, StackyQueue und RingQueue. Lässt sich einfach zu den anderen Klassen packen und dann ausführen. Vor der Abgabe entfernen bzw. nicht mit abgeben, die Tests laufen sonst nicht durch.

*Wichtig: es gibt auch noch weitere richtige Lösungen, die vielleicht nicht "optimal" sind. Meine Lösung geht davon aus, dass Elemente bei einer Größenänderung an den Arrayanfang geordnet werden und dass immer nur das nötigste kopiert wird (Kopieraufwand minimieren)*

**Bitte nicht mehr Version 1.0 oder älter verwenden! (aktuell: 1.7)**

**Jetzt mit Tests zu DynamicStack, StackyQueue und RingQueue (ab Version 1.5)**
