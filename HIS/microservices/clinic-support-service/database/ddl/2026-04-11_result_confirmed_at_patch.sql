prompt === Add CONFIRMED_AT to result tables without the column ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'CONFIRMED_AT';

    if v_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM_RESULT add (CONFIRMED_AT timestamp(6))';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT'
       and column_name = 'CONFIRMED_AT';

    if v_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN_EXAM_RESULT add (CONFIRMED_AT timestamp(6))';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT'
       and column_name = 'CONFIRMED_AT';

    if v_count = 0 then
        execute immediate 'alter table CHJ.PHYSIOLOGICAL_EXAM_RESULT add (CONFIRMED_AT timestamp(6))';
    end if;
end;
/

prompt === Done ===
