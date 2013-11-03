LEM Lab Software
==============
Introduzione: Software sviluppato dalla dita LEM Labs a Trento per la gestione d'interventi su macchinarie industriali. L'applicativo in questione vuole sfruttare la potenzialità della infrasttruttura GAE (Google App Engine - offerta da Google) ed è sviluppato utilizzando la GWT (Google Web Tools - in java). 

Revision History
------------------------------------------------------------------------
Ver 0.0.1 - 03/11/2013
------------------------------------------------------------------------
POC (Proof of Concept) release:
- Vesione iniziale caricata sui server di Google ed accesibile (per intanto) al indirizzo http://1.sbassabesoft.appspot.com/
- Il DataStore è stato caricato con ben 10 record ed utilizza soltanto 2 entity (Intervento e SearchItem).
- La ricerca aggisce su tutti i campi (Data, Operatore, Macchina e Descrizione) ed è 'case insensitive'.
- Il bottone 'Populate' non fa niente e serve soltanto per caricare i dati la prima volta.
- Su questo primo commit ho caricato tutto il progetto Eclipse (in futuro leviamo le rotelle e salviamo soltanto il codice java!)