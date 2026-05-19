prompt === Add status columns to CHJ.IMAGING_EXAM_RESULT ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'STATUS';

    if v_count = 0 then
        execute immediate q'[alter table CHJ.IMAGING_EXAM_RESULT add (STATUS varchar2(20) default 'ACTIVE')]';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'UPDATED_AT';

    if v_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM_RESULT add (UPDATED_AT timestamp(6))';
    end if;
end;
/

update CHJ.IMAGING_EXAM_RESULT
   set STATUS = 'ACTIVE'
 where STATUS is null;

commit;

prompt === Done ===
