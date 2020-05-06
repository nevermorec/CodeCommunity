alter table comment modify id int auto_increment;
alter table comment modify parent_id int not null;