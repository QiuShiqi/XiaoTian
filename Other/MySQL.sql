#https://github.com/QiuShiqi/XiaoTian

#drop database XiaoTian;
create database XiaoTian character set utf8;
use XiaoTian;

#drop table xiaotian_system;
#drop table xiaotian_record;
#drop table xiaotian_friend;
#drop table xiaotian_user;

create table xiaotian_user(
	id int primary key auto_increment,
	username varchar(25) not null unique,
	password varchar(32) not null,
	usergroup varchar(25) not null,
	time datetime not null,
	ip varchar(15) not null,
	type varchar(25) not null
);


create table xiaotian_friend(
	id int primary key auto_increment,
	userid int not null,
	friendid int not null,
	time datetime not null,
	type varchar(25) not null
);


create table xiaotian_record(
	id int primary key auto_increment,
	userid int not null,
	friendid int not null,
	content varchar(255) not null,
	time datetime not null,
	type varchar(25) not null
);

create table xiaotian_system(
	id int primary key auto_increment,
	userid int not null,
	content varchar(255) not null,
	time datetime not null,
	type varchar(25) not null
);

#添加用户
insert into xiaotian_user values(0, 'admin', 'qweqwe', 'admin', sysdate(), '127.0.0.1', 'default');
insert into xiaotian_user values(0, 'test1', 'test1', 'user', sysdate(), '127.0.0.1', 'default');
insert into xiaotian_user values(0, '测试2', 'test2', 'user', sysdate(), '127.0.0.1', 'default');
insert into xiaotian_user values(0, 'test3', 'test3', 'user', sysdate(), '127.0.0.1', 'default');


#添加关系
insert into xiaotian_friend values(0, 2, 3, sysdate(), 'default');
insert into xiaotian_friend values(0, 2, 4, sysdate(), 'default');
insert into xiaotian_friend values(0, 3, 2, sysdate(), 'default');
insert into xiaotian_friend values(0, 3, 4, sysdate(), 'default');
insert into xiaotian_friend values(0, 4, 2, sysdate(), 'default');
insert into xiaotian_friend values(0, 4, 3, sysdate(), 'default');

#添加记录
insert into xiaotian_record values(0, 2, 3, '2->3.1', sysdate(), 'default');
insert into xiaotian_record values(0, 2, 3, '2->3.2', sysdate(), 'default');
insert into xiaotian_record values(0, 2, 3, '2->3.3', sysdate(), 'default');
insert into xiaotian_record values(0, 3, 2, 'test3->2', sysdate(), 'default');
insert into xiaotian_record values(0, 3, 4, 'test3->4', sysdate(), 'default');
insert into xiaotian_record values(0, 4, 3, 'test4->3', sysdate(), 'default');
insert into xiaotian_record values(0, 2, 4, 'test2->4.1', sysdate(), 'default');
insert into xiaotian_record values(0, 2, 4, 'test2->4.2', sysdate(), 'default');
insert into xiaotian_record values(0, 2, 4, 'test2->4.3', sysdate(), 'default');

#添加配置	1代表所有用户
insert into xiaotian_system values(0, 1, 'on', sysdate(), 'system_type');
