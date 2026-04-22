create table users
(
    id                 bigserial primary key,
    username           varchar(100) not null unique,
    email              varchar(150) not null unique,
    password           varchar(255) not null,
    enabled            boolean      not null default true,
    role               varchar(50)  not null default 'USER',
    account_non_locked boolean      not null default true,
    failed_attempts    int          not null default 0,
    locked_until       timestamp,
    mfa_enabled        boolean      not null default false,
    mfa_secret         varchar(255),
    created_at         timestamp    not null,
    updated_at         timestamp
);

create table refresh_tokens
(
    id          bigserial primary key,
    user_id     bigint       not null,
    token       varchar(500) not null unique,
    expiry_date timestamp    not null,
    revoked     boolean      not null default false,
    device_id   varchar(255),
    ip_address  varchar(100),
    user_agent  varchar(500),
    created_at  timestamp    not null,
    updated_at  timestamp,

    constraint fk_refresh_tokens_user
        foreign key (user_id)
            references users (id)
            on delete cascade
);

create table audit_logs
(
    id         bigserial primary key,
    user_id    bigint,
    event_type varchar(50) not null,
    message    varchar(255),
    ip_address varchar(100),
    user_agent varchar(500),
    created_at timestamp   not null
);

create index idx_refresh_tokens_user_id on refresh_tokens (user_id);
create index idx_refresh_tokens_token on refresh_tokens (token);
create index idx_audit_logs_user_id on audit_logs (user_id);
create index idx_audit_logs_event_type on audit_logs (event_type);