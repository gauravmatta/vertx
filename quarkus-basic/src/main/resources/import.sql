CREATE SEQUENCE hibernate_sequence START 1;
insert into users(id,name) values (nextval('hibernate_sequence'),'Gaurav');
insert into users(id,name) values (nextval('hibernate_sequence'),'Raghavi');
insert into users(id,name) values (nextval('hibernate_sequence'),'Ekta');