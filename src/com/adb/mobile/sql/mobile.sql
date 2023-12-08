-- :name save-mobile-photo
-- :command :returning-execute
-- :result :one
insert into mobile_photos(device_id, file_path, file_name, file_data)
values (:device-id, :file-path, :file-name, :file-data)
on conflict (device_id, file_name) do nothing
returning *;

-- :name get-mobile-photos
-- :result :many
select device_id, file_path, file_name, created_at
from mobile_photos
order by device_id, created_at desc;

-- :name get-mobile-photo-record-image
-- :result :one
select file_data, file_name
from mobile_photos
where device_id = :device-id and file_name = :file-name;

-- :name save-mobile-call
-- :command :returning-execute
-- :result :one
insert into mobile_calls(device_id, id, number, duration, name, created_at)
values (:device-id, :id, :number, :duration, :name, :created-at)
on conflict (device_id, id) do nothing
returning *;

-- :name get-mobile-calls
-- :result :many
select *
from mobile_calls
order by device_id, created_at desc;

-- :name save-mobile-contact
-- :command :returning-execute
-- :result :one
insert into mobile_contacts(device_id, id, display_name, contact_id, number)
values (:device-id, :id, :display-name, :contact-id, :number)
on conflict (device_id, id) do nothing
returning *;

-- :name get-mobile-contacts
-- :result :many
select *
from mobile_contacts
order by device_id, contact_id desc;

-- :name save-mobile-sms*
-- :command :returning-execute
-- :result :one
insert into mobile_sms(device_id, id, address, body, creator, seen, created_at)
values (:device-id, :id, :address, :body, :creator, :seen, :created-at)
on conflict (device_id, id) do nothing
returning *;

-- :name get-mobile-sms
-- :result :many
select *
from mobile_sms
order by device_id, created_at desc;
