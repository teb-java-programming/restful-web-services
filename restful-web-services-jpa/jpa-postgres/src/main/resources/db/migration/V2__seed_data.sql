insert into authors (name, email)
values ('Shakespeare', 'thebard64@email.com');
insert into authors (name, email)
values ('Tagore', 'guru61dev@email.com');
insert into authors (name, email)
values ('Hemingway', '99papa@email.com');
insert into authors (name, email)
values ('Angelou', 'peoplespoet@email.com');

insert into books (title, isbn, author_id)
values ('Hamlet', 'ISBN-0001', 1);
insert into books (title, isbn, author_id)
values ('The Home and the World', 'ISBN-0002', 2);
insert into books (title, isbn, author_id)
values ('Gitanjali', 'ISBN-0003', 2);
insert into books (title, isbn, author_id)
values ('A Farewell to Arms', 'ISBN-0004', 3);
insert into books (title, isbn, author_id)
values ('And Still I Rise', 'ISBN-0005', 4);
insert into books (title, isbn, author_id)
values ('The Old Man and the Sea', 'ISBN-0006', 3);
insert into books (title, isbn, author_id)
values ('Macbeth', 'ISBN-0007', 1);
insert into books (title, isbn, author_id)
values ('I Know Why the Caged Bird Sings', 'ISBN-0008', 4);

insert into library (name, location)
values ('Central Library', 'Edinburgh');
insert into library (name, location)
values ('National Library', 'Kolkata');

insert into stock (book_id, library_id, quantity)
values (1, 1, 5);
insert into stock (book_id, library_id, quantity)
values (3, 2, 7);
insert into stock (book_id, library_id, quantity)
values (2, 1, 3);
insert into stock (book_id, library_id, quantity)
values (5, 1, 2);
insert into stock (book_id, library_id, quantity)
values (8, 2, 4);
insert into stock (book_id, library_id, quantity)
values (6, 1, 1);
insert into stock (book_id, library_id, quantity)
values (7, 2, 6);
insert into stock (book_id, library_id, quantity)
values (4, 1, 3);