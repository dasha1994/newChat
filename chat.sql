
DROP TABLE users CASCADE CONSTRAINTS;
drop sequence usr_id;
CREATE TABLE users
    ( id  NUMBER NOT NULL PRIMARY KEY,
	name  VARCHAR2(25) NOT NULL,
	ban  VARCHAR2(25)
);
insert into users
values (1,'name','ban');

create sequence usr_id
start with 2
increment by 1;
commit;

