create table notification
(
	id int auto_increment,
	notifier int not null,
	receiver int not null,
	outer_id int,
	type int,
	gmt_create Bigint,
	status int default 0,
	constraint NOTIFICATION_PK
		primary key (id)
);