-- :name save-mobile-data
-- :command :returning-execute
-- :result :one
insert into mobile_data(device_id, file_path, file_name, file_data)
values (:device-id, :file-path, :file-name, :file-data)
on conflict (device_id, file_name) do nothing
returning *;

-- :name get-mobile-data
-- :result :many
select device_id, file_path, file_name, created_at
from mobile_data
order by device_id, file_name;

-- :name get-mobile-data-record
-- :result :one
select device_id, file_path, file_name, created_at
from mobile_data
where device_id = :device-id and file_name = :file-name;

-- :name get-mobile-data-record-image
-- :result :one
select file_data, file_name
from mobile_data
where device_id = :device-id and file_name = :file-name;

-- :name delete-mobile-data-record :! :n
delete from mobile_data where device_id = :device-id and file_name = :file-name;
