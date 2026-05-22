alter table request_batch drop column if exists request_id;
alter table request_batch drop constraint if exists request_batch_source_batch_id_fkey;
