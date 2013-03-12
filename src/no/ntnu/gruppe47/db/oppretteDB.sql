-- Oppretter SQL-databasen
CREATE DATABASE kalender;
CREATE TABLE avtale (
	 avtale_ID int NOT NULL AUTO_INCREMENT,
	 start timestamp NOT NULL,
	 slutt timestamp NOT NULL,
	 beskrivelse varchar(255),
	 status enum('avlyst','pågår','avsluttet','planlagt'),
	 opprettet_av varchar(60),
	 rom int,
	 PRIMARY KEY (avtale_ID)
	 FOREIGN KEY (opprettet_av) REFERENCES person( brukernavn ),
	 FOREIGN KEY (rom) REFERENCES rom(romnummer)
);