create table mobile_data(
    device_id text,
    file_path text,
    file_name text,
    created_at timestamp with time zone default clock_timestamp() not null,
    file_data bytea,
    primary key (device_id, file_name)
);
