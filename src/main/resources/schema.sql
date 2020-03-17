DROP TABLE IF EXISTS APPLICANT, ADDRESS, APPLICATION, CERTIFICATE;
create table APPLICANT
(
    id          identity primary key,
    uniserial   varchar,
    refname     varchar,
    surname     VARCHAR,
    name        VARCHAR,
    BIRTHPLACE  VARCHAR,
    BIRTHDAY    VARCHAR,
    NATIONALITY VARCHAR,
    COMMENT     VARCHAR,
    COURSE      VARCHAR,
    STATUS      VARCHAR
);



create table ADDRESS
(
    APPLICANT integer references APPLICANT (id),
    STREET    varchar,
    CITY      varchar,
    COUNTRY   varchar,
    ZIPCODE   integer
);

create table CERTIFICATE
(
    APPLICANT int references APPLICANT (id),
    NAME      varchar,
    COURSE    varchar
);

create table APPLICATION
(
    ID        identity,
    APPLICANT INT references APPLICANT (id),
    HOURS     INT,
    MODULE    VARCHAR,
    PRIORITY  INT,
    GRADE     DOUBLE,
    LECTURER  VARCHAR,
    SEMESTER  VARCHAR,

    ROLE      ENUM ('TUTOR', 'KORREKTOR', 'BOTH')
);




