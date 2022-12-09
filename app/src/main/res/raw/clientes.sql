create table clientes (
    nif char(9) primary key,
    nombre varchar(50) not null,
    fecha date not null,
    estudiante integer not null,
    sexo char(6) not null
);

create table tarjetas (
    nif char(9),
    numero char(16) not null,
    mes integer not null,
    año integer not null,
    cvc integer not null,
    primary key (nif, numero),
    foreign key (nif) references clientes (nif)
);