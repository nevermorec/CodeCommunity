create table notification
(
	id int auto_increment,
	notifier int not null,
	receiver int not null,
	outer_id int,
	type int,
	gmt_create bigint,
	status int default 0,
	constraint notification_pk
		primary key (id)
);