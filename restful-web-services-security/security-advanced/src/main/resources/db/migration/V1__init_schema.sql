create table users
(
    id            bigint generated always as identity primary key,
    username      varchar(255) not null unique,
    password      varchar(255) not null,
    token_version bigint       not null default 0
);

create table user_roles
(
    user_id bigint      not null,
    role    varchar(50) not null,

    constraint fk_user_roles_user
        foreign key (user_id)
            references users (id)
            on delete cascade
);

create table refresh_tokens
(
    id       bigint generated always as identity primary key,
    token    varchar(255) not null unique,
    username varchar(255) not null,
    expiry   timestamp    not null
);

create table revoke_tokens
(
    id         bigint generated always as identity primary key,
    jti        varchar(255) not null unique,
    revoked_at timestamp    not null
);