create table if not exists persons
(
    id         bigint not null,
    birth_date date,
    name       varchar(255),
    surname    varchar(255),
    primary key (id)
);

INSERT INTO persons (name, surname, birth_date)
VALUES ('Johan', 'Morrow', '2002-08-11');
INSERT INTO persons (name, surname, birth_date)
VALUES ('Igor', 'Matrovski', '1991-05-18');
INSERT INTO persons (name, surname, birth_date)
VALUES ('Ivan', 'Bublyk', '2003-07-24');
INSERT INTO persons (name, surname, birth_date)
VALUES ('Freddy', 'Krugger', '1986-03-12');
INSERT INTO persons (name, surname, birth_date)
VALUES ('John', 'Bush', '1990-11-26');