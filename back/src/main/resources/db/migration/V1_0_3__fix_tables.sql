alter table request add column if not exists status varchar(100) not null;

alter table product
add constraint products_country_fk foreign key (country_of_origin_id) references country (id);

alter table product alter column image_id type varchar(100);

update storage set name = 'Больничная аптека' where id = 1;