Proprastination - KorrektorInnen Bewerbung
===


Einführung und Ziele {#section-introduction-and-goals}
====================

**Einfach, praktisch und gut**

Was ist unserer Ziel? Wir möchten eine einfache, übersichtliche und intuitive Platform bieten, auf der Bewerbungen erstellt und abgeschickt, sowie direkt bearbeitet werden können. 
Bewerber sollen die Möglichkeit haben Bewerbungen für mehrere Module abschicken zu können und den Bearbeitungsstatus jener einzusehen (in Bearbeitung oder fertig).
Unser System soll mit Filtern die eingetroffenen Bewerbungen gruppieren und so eine simple Übersicht bieten, mit der Organisatoren ihre zukünftigen Korrektoren priorisieren können. 
Ebenso soll unser System dem Verteiler eine intuitive Oberfläche bieten um die Korrektoren den passenden Modulen zuzuordnen.
Dabei wollen wir eine Möglichkeit geben, die Daten bearbeiten zu können, bis die Frist abgelaufen ist. 
    All dies soll möglichst transparent ablaufen, damit für alle Beteiligten der Prozess nachvollziehbar bleibt (sofern dies notwendig ist). Dafür wollen wir unser System so einfach wie möglich halten und Schnittstellen nur dort anwenden, wo sie tatsächlich gebraucht werden. 


Aufgabenstellung {#_aufgabenstellung}
----------------

**Inhalt.**

Folgende Aufgabenstellung wird vorrausgesetzt:
>**Korrektorinnen Bewerbung**
>In jedem Semester werden studentische Hilfskräfte für den Übungsbetrieb benötigt. In (zumindest) den Grundlagenveranstaltungen wird dazu ein gemeinsames Bewerbungsverfahren benutzt:
>- Bewerber füllen einen Fragebogen aus.
>- Nach Ablauf der Frist werden die Bewerberinnen, die potentiell für eine Stelle in Frage kommen gruppiert und den Verantwortlichen der Veranstaltung zur Verfügung gestellt. Bewerberinnen kommen in Frage, wenn sie eine Veranstaltung nicht ausgeschlossen haben.
>- Die Verantwortlichen geben für jede Bewerbung eine Priorität an.
>- Die Verteilung auf die einzelnen Veranstaltungen werden von einer verantwortlichen Person manuell durchgeführt, dazu wird aber eine hinreichend gute Darstellung der gesammelten Informationen gebraucht
>- Am Ende sollen automatisch die Einstellungsbögen für die Personalabteilung als PDF erzeugt werden


**Motivation.**

Das derzeitige System zur Bewerbung läuft über das Ilias. Das ist zwar gut, um die Daten des Bewerbers zu bekommen, jedoch nimmt der ganze Prozess inklusive manuellem verschicken und duplizieren der Daten sehr viel Zeit in Anspruch.
Wir wollen ein Subsystem für MOPS bieten, welches wesentlich besser, schneller und einfacher zu handhaben ist.

**Form.**

- Bewerber-Dashboard mit übersichtlicher Bewerbung
    - Bewerbungen sind editierbar bis zum Frist-Ende
- Organisator-Dashboard mit vereinfachter Priorisierung
    - Bewerbungen nach den wichtigsten Eigenschaften filterbar
    - Anbindung an Terminfindung für eventuelle Einladung zum Vorstellungsgespräch
    - Einfache Priorisierung 
    - Übersicht über Mehrfachbewerber
- Verteiler-Dashboard mit intuitiver Zuordnung
    - Prioritäten von Bewerber und Organisator auf einen Blick
    - Einfache Zuteilung der Bewerber zum Modul
    - Möglichkeit zur Änderung vor der endgültigen Absendung
    - Legt die Wochenstundenzahl endgültig fest
- Automatisierte Erstellung der Einstellungsbögen mithilfe der Bewerbungsinformationen und Möglichkeit zum Download

Qualitätsziele {#_qualit_tsziele}
--------------


| Priority | Quality Goal | Reason |
| -------- | -------- | -------- |
|   1   |   Maintainability   |   Allgemein wichtig für die Wartbarkeit, Kostenreduzierung, etc.   |
|   2   |   Compatibility   |   Das Teilsystem soll sich einfach und unkompliziert mit den anderen System verbinden lassen und Möglichkeiten für Erweiterungen bieten.   |
|   3   |   Usability   |   Ansprechende UI und intuitive Nutzung für eine userfreundliche Handhabung  |
|   4   |   Reliability   |   Ausfälle sollen möglichst vermieden werden.  |


Stakeholder {#_stakeholder}
-----------

Tabelle mit Rollen- oder Personennamen, sowie deren Erwartungshaltung
bezüglich der Architektur und deren Dokumentation.


| Rolle | Kontakt |
| -------- | -------- |
|   Entwickler  |   Sind selber Studenten und wollen eine optimierte Plattform, sammeln wichtige Kenntnisse in der Softwareentwicklung und das Praktikum bestehen |
|   Bewerber   |   Wollen die Möglichkeit, ihre Bewerbung zu ändern und eine Statusmeldung zu haben|
|   Organisatoren   |   Brauchen einfachen Weg der Priorisierung ohne langes Kopieren aller Bewerbungen    |
| Ersteller | Will möglichst schnell und einfach die zur Bewerbung offenen Module mit den jeweiligen Einstellungsinformationen eintragen
|   Verteiler   |  Abgesehen vom praktischen, automatischen Erstellen der Einstellungsbögen wollen Sie eine einfache und bestenfalls teils-automatisierte Verteilungsmöglichkeit|
|   Jens Bendisposto  |   Möchte endlich das AUAS verlassen können und will, dass wir das Praktium bestehen und ein tolles System zusammenstellen  |


Randbedingungen {#section-architecture-constraints}
===============




**Technisch**

| Randbedindung | Erläuterung, Begründung |
| -------- | -------- |
|Implementierung in Spring-Boot mit Java | Vorgegeben und bisher auch fast ausschließlich bei vorbereiteten Projekten genutzt |
| Verknüpfung mit anderen Subsystemen durch Links|Organisatorisch einfacher, hebt viele Abhängigkeiten auf, was isoliertes Arbeiten erleichtert |
|System als Docker Image| Das System wird mit einem Befehl einheitlich gestartet, einfacher für die Korrektoren|
|Gemeinsamer Styleguide|Einheitliches Aussehen und ersparen von Kleinarbeit|
|Authentifizierung über KeyCloak|Absicherung der Anwendung und Aufteilung in verschiedene Rollen|
|CheckStyle & Spotbugs| Muss durchlaufen, überprüft Coding Conventions|

**Organisatorisch**

| Randbedindung | Erläuterung, Begründung |
| -------- | -------- |
|Merge in develop Branch nur über Pull Request| Kleinschrittiges Arbeiten, Requests können zugewiesen werden|
|Code Review| Build gründlich reviewen, Fehler nicht selber beheben|
|Team | Kleinere Teams für Features, arbeiten auf Feature Branches|
|Zeitplan| Einen Monat Zeit, Grundbuild soll nach maximal der Hälfte der Zeit sicher stehen|
|Vorgehensmodell| Schicht-Architektur, testgetrieben, Dokumentation nach arc42|

**Konventionen**

| Randbedindung | Erläuterung, Begründung |
| -------- | -------- |
|Coding Conventions| Java Conventions von Sun, überprüft von Checkstyle|
|Architekturdokumentation| Nach dem deutschen arc42 Template|
|Sprache| Deutsch für HTML Seiten und Rollen da User größtenteils deutschsprachig sind, Englisch für Benennung der Klassen/Methoden/Kommentare im Code (CamelCase)|


Kontextabgrenzung {#section-system-scope-and-context}
=================

Eine Beschreibung des Umfeld unseres Systems, besonders die Schnittstellen mit Usern und anderen Subsystemen.

![](https://i.imgur.com/X6joKOX.png)


Fachlicher Kontext {#_fachlicher_kontext}
------------------

**Nutzer**

* *Ersteller*

Der Ersteller gibt die jeweiligen Module zur Bewerbung frei und startet den Bewerbungsprozess. Dies kann ein Student, Professor oder Mitarbeiter sein.

* *Bewerber*

 Allen voran sind unsere Nutzer die Studenten und Mitarbeiter, die sich um eine Stelle bewerben wollen. Sie haben ihre persönlichen Daten und offenen Bewerbungen direkt auf einen Blick und können alles bearbeiten, zumindest bis zum Bewerbungsschluss.


* *Organisator*

Der Organisator, der zu Anfang meist auch die Bewerbung für sein Modul eröffnet, muss für jede Bewerbung eine Priorität von 1-4 setzen, wobei 1 die höchste Priorität ist. Er gibt auch seine gewünschten Wochenarbeitsstunden für den Verteiler an.

* *Verteiler*

Am Ende bekommt der Verteiler gesammelt alle Bewerbungen pro Modul mit der Priorität des Bewerbers und des Professors/Organisators. Anhand dessen wird erst einmal automatisch eine Zuweisung erstellt, die manuell noch veränderbar ist. Er legt auch die engültige Wochenarbeitszeit fest.

**Personalabteilung**

Nachdem nun die Korrektoren und Tutoren für jedes Fach ausgewählt worden sind, werden von unserem System automatisch Einstellungsbögen erstellt und an die Personalabteilung geschickt.


Technischer Kontext {#_technischer_kontext}
-------------------

**KeyCloak**

KeyCloak wird benutzt um die einzelnen Nutzer unseres Systems in Rollen aufzuteilen, die den Zugangs zu bestimmten Seiten zulassen oder einschränken. So sollte beispielsweise ein nur als Student angemeldeter User keinen Zugriff auf die Seite des Verteilers haben, wenn er diese Rolle nicht innehat.

**Datenbank**

Für das Development nutzen wir eine einfach zu benutzende h2 Datenbank mit Dummydaten, die bei jedem Start der Mopsapplication neu eingepflanzt werden. In der Produktion würde an diese Stelle eine mysql Datenbank treten, die die Daten auch persistiert.

Lösungsstrategie {#section-solution-strategy}
================

**Qualitätsziele und Arbeitsansatz**

*Ausgehend von unseren Qualitätszielen (siehe oben)*



* *Effizienz*:

  Automatischer Verteilalgorithmus
  
  Einfaches Hinzufügen von mehreren Bewerbungen
  
* *Benutzbarkeit*:

  Übersichtliche UI's mit intuitiven Buttons zum Hinzufügen von Bewerbungen direkt vom Dashboard
  
  Bearbeitungsmöglichkeit von Bewerbung und auch Verteilung
* *Zuverlässigkeit*:

  Checks und Abfragen der Eingaben
  
  Sicheres Abspeichern der Daten


*Motivation*

Wir haben am Anfang des Projekt den Fehler gemacht, mit der Frontend und Backend Bearbeitung gleichzeitig anzufangen. Davon ausgehend mussten wir mehrmals unsere Ideen neu überdenken, besonders was die Datenbank angeht. Wir mussten über unsere Features diskutieren und realistisch auswählen, welche wir implementieren können. Die groben Unterteilungen unseres Subsystems waren von Anfang an klar, eine in die jeweiligen Rollen und die andere in Backend und Frontend. Wir haben versucht in maximal Zweierteams zu arbeiten, aber da es schnell zu Problemen kam haben sich die Abgrenzungen aufgeweicht.

Bausteinsicht {#section-building-block-view}
=============

**Inhalt**

Hier ein Blick auf das System was wir am Anfang des Praktikums entwerfen wollten.

![](https://i.imgur.com/hSKyeqx.jpg)


Die grobe Struktur haben wir beibehalten, abgesehen davon, dass die Datenbank weit mehr als die Bewerbung speichert. Außerdem mussten wir eine weitere Rolle hinzufügen, die des Erstellers, der Module für die Bewerbung einstellt.
Die genaue Erklärung zu den einzelnen Rollen findet sich im Abschnitt **Stakeholder** und **Nutzer**.


Whitebox Gesamtsystem {#_whitebox_gesamtsystem}
---------------------


![](https://i.imgur.com/tDPmRGb.png)


Begründung

:  Die Zerteilung ist standardmäßig in die *Java Klassen*, die die Logik des Programms enthalten, die *Resources* mit dm Frontend Design und Text Dokumenten, sowie die *Tests* mit denen das Programm so weit wie möglich abgedeckt sein sollte.




### Java Klassen {#__name_blackbox_1}

![](https://i.imgur.com/GNnlHYg.png)



#### (1) << config >>

Erfüllt die Sicherheitsanforderungen an unser System und schützt die Daten der Nutzer.

| Datei | Inhalt | Zweck |
| -------- | -------- | -------- |
| Keycloak config| Erstellt ConfigResolver| Workaround für einen Bug von Keycloak |
| Security Config | Configuration, Access an die Account tokens | Regelt die Accounts und Anmeldung für das ganze System |


**Schnittstellen**

-   Keycloak
-   Spring security

### (2) << controller >> {#__name_blackbox_2}

Kontrollieren den Fluss der HTML Websites und das Weitergeben der Daten aus den Formularen an die Services, sowie das Weitergeben der Daten aus den Services an die templates.

*Webcontroller*

| Datei | Zweck |
| -------- | -------- |
| Application Controller | Nimmt das Mapping der Rolle *studentin* entgegen und lädt die gespeicherten persönlichen Daten (auch Keycloak Account Daten) und die offenen Bewerbungen als Java Webclassen (siehe Abschnitt 6) in die html Seiten des Bewerbers und gibt Änderungen an den Service weiter |
| Orga Controller | Übergibt an den Organisator die Bewerbungen für sein jeweiliges Modul und nimmt aus den Formularen die vom Organisator eingetragene Priorität und die gwünschte Arbeitszeit an |
| Distributor Controller | Gibt an die Verteilerseite alle eingegangenen Bewerbungen, diese wurden schon vorher mit einem Algorithmus bestmöglich verteilt. Gibt etwaige manuelle Änderungen an der Verteilung an den VerteilerService weiter |
|Setup Controller| Ermöglicht dem Ersteller auf seiner Seite auf die zur Bewerbung freigegebenen Module zuzugreifen und neue hinzuzufügen oder sie zu Ändern |

*Andere*

| Datei | Zweck |
| -------- | -------- |
| Logout Controller | Loggt den derzeitigen Benutzer aus und leitet auf die Startseite weiter |
| Distribute Controller | Verteilt am Anfang die jeweiligen User ihren Rollen entsprechend auf die einzelnen Startseiten|
| PDF Controller | Ermöglicht das Schreiben in die Einstellungsbögen, die als pdf gespeichert vorliegen |


#### (3) << testenvironment >>

Erstellt die Dummydaten für die Development Datenbank zum testen und arbeiten.

| Datei | Inhalt | Zweck |
| -------- | -------- | -------- |
| Databaseinit | Methoden die Einträge für die Datenbank erstellen| Daten zur Verfügung zu stellen mit denen die Entwickler die Funktionalität sicherstellen können |

#### (4) << services >>

Bieten die Schnittstelle zwischen Controller und Repository, also zwischen der Website und der Datenbank, sowie die Möglichkeit externe Dateien zu  schreiben und zu bearbeiten.

*Database Services*

Speichern und Laden Daten zu Entities in der Datenbank mithilfe des jeweiligen Repositories.
Veränderte Daten werden weitergereicht und es kann nach bestimmten Attributen gesucht werden.


*Webservices*
| Datei | Inhalt | Zweck |
| -------- | -------- | -------- |
| Account Generator | erstellen der KeyCloak Accounts | Ausgelagert, da duplizierter Code |
| Distribute Service | Rückgabe einzelner html Paths und checken der Rollen | Einfacheres und zentraleres Weiterleiten je nach angemeldeter Rolle |
| Orga Service | Nutzt die anderen Entity Services und deren Methoden | Stellt dem Orga Controller die entsprechenden Bewerber, Bewerbungen und Prioritäten zur Verfügung und gibt die eingegebene Organisator Präferenz an den Evaluation Service weiter|
| Student Service | Methoden zur Umwandlung von Webklassen in Modelklassen und vice versa | Aus dem Controller ausgelagerte Logik die den Übergang zwischen den im Frontend verwendeten Webklassen und den in der Datenbank benutzten Modelklassen |
| Webapplication Service | Ausgabe von Binding errors und Erstelen von Bwerbern falls es noch keinen für diese Bewerbung gibt | Überprüft Eingaben des Bewerbers und löscht das Modul der abgesendeten Bewerbung aus der Liste möglicher weiterer Bewerbungen. |
| WebDistributionService | Suchmethoden zu den unterschiedlichen Arbeitszeiten sowie Umwandlungsmethoden | Mittler zwischen den in der Datenbank eingetragenen Verteilungen und der Darstellung auf der Seite des Verteilers |
| Webmodul Service | Wandelt Web Module in Module und andersherum und speichert oder schickt diese weiter | Zur einfacheren Handhabung im Setup Controller |
| WebOrganizer Service | Gibt Daten aus den Organisator Seiten an die Datenbank weiter und Ergebnisse aus Suchen im repository zurück | Die Organisator Daten werden vor allem für die Erstellung der pfd Bögen gebraucht, der Service speichert Änderungen die eingegebn werden |
| WebPdf Service | Gibt Download Seiten für die Einstellungsbögen zurück oder stellt diese als Zip Download zur Verfügung. Er initiiert auch das Versenden der Mails mit den Einstellungsbögen.|  |


*Andere*

| Datei | Inhalt | Zweck |
| -------- | -------- | -------- |
| CSV Service | Schreiben in und Lesen aus CSV Dateien, sowie suchen nach bestimmten Einträgen| Wurde am Anfang für die Module genutzt, nachdem diese in die Datenbank verschoben wurden lädt der Service nur die Länder- und Studienfachauswahl für die Bewerbungsseite, sowie die Semestermöglichkeiten|
| Email Service | Sendet Mails an eine oder mehrere Personen | Wird zum verschicken der Personalbögen eingesetzt |
| PDF Service | Öffnet eine pdf Datei und fügt Informationen ein | Hier werden die Einstellungsbögen mit den Daten der Bewerber, Bewerbungen und Organisatoren erstellt |
| Distribution Service | Beinhaltet ein wichtiges Feature, den Sortier Algorithmus der dem Verteiler die Bewerbungen in die einzelnen Module vorsortiert. Weiterhin sind dort einige Umwandlungsmethoden und die Logik hinter dem drag and drop System der html Seite |

#### (5) << repositories >>

Lesen und schreiben Daten direkt aus der Datenbank und können nach bestimmten Feldern/Bedingungen suchen. Wir nutzen CrudRepository extensions, bei denen wir die Methodennamen so anpassen das die Queries nicht selber geschrieben werden müssen. Die save() Methode ist bei allen repositories schon vorhanden und wir in den services aufgerufen.

| Datei | Zweck |
| -------- | -------- |
| Applicant repository | Lädt alle Bewerber aus der Datenbank mit ihren Details als applicant Objekte und kann nach einzelnen Usern anhand der iD oder einer Bewerbung suchen|
| Application repository | Lädt alle Bewerbungen und kann nach einzelnen Bewerbungen anhand der iD oder mehreren anhand des eingetragenen Moduls suchen, was für den Organisator und Verteiler wichtig ist|
| Course repository | Da die möglichen Studienfächer im Moment noch statisch von uns eingefügt werden, gibt das repo nur eine gesammelte Liste an courses zurück aus denen der Student bei der Bewerbung wählen kann |
|Distribution repository| Speichert und lädt die Zuteilung des Verteilers, bei der die Bewerbung und das Modul enthalten sind. Kann nach iD und Modul gefilter/gesucht werden |
| Evaluation repository | Findet einzelne Bewertungen mit Priorität des Professors und der Bewerbung und stellt sie dem Verteiler zur Verfügung  |
| Module repository | Gibt alle Module die vom Ersteller hinzugefügt wurden heraus und lässt nach einzelnen Modulen per Name suchen, wird auch zum löschen und leeren der ModulTabelle genutzt |
| Organizer repository | Gibt alle eingetragenen Organisatoren wieder und lässt nach bestimmten Unikennungen suchen |

#### (6) << Modelklassen >>

Erstellen mit JPA Hibernate die Tabellen in der Datenbank und sind Transporter der Daten zwischen Frontend und Backend. Die grobe Unterteilung ist hier in Webklassen, die in den Controllern für die html Seiten genutzt werden, und die Datenbank Klassen mit iD Feld.

*Datenbankklassen*

| Datei | Zweck |
| -------- | -------- |
| Module | Speichert Name, Kurzform und Verantwortlichen für das Modul, sowie die gewünschte Anzahl an Mitarbeitern mit 7,9 und 17 Wochenstunden. Hat eine eigene Methode die es in ein WebModule verwandelt|
| Applicant | Speichert die standardmäßigen persönlichen Daten des Bewerbers zusätzlich zu seiner Unikennung, sowie den Studienganz, die gewünschte Einstellungsart (Tutor/Korrektor/Beides), den bisher höchsten Abschluss falls es einen gibt und die bisher abgeschickten Bewerbungen. Dazu gibt es eine Methode die einzelne Bewerbungen raussucht |
| Address | Speichert die Adresse des Bewerbers, als Feld dort gespeichert |
| Certificate | Speichert den höchsten Abschluss des Bewerbers, falls es einen gibt |
| Course | Speichert den Studiengang, die Liste an möglichen Studiengängen ist in der Datenbank gespeichert |
| Application | Speichert die Bewerbungsdetails, abgesehen vom Modul und der Mindest- und Maximalarbeitszeit auch die Note und Details des eigenen Besuchs des Bewerbungsmoduls |
| Evaluation | Repräsentiert die "Bewertung" des Organisators der einzelnen Bewerbungen, speichert die Bewerbung und die eingegebene Priorität des Organisators, sowie die Wochenstunden und ein Kommentar falls nötig |
| Distribution | Speichert die Einteilung des Verteilers, also das Modul und die zugewiesenen Bewerber |
| Organizer | Speichert persönliche Daten der Organisatoren |

*Webklassen*

Ähnliche Gegenstücke zu den Modelklassen, werden für die Controller zum rendern in der html Seite benötigt. Da wir uns für Thymeleafs th:object zum wrappen der einzelnen Daten in den Eingabefeldern der Websites entschieden haben, oönnten wir die Modulklassen mit ihrerm iD Feld nicht automatisch erstellen und ausfüllen lassen, daher diese Methode.
Haben fast Felder der entsprechenden Modelklasse bis auf die iD und werden zum Beispiel beim Applicant zum zusammentragen und zemtral speichern der Informationen (Adresse, Abschlüsse, Bewerbungen) genutzt. Die WebDistribution vom Verteiler speichert zusätzlich auch noch der Einfachheit halber die finale Arbeitsstundenzahl und hat spezielle WebDistributionApplicant und -Application Klassen die nur die nötigen Infos speichern und laden.

*Andere*

| Datei | Zweck |
| -------- | -------- |
| Priority | Enum das mögliche Werte für die Priorität angibt (1-4)|
| Role | Enum das die möglichen Anstellungsarten angibt (Korrektor/Tutor/Beides) |
| Account | Speichert die Daten des Keycloak accounts |
| Document | Speichert Dokumente und sucht nach pdf Feldern |


### Resources {#__name_blackbox_1}

![](https://i.imgur.com/ZpMXR8Y.png)


#### (1) << bootstrap >>

Bindet den Style ein der von Mops vorgegeben ist.

#### (2) << html templates >>

Die einzelnen Seiten die jede Rolle hat. Lassen manuell Daten eintragen und geben alles zum speichern weiter.

*Ersteller*

Der Ersteller befindet sich zuerst auf seinem Dashboard (**/setupMain**), auf diesem sieht er seine bereits eingetragenen Module, die offen für Bewerbungen sind. Durch Buttons kann er neue Module hinzufügen und wird dazu auf eine Seite geleitet bei der er die Details eingeben muss (**/neuesModul**).
Weiterhin kann er bestehende Module bearbeiten (**/modulBearbeiten**), komplett löschen und alle Module aus der Datenbank löschen. Er ist verantwortlich dafür, die Deadlines für Bewerber und Organisatoren zu setzen.

*Bewerber*

Auf dem Dashboard des Bewerbers (**/applicantMain**) sieht dieser seine persönlichen Daten von seinem Keycloak account, seine bereits abgegebenen offenen Bewerbungen und hat die Möglichkeit neue hinzuzufügen. Sollte er das tun, wir er erst aufgefordert seine persönlichen Daten die in WebApplicant gespeichert werden einzugeben (**/applicationPersonal**), dann kann er aus einem Dropdownmenü ein für ihn noch verfügbares Modul zum Bewerben auswählen und wird auf die Bewerbungsseite für das Modul weitergeleitet (**applicationModule**). Dort gibt er die entsprechenend Information, die die Klasse "application" braucht, ein und kann nach dem Fertigstellen entweder noch eine Bewerbung hinzufügen oder auf die Übersicht weitergeleitet werden (**/applicationOverview**). Hier sind noch einmal alle Bewerbungen aufgelistet und bearbeitbar.

*Organisator*

Auf seinem Dashboard (**/orgaMain**) sieht der Organisator die ihm zugeteilten Module. Klickt er auf diese, wird er weitergeleitet und sieht geordnet die für sein Modul eingegangenen Bewerbungen und die von den Bewerbern abgegebene Priorität (**/OrgaOverview**). Dort kann er nun seine Priorität und seine gewünschten Wochenarbeitsstunden eintragen und wenn er möchte nochmal die ganze Bewerbung sehen (**/applicationModalContent**).

*Verteiler*

Der Verteiler sieht auf seinem Dashboard (**/distributorMain**) die einzelnen Module des Semesters und die schon durch den Verteileralgorithmus verteilten Bewerber. Diese kann er nun per drag and drop manuell noch verändern oder mit einem Klick auf die Checkbox als final markieren. 

#### (3) << application properties >>

Die Einzelheiten unserer application, wir haben eine Datei für die Development Phase und eine andere für die fertige Produktion. Dies ist nötig, da wir unterschiedliche Datenbanken nutzen wollten um die Entwicklung einfacher zu machen.

#### (4) << csv Dateien >>

Eine statische Liste an entweder Ländern um die Nationalität des Bewerbers auszuwählen, Studiengängen mit demselben Zweck oder Semester in der Form 'WS1920'. Da diese nie bzw. sehr selten bearbeitet oder geändert werden, reicht die CSV Datei anstatt einem Eintrag in der Datenbank.


### Tests {#__name_blackbox_1}

![](https://i.imgur.com/wEchotE.png)

Alle Tests prüfen das Verhalten der zugewiesenen Klassen, wir haben uns für JUnittests und assertj entschieden.

#### (1) << controllertests >>

Überprüft vor allem das Mapping des Controllers und die korrekte Übergabe der Dateien und Klassen.

#### (2) << servicetests >>

Überprüft, dass die Methoden der Services korrekt funktionieren, bei den Services die auf die Datenbank zugreifen wird der Zugriff über die repositories geprüft.
Beim CSV und Mail Service, sowie beim pdf Service werden die Zugriffe auf die entsprechenden Dateien geprüft.

#### (3) << repositorietests >>

Prüft Zugriffe auf die Datenbank, lesende und schreibende.

#### (4) << Modelklassen >>

Da die Klassen fast keine eigene Logik haben, wird meist nur der Builder getestet, mit dem Sie instanziiert werden. Sonst werden die equals und hash Methoden geprüft, die per lombok Annotation hinzugefügt oder überschrieben worden sind.






Laufzeitsicht {#section-runtime-view}
=============

*Standardszenario* {#__emphasis_bezeichnung_laufzeitszenario_1_emphasis}
------------------------------------

1. Ersteller stellt ein neues Modul ein.
2. Bewerber bewirbt sich auf das Modul mit der gewünschten Arbeitsstundenanzahl
3. Organisator und Bewerber geben eine ähnliche Priorität an
4. Bewerbung wird automatisch dem Modul zugeteilt, Verteiler segnet dies ab und verschickt den Einstellungsbogen

*Problemszenario 1* {#__emphasis_bezeichnung_laufzeitszenario_2_emphasis}
------------------------------------
1. Ersteller stellt ein Modul ein, gibt aber die falsche Stundenzahl an.
2. s.o.
3. Der Organisator sieht die falsche Stundenanzahl und meldet sich beim Verteiler
4. Verteiler ändert in Absprache mit dem Bewerber die finale Arbeitszeit

*Problemszenario 2* {#__emphasis_bezeichnung_laufzeitszenario_n_emphasis}
------------------------------------
1. Ersteller stellt ein Modul ein
2. Bewerber bewirbt sich auf mehrere Module und gibt überall eine hohe Priorität an
3. Organisator gibt auch eine hohe Priorität an, bei jeder Bewerbung des Bewerbers
4. Verteilalgorithmus trägt den Bewerber bei einem beliebigen Modul ein
5. Verteiler trägt nach Absprache mit dem Bewerber manuell die Verteilung ein

Dadurch, dass die Datenbank und die darin enthaltenen Daten von jedem Service erreicht werden können, kann eine hohe Modularität und Flexibilität erreicht werden. Die einzelnen Bausteine arbeiten zusammen um einen sicheren Datenaustausch zu garantieren und lässt Platz für Human Errors.

Verteilungssicht {#section-deployment-view}
================

![](https://i.imgur.com/tRmWgzY.png)


**Inhalt.**

Da Wir nur ein Subsystem von vielen Bearbeiten, wurde uns eine graphische Umgebung vorgegeben, die wir mit einbinden. Diese beinhaltet eine Struktur für unsere html Seiten, welche einen Header mit der MOPS Überschrift enthäkt, sowie eine seitliche Navigationsbar die Links zu den unterschiedlichen Subsystemen bereitstellt.

Unsere Application läuft in allen Browsern und ist weitestgehend Betriebssystemübergreifend. Einige Felder nehmen native Aussehensmerkmale des Browsers an, wie beispielsweise die Datumseingabe in einigen Browsern einen Kalender modular aufruft und bei andern ein einfaches Textfeld ist.


Querschnittliche Konzepte {#section-concepts}
=========================


*Fehlerbehandlung* {#__emphasis_konzept_1_emphasis}
---------------

Fehler können zum Beispiel bei den Eingaben in die Felder der Websites vorkommen. Wird ein benötigtes Feld nicht oder falsch ausgefüllt, wird über die html Datei ein Fehler auf der Seite angezeigt. So wird sichergestellt, dass alle Informationen, Beispielsweise bei der Bewerbung, richtig eingetragen werden.

![](https://i.imgur.com/NhivfjK.png)

*Beispiel nicht ausgewählte Nationalität nach dem Klicken auf <<Bestätigen>>*

Logging {#__emphasis_konzept_2_emphasis}
---------------

Alle Subsysteme nutzen ein einheitliches Logging Format, dass über die application properties festgelegt und eingebunden wird.

*\<Konzept n\>* {#__emphasis_konzept_n_emphasis}
---------------

*\<Erklärung\>*

Entwurfsentscheidungen {#section-design-decisions}
======================

**Datenbankauswahl**

Problemstellung:

Eine Datenbank finden, die zu der geplanten Speicherform der Daten passt und bestenfalls an development und production angepasst werden kann.

Relevante Einflussfaktoren:

- Sichere Persistenz zwischen den Sessions in der Produktion ABER isolierte nicht persistente Sessions für die Entwicklung
- Native Unterstützung des gewählten Speicherformats, besonders der unterschiedlichen Felder
- Kleinerer Einfluss: Erfahrung der Entwickler mit den unterschiedlichen Datenbanken

Betrachtete Alternativen:

- Postgres: Interessant, da Anfangs geplant war die Daten im JSON Format zu speichern, biete viel native Unterstützung
- H2:
- MongoDB:
- mySQL:

Entscheidungsfluss:
1. 

**Speicherung der Module**


Module wurden am Anfang in einer CSV Datei als einfacher Text gespeichert. Da aber nach und nach immer mehr Services darauf zugreifen mussten und viel verändert wurde, kam die Idee eine Tabelle in der Datenbank anzulegen. Dies half der Lesbarkeit und Modularität der Modul Objekte.



Qualitätsanforderungen {#section-quality-scenarios}
======================



Qualitätsbaum {#_qualit_tsbaum}
-------------

![](https://i.imgur.com/6hiNFeG.png)


Qualitätsszenarien {#_qualit_tsszenarien}
------------------

| Nr | Szenario |
| -------- | -------- |
| 1 | Der Verteilalgorithmus bekommt eine Bewerbung, die von Organisator und Student die höchste Priorität bekommen hat. Er sortiert sie in das entsprechende Modul. |
| 2 | Ein Student füllt eine Bewerbung aus. Er vergisst, seine Hausnummer einzutragen. Die Anwendung zeigt ihm dort einen Fehler an und lässt ihn erst abschicken, als das Feld gefüllt ist. |
| 3 | Der Organisator sieht auf einen Blick die wichtigstens Informationen seiner Bewerbungen und kann diese bei Bedarf ganz anzeigen |
| 4 |  Die html Seiten werden auch mit Laden der Daten aus der Datenbank in Millisekunden geladen |
| 5 | Der Verteiler finalisiert seine Angaben und es werden automatisch von der Anwendung die Personalbögen erstellen. |
| 6 | Ein Student meldet sich an. Er wird auf die Startseite des Studenten geleitet.|
| 7 | Beim Bearbeiten eines Moduls sieht der Ersteller die vorherigen Angaben in den jeweiligen Textfeldern |
| 8 | Die Wunscharbeitszeit kann von Student und Organisator angegeben und vom Verteiler finalisiert werden|
| 9 | Ein Entwickler möchte eine eine neue Website einstellen. Durch die Projektstruktur und den kommentierten Code findet er sich leicht zurecht und kann seinen content einfach einbinden |

Risiken und technische Schulden {#section-technical-risks}
===============================

**Risiko 1**

Problemstellung

Eventuellfallplanung (Anfänglicher "Plan B")

Risikominderung/Lösung


Glossar {#section-glossary}
=======

**Inhalt.**

Die wesentlichen fachlichen und technischen Begriffe, die Stakeholder im
Zusammenhang mit dem System verwenden.

Nutzen Sie das Glossar ebenfalls als Übersetzungsreferenz, falls Sie in
mehrsprachigen Teams arbeiten.

**Motivation.**

Sie sollten relevante Begriffe klar definieren, so dass alle Beteiligten

1.  diese Begriffe identisch verstehen, und

2.  vermeiden, mehrere Begriffe für die gleiche Sache zu haben.

-   Zweispaltige Tabelle mit \<Begriff\> und \<Definition\>

-   Eventuell weitere Spalten mit Übersetzungen, falls notwendig.



| Begriff | Definition |
| -------- | -------- |
| Text     | Text     |
