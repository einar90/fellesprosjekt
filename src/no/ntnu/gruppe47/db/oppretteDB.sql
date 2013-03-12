-- Oppretter SQL-databasen
CREATE DATABASE kalender;

CREATE TABLE Avtale
(
	 avtale_id 		int 		NOT NULL AUTO_INCREMENT,
	 start 			timestamp 	NOT NULL,
	 slutt 			timestamp 	NOT NULL,
	 beskrivelse 	varchar(255),
	 status 		enum('avlyst','pågår','avsluttet','planlagt'),
	 opprettet_av 	varchar(60), NOT NULL
	 romnummer		varchar(10),
	 PRIMARY KEY (avtale_id)
	 FOREIGN KEY (opprettet_av) REFERENCES person( brukernavn ),
	 FOREIGN KEY (rom) REFERENCES rom( romnummer )
);

CREATE TABLE Rom
(
	romnummer 		varchar(10)	NOT NULL AUTO_INCREMENT,
	kapasitet 		int,
	PRIMARY KEY (romnummer)
);

CREATE TABLE Har_avtale 
(
	avtale_id 		int 		NOT NULL AUTO_INCREMENT,
	tilhører 		varchar(60) NOT NULL,
	PRIMARY KEY (avtale_id, tilhører),
	FOREIGN KEY (tilhører) REFERENCES gruppe( gruppe_id ),
	FOREIGN KEY (avtale_id) REFERENCES avtale( avtale_id )
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

CREATE TABLE Medlem_av
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
