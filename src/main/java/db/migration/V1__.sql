-- the first script for migration
create sequence hibernate_sequence;
	
create table Quip (
    id bigint primary key default nextval('hibernate_sequence'),
    content varchar(10000),
    score bigint,
    source varchar(255),
    postedAt timestamp,
    sourceDate timestamp
);

create table Quip_authorIds (
    Quip_id bigint primary key default nextval('hibernate_sequence'),
    authorIds bigint
);
   
create table Users (
    id bigint primary key default nextval('hibernate_sequence'),
    fullname varchar(255),
    isAdmin boolean not null,
    password varchar(255),
    username varchar(255)
);
    
alter table Quip_authorIds
add constraint FK_f9ivk719aqb0rqd8my08loev7 
foreign key (Quip_id)
references Quip;