# Installation

Go to [INSTALLATION.md](INSTALLATION.md)

# OOKA Übung 5

In dieser Übung wurden fünf Spring Boot Microservices erstellt:

- [power-systems](power-systems): Ein Service der 5 Sekunden dauert und ein Integer als zurückgibt.

- [liquid-gas-systems](liquid-gas-systems): Ein Service der 7,5 Sekunden dauert und ein Integer als zurückgibt.

- [management-systems](management-systems): Ein Service der 5 Sekunden dauert und ein Integer als zurückgibt.

- [analysis-control](analysis-control): Führt algorithmA und algorithmB parallel aus berechnet das finale Resultat.

- [cli](cli): Zur Steuerung von analysis-control.

![](img/uml.png)

## Aufgaben

> Simulieren sie die Eingabe einer Konfiguration des Optional Equipments für eine Diesel Engine.
> Dies kann eine einfache Eingabe-Maske sein, die über einen separaten Microservice
> bereitgestellt werden kann.

Als Eingabemethode wird eine einfache [CLI](cli/src/main/java/com/ooka/cli/Cli.java) verwendet.

> Eine Konfiguration soll über einen Microservice persistent verwaltet werden können.

Die Konfiguration wird im Microservice des
[AnalysisController](analysis-control/src/main/java/com/ooka/analysis/AnalysisController.java) persistent gehalten
und kann von diesem zur Analyse verwendet werden. Ein möglicher weiterer Schritt wäre, es zu ermöglichen, die
gespeicherte Konfiguration zu editieren.

> Die eigentliche Analyse soll über einen entsprechenden Button oder einem sonstigen
> Interaktionselement ausgeführt werden können. Durch diese Interaktion wird ein bestimmter
> „Anker“-Algorithmus ausgeführt.

Als Eingabemethode wird eine einfache [CLI](cli/src/main/java/com/ooka/cli/Cli.java) verwendet.

> Wie von dem Ingenieur vorgeschlagen, sollten die Algorithmen auf choreographierte Microservices
> aufgeteilt werden, die sich nacheinander in einer Sequenz aufrufen können. [...]
> Das finale Ergebnis der Choreographie soll dann in der Benutzeroberfläche des Microservice
> dargestellt werden.

Die Algorithmen werden
vom [AnalysisController](analysis-control/src/main/java/com/ooka/analysis/AnalysisController.java)
choreografiert und aufgerufen. Das zentrale Aufrufen der Algorithmen erlaubt eine einfache Erweiterung
auf die parallele Ausführung der Algorithmen. Sind alle Algorithmen beendet, gibt der Controller das Ergebnis der
Analyse aus und speichert dieses in der persistierten Konfiguration.

> Sie können die Algorithmen nach einer eigenen Systematik clustern [...]. Somit sollten sie
> mindestens 3-4 Algorithmen bzw. Microservices bereitstellen.

Die Algorithmen wurden in "Power Systems", "Liquid and Gas Systems" und "Management Systems" aufgeteilt.

> Wie in der Anforderungsbeschreibung des Ingenieurs beschrieben, soll der Bearbeitungs-Status
> der Algorithmen in der Benutzeroberfläche der Anwendung stets zu entnehmen werden.

Es werden die im Enum [State](analysis-control/src/main/java/com/ooka/analysis/State.java) festgelegten
Zustände *IDLE*, *RUNNING*, *SUCCEEDED* und *FAILED* verwendet.

> Beachten sie zudem den Fall, dass Microservices nicht erreichbar sind. Verwenden sie das
> Pattern Circuit Breaker (Kapitel 4), um die Widerstandsfähigkeit der gesamten Anwendung
> zu garantieren.

Bisher wurde das Curcuit Breaker Pattern noch nicht umgesetzt. Dies soll in Kombination mit der Implementierung
von Netflix Hystrix stattfinden.

> Machen sie sich Gedanken über die Gestaltung der Schnittstellen der einzelnen Microservices;
> diese sollten als REST-Endpoints entwickelt werden.

Jeder Microservice verwendet REST-Controller als Schnittstellen, mit denen über HTTP-Requests
kommuniziert werden kann.

