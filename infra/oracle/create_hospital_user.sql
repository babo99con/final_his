whenever sqlerror exit sql.sqlcode

alter session set "_ORACLE_SCRIPT"=true;

begin
  execute immediate 'drop user HOSPITAL cascade';
exception
  when others then
    if sqlcode != -1918 then
      raise;
    end if;
end;
/

create user HOSPITAL identified by 1111 default tablespace USERS temporary tablespace TEMP quota unlimited on USERS;

grant create session, resource, connect to HOSPITAL;
grant create view, create sequence, create trigger, create procedure, create table to HOSPITAL;

create or replace directory RESTORE_DIR as '/dump';
grant read, write on directory RESTORE_DIR to HOSPITAL;

exit;
