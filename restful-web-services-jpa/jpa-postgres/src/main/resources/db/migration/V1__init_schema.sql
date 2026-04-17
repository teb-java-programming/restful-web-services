create table authors
(
    id    bigint generated always as identity primary key,
    name  varchar(255),
    email varchar(255)
);

create table books
(
    id        bigint generated always as identity primary key,
    title     varchar(255),
    isbn      varchar(255),
    author_id bigint not null,

    constraint fk_books_author
        foreign key (author_id)
            references authors (id)
            on delete cascade
);

create table library
(
    id       bigint generated always as identity primary key,
    name     varchar(255),
    location varchar(255)
);

create table stock
(
    id         bigint generated always as identity primary key,
    book_id    bigint not null,
    library_id bigint not null,
    quantity   bigint,

    constraint fk_stock_book
        foreign key (book_id)
            references books (id)
            on delete cascade,

    constraint fk_stock_library
        foreign key (library_id)
            references library (id)
            on delete cascade
);