prompt === Add PAST_MEDICAL_HISTORY to CHJ.NURSING_RECORD ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'NURSING_RECORD'
       and column_name = 'PAST_MEDICAL_HISTORY';

    if v_count = 0 then
        execute immediate 'alter table CHJ.NURSING_RECORD add (PAST_MEDICAL_HISTORY varchar2(500))';
    end if;
end;
/

prompt === Done ===
