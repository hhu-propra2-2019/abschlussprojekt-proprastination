DROP TABLE if exists applicant, priorization, distribution;

create table applicant
(
	username varchar not null,
	details json
);

create unique index applicant_username_uindex
	on applicant (username);

alter table applicant
	add constraint applicant_pk
		primary key (username);
create table priorization
(
    id int not null
        constraint priorization_pk
            primary key,
    application json,
    priority int
);
create table distribution
(
    module varchar not null
        constraint distribution_pk
            primary key,
    applicants json
);