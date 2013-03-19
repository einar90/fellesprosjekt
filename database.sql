 DROP DATABASE IF EXISTS kalender;

CREATE DATABASE kalender;

USE kalender;

CREATE TABLE person
  (
     bruker_id  INT NOT NULL auto_increment,
     brukernavn VARCHAR(30) NOT NULL,
     navn       VARCHAR(60) NOT NULL,
     epost      VARCHAR(30) NOT NULL,
     passord    VARCHAR(30) NOT NULL,
     UNIQUE(brukernavn),
     PRIMARY KEY(bruker_id)
  );

CREATE TABLE gruppe
  (
     gruppe_id INT NOT NULL auto_increment,
     navn      VARCHAR(60) NOT NULL,
     privat    BOOLEAN NOT NULL DEFAULT false,
     UNIQUE(navn),
     PRIMARY KEY(gruppe_id)
  );

CREATE TABLE rom
  (
     rom_id    INT auto_increment,
     romnummer VARCHAR(10) NOT NULL,
     kapasitet INT,
     PRIMARY KEY ( rom_id )
  );

CREATE TABLE avtale
  (
     avtale_id    INT NOT NULL auto_increment,
     start        TIMESTAMP NOT NULL,
     slutt        TIMESTAMP NOT NULL,
     beskrivelse  VARCHAR(255),
     status       ENUM('avlyst', 'pågår', 'avsluttet', 'planlagt'),
     opprettet_av INT NOT NULL,
     rom_id       INT DEFAULT 0,
     sted         VARCHAR(60) DEFAULT '',
     PRIMARY KEY (avtale_id),
     CONSTRAINT avtale_fk FOREIGN KEY (opprettet_av) REFERENCES person(
     bruker_id ),
     FOREIGN KEY (rom_id) REFERENCES rom( rom_id )
  );

CREATE TABLE har_avtale
  (
     avtale_id INT NOT NULL,
     bruker_id INT NOT NULL,
     PRIMARY KEY ( avtale_id, bruker_id ),
     FOREIGN KEY ( bruker_id ) REFERENCES bruker( bruker_id ),
     FOREIGN KEY ( avtale_id ) REFERENCES avtale( avtale_id )
  );

CREATE TABLE medlem_av
  (
     bruker_id INT NOT NULL,
     gruppe_id INT NOT NULL,
     PRIMARY KEY ( bruker_id, gruppe_id ),
     FOREIGN KEY ( bruker_id ) REFERENCES person( bruker_id ),
     FOREIGN KEY ( gruppe_id ) REFERENCES gruppe( gruppe_id )
  );

CREATE TABLE varsel
  (
     bruker_id INT NOT NULL,
     avtale_id INT NOT NULL,
     tekst     VARCHAR(30),
     FOREIGN KEY ( bruker_id ) REFERENCES bruker( bruker_id ),
     FOREIGN KEY ( avtale_id ) REFERENCES avtale( avtale_id )
  );

CREATE TABLE alarm
  (
     avtale_id INT NOT NULL,
     bruker_id INT NOT NULL,
     tidspunkt TIMESTAMP NOT NULL,
     FOREIGN KEY ( bruker_id ) REFERENCES bruker( bruker_id ),
     FOREIGN KEY ( avtale_id ) REFERENCES avtale( avtale_id )
  );

CREATE TABLE innkalling
  (
     avtale_id INT NOT NULL,
     bruker_id INT NOT NULL,
     svar      INT,
     PRIMARY KEY ( avtale_id, bruker_id ),
     FOREIGN KEY ( bruker_id ) REFERENCES bruker( bruker_id ),
     FOREIGN KEY ( avtale_id ) REFERENCES avtale( avtale_id )
  );  