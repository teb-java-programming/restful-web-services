create table event_store
(
    id           bigserial primary key,
    event_id     varchar(100) not null unique,
    event_type   varchar(100) not null,
    aggregate_id varchar(100) not null,
    payload      jsonb        not null,
    created_at   timestamp    not null default current_timestamp
);

create index idx_event_store_aggregate_id on event_store (aggregate_id);
create index idx_event_store_event_type on event_store (event_type);