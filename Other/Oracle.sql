--https://github.com/QiuShiqi/XiaoTian

conn scott/admin;

--drop sequence xiaotian_user_add;
--drop sequence xiaotian_friend_add;
--drop sequence xiaotian_record_add;
--drop sequence xiaotian_system_add;

create sequence xiaotian_user_add
	increment by 1
	start with 1
	nomaxvalue
	nocycle;

create sequence xiaotian_friend_add
	increment by 1
	start with 1
	nomaxvalue
	nocycle;


create sequence xiaotian_record_add
	increment by 1
	start with 1
	nomaxvalue
	nocycle;

create sequence xiaotian_system_add
	increment by 1
	start with 1
	nomaxvalue
	nocycle;

--drop table xiaotian_system;
--drop table xiaotian_record;
--drop table xiaotian_friend;
--drop table xiaotian_user;

create table xiaotian_user(
	id number constraint user_id_pk primary key,
	username varchar2(25) not null unique,
	password varchar2(32) not null,
	usergroup varchar2(25) not null,
	time date not null,
	ip varchar2(15) not null,
	type varchar2(25) not null
);


create table xiaotian_friend(
	id number constraint friend_id_pk primary key,
	userid number constraint friend_userid_fk references xiaotian_user(id),
	friendid number constraint friend_friendid_fk references xiaotian_user(id),
	time date not null,
	type varchar2(25) not null
);


create table xiaotian_record(
	id number constraint record_id_pk primary key,
	userid number constraint record_userid_fk references xiaotian_user(id),
	friendid number constraint record_friendid_fk references xiaotian_user(id),
	content varchar2(255) not null,
	time date not null,
	type varchar2(25) not null
);

create table xiaotian_system(
	id number constraint system_id_pk primary key,
	userid number constraint system_userid_fk references xiaotian_user(id),
	content varchar2(255) not null,
	time date not null,
	type varchar2(25) not null
);

--添加用户
insert into xiaotian_user values(xiaotian_user_add.nextval, 'admin', 'qweqwe', 'admin', sysdate, '127.0.0.1', 'default');
insert into xiaotian_user values(xiaotian_user_add.nextval, 'test1', 'test1', 'user', sysdate, '127.0.0.1', 'default');
insert into xiaotian_user values(xiaotian_user_add.nextval, '测试2', 'test2', 'user', sysdate, '127.0.0.1', 'default');
insert into xiaotian_user values(xiaotian_user_add.nextval, 'test3', 'test3', 'user', sysdate, '127.0.0.1', 'default');


--添加关系
insert into xiaotian_friend values(xiaotian_friend_add.nextval, 2, 3, sysdate, 'default');
insert into xiaotian_friend values(xiaotian_friend_add.nextval, 2, 4, sysdate, 'default');
insert into xiaotian_friend values(xiaotian_friend_add.nextval, 3, 2, sysdate, 'default');
insert into xiaotian_friend values(xiaotian_friend_add.nextval, 3, 4, sysdate, 'default');
insert into xiaotian_friend values(xiaotian_friend_add.nextval, 4, 2, sysdate, 'default');
insert into xiaotian_friend values(xiaotian_friend_add.nextval, 4, 3, sysdate, 'default');

--添加记录
insert into xiaotian_record values(xiaotian_record_add.nextval, 2, 3, '2->3.1', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 2, 3, '2->3.2', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 2, 3, '2->3.3', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 3, 2, 'test3->2', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 3, 4, 'test3->4', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 4, 3, 'test4->3', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 2, 4, 'test2->4.1', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 2, 4, 'test2->4.2', sysdate, 'default');
insert into xiaotian_record values(xiaotian_record_add.nextval, 2, 4, 'test2->4.3', sysdate, 'default');

--添加配置	1代表所有用户
insert into xiaotian_system values(xiaotian_system_add.nextval, 1, 'on', sysdate, 'system_type');

--重启数据库
conn sys/admin as sysdba;
shutdow immediate;
startup;
conn scott/admin;