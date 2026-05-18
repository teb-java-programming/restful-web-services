create table ride
(
    ride_id    varchar(64) primary key,
    user_id    varchar(64) not null,
    driver_id  varchar(64),
    status     varchar(32) not null,
    updated_at timestamp   not null
);

create table ride_event
(
    event_id     varchar(64) primary key,
    ride_id      varchar(64) not null,
    event_type   varchar(64) not null,
    payload_json text        not null,
    created_at   timestamp   not null
);

create table ride_outbox
(
    event_id     varchar(64) primary key,
    aggregate_id varchar(64) not null,
    event_type   varchar(64) not null,
    payload_json text        not null,
    processed    boolean     not null default false,
    created_at   timestamp   not null
);