create table event_status
(
    id         bigserial primary key,
    saga_id    varchar(100) not null,
    event_type varchar(50)  not null,
    status     varchar(30)  not null,
    payload    jsonb,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now()
);

create index idx_event_status_saga_id on event_status (saga_id);
create index idx_event_status_event_type on event_status (event_type);
create index idx_event_status_status on event_status (status);