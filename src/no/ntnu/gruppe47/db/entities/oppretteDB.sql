-- Oppretter SQL-databasen
CREATE DATABASE kalender;
USE kalender;

-- Samling av tabeller som ikke peker til andre tabeller:

CREATE TABLE Person
(
    bruker_id    int                NOT NULL,
    brukernavn    varchar(30)        NOT NULL,
    navn        varchar(60)        NOT NULL,
    epost        varchar(30)        NOT NULL,
    passord        varchar(30)        NOT NULL,

    UNIQUE(brukernavn, epost),
    PRIMARY KEY(bruker_id)    
);

CREATE TABLE Gruppe
(
    gruppe_id    int                NOT NULL,
    navn        varchar(60)        NOT NULL,

    UNIQUE(navn),
    PRIMARY KEY(gruppe_id)
);

CREATE TABLE Rom
(
    rom_id            int AUTO_INCREMENT,
    romnummer         varchar(10)    NOT NULL,
    kapasitet         int,
    PRIMARY KEY ( rom_id )
);


-- Tabeller med fremmednøkkler:
CREATE TABLE Avtale
(
     avtale_id         int         NOT NULL AUTO_INCREMENT,
     start             timestamp     NOT NULL,
     slutt             timestamp     NOT NULL,
     beskrivelse     varchar(255),
     status         enum('avlyst','pågår','avsluttet','planlagt'),
     opprettet_av     int         NOT NULL,
     rom_id            int,
     PRIMARY KEY (avtale_id),
     CONSTRAINT avtale_fk
            FOREIGN KEY (opprettet_av) REFERENCES person( bruker_id ),
            FOREIGN KEY (rom_id) REFERENCES rom( rom_id )
);

CREATE TABLE Har_avtale 
(
    avtale_id         int         NOT NULL,
    tilhører         int         NOT NULL,
    PRIMARY KEY ( avtale_id, tilhører ),
    FOREIGN KEY ( tilhører ) REFERENCES gruppe( gruppe_id ),
    FOREIGN KEY ( avtale_id ) REFERENCES avtale( avtale_id )
);

CREATE TABLE Medlem_av
(
    bruker_id    int        NOT NULL,
    gruppe_id    int        NOT NULL,

    PRIMARY KEY ( bruker_id, gruppe_id ),
    FOREIGN KEY ( bruker_id ) REFERENCES Person( bruker_id ),
    FOREIGN KEY ( gruppe_id ) REFERENCES Gruppe( gruppe_id )
);

CREATE TABLE Varsel
(
    gruppe_id    int                                NOT NULL,
    avtale_id    int                                NOT NULL,
    varsel_type    ENUM('ny', 'avlyst', 'endret')    NOT NULL,

    PRIMARY KEY ( gruppe_id, avtale_id ),
    FOREIGN KEY ( avtale_id ) REFERENCES Avtale( avtale_id ),
    FOREIGN KEY ( gruppe_id ) REFERENCES Gruppe( gruppe_id )
);

CREATE TABLE Alarm
(
    alarm_id     int            NOT NULL AUTO_INCREMENT,
    gruppe_id    int            NOT NULL,
    avtale_id    int            NOT NULL,
    tidspunkt    timestamp    NOT NULL,
    
    PRIMARY KEY ( alarm_id ),
    FOREIGN KEY ( avtale_id ) REFERENCES Avtale( avtale_id ),
    FOREIGN KEY ( gruppe_id ) REFERENCES Gruppe( gruppe_id )
);

CREATE TABLE Innkalling
(
    gruppe_id    int            NOT NULL,
    avtale_id    int            NOT NULL,

    PRIMARY KEY ( gruppe_id, avtale_id ),
    FOREIGN KEY ( avtale_id ) REFERENCES Avtale( avtale_id ),
    FOREIGN KEY ( gruppe_id ) REFERENCES Gruppe( gruppe_id )
);
