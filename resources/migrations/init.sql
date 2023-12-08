create table mobile_photos (
    device_id text,
    file_path text,
    file_name text,
    created_at timestamp with time zone default clock_timestamp() not null,
    file_data bytea,
    primary key (device_id, file_name)
);

create table mobile_calls (
    device_id  text,
    id         text,
    number     text,
    duration   text,
    name       text,
    created_at timestamp with time zone,
    primary key (device_id, id)
);

create table mobile_contacts (
    device_id    text,
    id           text,
    display_name text,
    contact_id   text,
    number       text,
    primary key (device_id, id)
);

create table mobile_sms (
    device_id  text,
    id         text,
    address    text,
    body       text,
    creator    text,
    seen       boolean,
    created_at timestamp with time zone,
    primary key (device_id, id)
);
