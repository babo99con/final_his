prompt === Remove default from CHJ.SPECIMEN_EXAM.SPECIMEN_STATUS ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM'
       and column_name = 'SPECIMEN_STATUS';

    if v_count > 0 then
        execute immediate q'[alter table CHJ.SPECIMEN_EXAM modify (SPECIMEN_STATUS default null)]';
    end if;
end;
/
