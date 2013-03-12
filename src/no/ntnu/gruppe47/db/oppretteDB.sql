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


CREATE TABLE Person
(
	bruker_id	int				NOT NULL,
	brukernavn	varchar(30)		NOT NULL,
	navn		varchar(60)		NOT NULL,
	epost		varchar(30)		NOT NULL,
	passord		varchar(30)		NOT NULL,

	UNIQUE(brukernavn, epost),
	PRIMARY KEY(bruker_id)	
);

CREATE TABLE MedlemAv
(
	bruker_id	int		NOT NULL,
	gruppe_id	int		NOT NULL,

	FOREIGN KEY(bruker_id) REFERENCES Person(bruker_id),
	FOREIGN KEY(gruppe_id) REFERENCES Gruppe(gruppe_id)
);

CREATE TABLE Gruppe
(
	gruppe_id	int				NOT NULL,
	navn		varchar(60)		NOT NULL,

	UNIQUE(navn),
	PRIMARY KEY(gruppe_id)
);

CREATE TABLE Varsel
(
	gruppe_id	int								NOT NULL,
	avtale_id	int								NOT NULL,
	varsel_type	ENUM('ny', 'avlyst', 'endret')	NOT NULL,

	FOREIGN KEY(bruker_id) REFERENCES Person(bruker_id),
	FOREIGN KEY(gruppe_id) REFERENCES Gruppe(gruppe_id)
);

CREATE TABLE Alarm
(
	gruppe_id	int			NOT NULL,
	avtale_id	int			NOT NULL,
	tidspunkt	timestamp	NOT NULL,
	
	FOREIGN KEY(avtale_id) REFERENCES Person(avtale_id),
	FOREIGN KEY(gruppe_id) REFERENCES Gruppe(gruppe_id)
);

CREATE TABLE Innkalling
(
	gruppe_id	int			NOT NULL,
	avtale_id	int			NOT NULL,

	FOREIGN KEY(avtale_id) REFERENCES Person(avtale_id),
	FOREIGN KEY(gruppe_id) REFERENCES Gruppe(gruppe_id)
);
