create table product
(
    id          bigserial primary key,
    name        varchar(255)   not null unique,
    description varchar(1000),
    price       numeric(19, 2) not null,
    category    varchar(100)   not null,
    created_at  timestamp      not null,
    updated_at  timestamp      not null
);