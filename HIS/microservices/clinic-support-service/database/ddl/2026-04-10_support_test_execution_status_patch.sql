prompt === Add STATUS to CHJ.SUPPORT_TEST_EXECUTION ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SUPPORT_TEST_EXECUTION'
       and column_name = 'STATUS';

    if v_count = 0 then
        execute immediate q'[alter table CHJ.SUPPORT_TEST_EXECUTION add (STATUS varchar2(20) default 'ACTIVE')]';
    end if;
end;
/

update CHJ.SUPPORT_TEST_EXECUTION
   set STATUS = 'ACTIVE'
 where STATUS is null;

commit;

prompt === Done ===
