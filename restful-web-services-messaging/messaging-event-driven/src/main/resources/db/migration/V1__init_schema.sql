create table outbox_event
(
    id             uuid primary key,
    aggregate_type varchar(50)  not null,
    aggregate_id   varchar(100) not null,
    event_type     varchar(50)  not null,
    payload        text         not null,
    status         varchar(20)  not null,
    created_at     timestamp    not null
);