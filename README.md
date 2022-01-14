# mHealth

Im Rahmen meiner Bachelor Thesis mit dem Titel "Konzeption und Implementierung einer mobilen
Anwendung als IT-Unterstützung in der
Rückfallprophylaxe bei Drogenabhängigkeit" ist diese mobile Anwendung entstanden.

## Build & Deploy

zum Bauen wird folgendes auf dem System vorrausgesetzt:
- [Maven](https://maven.apache.org/)
- [Java 8](https://openjdk.java.net/install/)
- [NPM](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
- [Angular CLI](https://angular.io/cli)
- [Ionic CLI](https://ionicframework.com/docs/cli)

das Deployment baut auf [Docker](https://docs.docker.com/get-docker/) bzw. [Docker-Compose](https://docs.docker.com/compose/install/) auf

Sofern alle Vorraussetzungen erfüllt werden kann in das Verzeichnis _/build_ gewechselt und das Skript _01_build.sh_ gestartet werden. 
> die build-Skripte rufen sich nacheinander auf
> soll nicht nur gebaut und deployed werden, sondern die bisheriger Container neuaufgebaut werden muss bei Skript _00_remove_old_docker.sh_ begonnen werden

Um das System vollständig testen zu können wird ein Mail Server benötigt. Dieser kann auch über Docker gestartet werden und anschließend in der DB in der Tabelle Param konfiguriert werden

> docker run -d --name=mailCatcher -p 1025:1025 -p 1080:1080 sj26/mailcatcher

## Kurzanleitung

Eine Anleitung einzelner UseCases findet man unter:
[Kurzanleitung](UserManual.adoc)

## Kernfunktionen
Es gibt 2 Benutzergruppen, die die Anwendung verwalten bzw. nutzen:
 - Suchtpatienten
 - Therapeuten

Grundfunktionen
- Warnfunktion
     - Patienten und Therapeuten können Orte definieren. Nähert sich der Patient einem dieser Orte wird eine Warnmeldung ausgegeben.
- Kontakt Suche
	- Es soll möglich sein, mit anderen Patienten in ähnlicher Situation in Kontakt treten zu können
- Kontakt aufbauen
	- Patienten können mehrere Kontaktpersonen oder Einrichtungen definieren zu denen Sie kurzfristig per Anruf oder Email Kontakt aufbauen können.
	- Zudem sollen Patienten mit ihrem Therapeuten über einen Chat kommunizieren
- Patientenunterstützung
	- Therapeuten können Ihren Patienten Übungen oder Maßnahmen zur selbstständigen Bearbeitung zur Verfügung stellen
- Datenerhebung
	- Es soll die Möglichkeit geben einen standardisierten SCL-90 Fragebogen zu Erstellen und den Patienten zur regelmäßigen Durchführung zur Verfügung zu stellen
	- Außerdem können auch individuelle Fragebögen erstellt werden
	- Für jeden Patienten wird ein Schwellwert definiert. Wird dieser bei einem Fragebogen überschritten bekommt der Therapeut eine Meldung.
	
Benutzergruppe: Patienten
- Warnfunktion
	- Es soll eine Warnung erscheinen, sobald man sich einem definierten Ort nähert.
	- Als Patient kann die Warnfunktion auf aktiv oder inaktiv gesetzt werden.
- Kontakt Suche
	- Es soll die Möglichkeit geben, andere Patienten in ähnlicher Situation und in einem bestimmten geografischem Umkreis zu orten um mit Ihnen in Kontakt treten zu können.
	- Neben der Ortung soll dann auch mit Patienten per Chat der Kontakt aufgenommen werden können.
	- Die eigene Ortung bzw. das eigene Auffinden soll deaktiviert werden können.
- Kontakt aufbauen
	- Patienten sollen eine oder mehrere Kontaktpersonen definieren können.
	- Es soll die Möglichkeit geben die definierte Kontaktperson direkt anrufen zu können
	- Es soll außerdem möglich sein, Beratungsstellen zu definieren und direkt per Anruf oder Email Kontakt aufzunehmen
	- Zusätzlich soll es eine Textnachrichten Funktion geben, mit der Patienten Ihren Therapeuten Nachrichten senden können.
- Patientenunterstützung
	- Dem Patienten werden Unterstützungsmaßnahmen zur selbstständigen Bearbeitung angezeigt.
- Datenerhebung
	- Dem Patienten werden standardisierte Fragebögen zur selbstständigen Bearbeitung angezeigt
	- Dem Patienten werden individuell erstellte Fragebögen zur selbstständigen Bearbeitung angezeigt
- sonstige Funktionen
	- Über einen Benutzernamen und Passwort kann die Anmeldung am System erfolgen
	- Nachdem ein Benutzerkonto über einen Therapeuten eingerichtet wurde, wird ein Initial-Passwort zur erstmaligen Anmeldung bereitgestellt
	
Benutzergruppe Therapeuten
- Warnfunktion
	- Gemeinsam mit dem Patienten sollen Orte, vor denen gewarnt werden soll, eingetragen und gelöscht werden können.
	- Kontakt aufbauen
	- Ein Therapeut ist Ansprechpartner für Suchtpatienten kann Nachrichten empfangen und zu einem beliebigen Zeitpunkt antworten
- Patientenunterstützung
	- Es gibt die Möglichkeit verschiedenen Patienten verschiedene Übungen zur selbstständigen Bearbeitung einzutragen
	- Es gibt die Möglichkeit eingetragene Maßnahmen zu löschen
- Datenerhebung
	- Es soll die Möglichkeit geben einen standardisierten SCL-90 Fragebogen zu Erstellen und den Patienten zur regelmäßigen Durchführung zur Verfügung zu stellen
	- Es soll die Möglichkeit geben individuelle Fragebögen zu erstellen
	- Es sollen zu jedem Patienten Schwellwerte zur Auswertung der Fragebögen festgelegt werden können
	- Die Fragebögen sollen ausgewertet werden können.
	- Sobald die Auswertung eines Fragebogens den definierten Schwellwert überschreitet, soll eine Nachricht versendet werden
- sonstige Funktionen
	- Über einen Benutzernamen und Passwort kann die Anmeldung am System erfolgen
	- Ein Therapeut kann einen neuen Patienten anlegen


## Screenshots

<div align="center">
  <img width="200" src="/Screenshots/Login-min.png">
</div>
  

<h6> Nach einem erfolgreichem Login wird einem, abhängig der Rolle (Therapeut oder Patient), die Startseite mit den Grundfunktionen präsentiert.</h6>

<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Startseite_Therapeut-min.png"/>
				<figcaption>Startseite: Therapeut</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Startseite_Patient-min.png"/>
				<figcaption>Startseite: Patient</figcaption>
			</figure>
		</td>
	</tr>
</table>


<h6> Für den Patienten gibt es die Zusatzfunktion ''Suche Kontakt''. Hier gibt es die Möglichkeit Patienten in der näheren Umgebung anzuzeigen um anschließend mit ihnen in Kontakt treten zu können. Außerdem kann die Funktion deaktiviert werden, damit kann man niemanden mehr finden wird aber auch selbst nicht mehr gefunden.</h6>
<figure align="center">
<img width="200" src="/Screenshots/Kontaktsuche-min.png"/> 
<figcaption>Startseite "Suche Kontakt"</figcaption>
</figure>

<h6> Bei der Funktion ''Kontakt aufbauen'' können Patient und Therapeut Kontakte pflegen.
Mit bereits vorhandenen Kontakten kann direkt interagiert werden, über den ''Chat'', einen Anruf oder per Email.</h6>

<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Kontakt_anzeigen-min.png"/>
				<figcaption>Anezeige der gepflegten Kontakte</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Kontakt_erstellen-min.png">
				<figcaption>Erstellen eines neuen Eintrages</figcaption>
			</figure>
		</td>
	</tr>
</table>

<h6> Die nächste Grundfunktion ''Warnfunktion'' soll den Patienten warnen, falls er sich einem potenziell gefährlichen Ort nähert.
Sowohl der Patient, als auch der Therapeut können Orte hinzufügen und bearbeiten.
Der Patient kann die Funktion, da hier der Standort übermittelt wird, deaktivieren.</h6>

<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Warnfunktion_Patient-min.png"/>
				<figcaption>Anzeige der Orte</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Warnfunktion_Ort_hinzufuegen-min.png"/>
				<figcaption>Erstellen eines Eintrages</figcaption>
			</figure>
		</td>
	</tr>
</table>

<h6> Der Menüpunkt ''Patientunterstützung'' ermöglicht dem Therapeut für seinen Patienten individuelle Übungen oder Maßnahmen einzutragen. Diese kann der Patient jederzeit aufrufen und einsehen</h6>

<figure align="center">
<img width="200" src="/Screenshots/Unterstützung_Patient-min.png"/> 
<figcaption>Patientenansicht der aufgelisteten Übungen</figcaption>
</figure>

<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Unterstützung_erstellung-min.png"/>
				<figcaption>Erstellung einer Maßnahme</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Unterstützung_Anzeige-min.png"/>
				<figcaption>Anzeige einer Maßnahme</figcaption>
			</figure>
		</td>
	</tr>
</table>

<h6> Über den Punkt ''Datenerhebung'' eröffnen sich für den Therapeuten die Möglichkeiten einen Fragebogen zu erstellen. Hierbei kann zwischen Ratings (von 0 bis 5 Punkten gewichtete Antwortmöglichkeit), Multiple Choice- und normalen Frei-Text Fragen gewählt werden.
Außerdem können bereits beantwortete Fragebögen eingesehen werden.
Und die offenen Fragebögen können den einzelnen Patienten, auch in einem automatischen wöchentlichem Turnus, zugewiesen werden.

Der Patient kann hier die Fragebögen beantworten. Wird dabei ein vorher definierter Schwellwert überschritten (anhand der Rating-Fragen) wird der Therapeut per Email informiert.</h6>

<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Datenerhebung_Fragebogen_Startseite-min.png"/>
				<figcaption>Startseite aus Sicht des Therapeuten</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Datenerhebung_Fragebogen_einsehen-min.png"/>
				<figcaption>Anzeige eines ausgefüllten Fragebogens</figcaption>
			</figure>
		</td>
	</tr>
</table>


<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Datenerhebung_Fragebogen_erstellen-min.png"/>
				<figcaption>Erstellung eines Fragebogen</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Datenerhebung_Fragebogen_Kontext_Menu-min.png"/>
				<figcaption>Menü eines Fragebogens</figcaption>
			</figure>
		</td>
	</tr>
</table>

<figure align="center">
<img width="200" src="/Screenshots/Datenerhebung_Fragebogen_ausfüllen-min.png"/> 
<figcaption>Ausfüllen eines Fragebogens (nur für Patienten)</figcaption>
</figure>



<h6> Die vorletzte Grundfunktion ''Chat''. Hier werden die bereits eröffneten Konversationen angezeigt und man kann direkt Nachrichten verfassen und versenden.</h6>
<br>
<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Chat_Startseite-min.png"/>
				<figcaption>Startseite Chat</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Chat_geöffnet-min.png"/>
				<figcaption>geöffneter Chat</figcaption>
			</figure>
		</td>
	</tr>
</table>


<h6> Der letzte Menüpunkt 'Sonstiges'  bietet noch essentielle Zusatzfunktionen wie: Passwort ändern, Kontodaten löschen, Patient aufnehmen und Schwellwerte (für die Fragenbögen) setzen.</h6>

<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Sonstiges_Patient-min.png"/>
				<figcaption>Sonstiges: Patientensicht</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Sonstiges_Therapeut-min.png"/>
				<figcaption>Sonstiges: Therapeutensicht</figcaption>
			</figure>
		</td>
	</tr>
</table>

<table style="border: none">
	<tr>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Sonstiges_Patient_aufnehmen-min.png"/>
				<figcaption>Patient aufnehmen</figcaption>
			</figure>
		</td>
		<td>
			<figure>
				<img width="200" src="/Screenshots/Sonstiges_Schwellwerte_festlegen-min.png"/>
				<figcaption>Schwellwerte festlegen</figcaption>
			</figure>
		</td>
	</tr>
</table>


