create sequence hibernate_sequence start with 1 increment by 1;
create table base (id bigint not null, latitude double not null, longitude double not null, name varchar(255), primary key (id));
create table item (id bigint not null, name varchar(255), value integer not null, primary key (id));
create table rebel (id bigint not null, age integer not null check (age>=18), gender integer, name varchar(255), number_betrayal_reports integer not null, base_id bigint, primary key (id));
alter table rebel add constraint FKgaykaa35v3jb9ymxm4fjn5533 foreign key (base_id) references base;
create sequence hibernate_sequence start with 1 increment by 1;
create table base (id bigint not null, latitude double not null, longitude double not null, name varchar(255), primary key (id));
create table inventary (rebel_id bigint not null, item_id bigint not null, primary key (rebel_id, item_id));
create table item (id bigint not null, name varchar(255), value integer not null, primary key (id));
create table item_rebels (item_id bigint not null, rebels_id bigint not null, primary key (item_id, rebels_id));
create table rebel (id bigint not null, age integer not null check (age>=18), gender integer, name varchar(255), number_betrayal_reports integer not null, base_id bigint, primary key (id));
alter table inventary add constraint FK9bb2rnhfb787e2xc7kty4m5xj foreign key (item_id) references item;
alter table inventary add constraint FKd1n6cy5r74g133clj5k1xa2cs foreign key (rebel_id) references rebel;
alter table item_rebels add constraint FKi7ket8yolyme2k5vp5ij2bv3c foreign key (rebels_id) references rebel;
alter table item_rebels add constraint FKjyj459dbxh4s2xqoxltpvpw3o foreign key (item_id) references item;
alter table rebel add constraint FKgaykaa35v3jb9ymxm4fjn5533 foreign key (base_id) references base;
create sequence hibernate_sequence start with 1 increment by 1;
create table base (id bigint not null, latitude double not null, longitude double not null, name varchar(255), primary key (id));
create table inventary (rebel_id bigint not null, item_id bigint not null, primary key (rebel_id, item_id));
create table item (id bigint not null, name varchar(255), value integer not null, primary key (id));
create table item_rebels (item_id bigint not null, rebels_id bigint not null, primary key (item_id, rebels_id));
create table rebel (id bigint not null, age integer not null check (age>=18), gender integer, name varchar(255), number_betrayal_reports integer not null, base_id bigint, primary key (id));
alter table inventary add constraint FK9bb2rnhfb787e2xc7kty4m5xj foreign key (item_id) references item;
alter table inventary add constraint FKd1n6cy5r74g133clj5k1xa2cs foreign key (rebel_id) references rebel;
alter table item_rebels add constraint FKi7ket8yolyme2k5vp5ij2bv3c foreign key (rebels_id) references rebel;
alter table item_rebels add constraint FKjyj459dbxh4s2xqoxltpvpw3o foreign key (item_id) references item;
alter table rebel add constraint FKgaykaa35v3jb9ymxm4fjn5533 foreign key (base_id) references base;
